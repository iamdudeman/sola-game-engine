package technology.sola.engine.tooling.font;

import technology.sola.engine.assets.graphics.font.FontGlyph;
import technology.sola.engine.assets.graphics.font.FontInfo;
import technology.sola.engine.assets.graphics.font.FontStyle;
import technology.sola.engine.assets.graphics.font.mapper.FontInfoJsonMapper;
import technology.sola.engine.tooling.Tool;
import technology.sola.json.SolaJson;

import javax.imageio.ImageIO;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class FontRasterizerTool implements Tool {
  public static final String DEFAULT_CHARACTERS = "abcdefghijklmnopqrstuvwxyz{|}~ !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`";
  private final File parentDirectory;
  private final String characters;

  public FontRasterizerTool(File parentDirectory) {
    this(parentDirectory, DEFAULT_CHARACTERS);
  }

  public FontRasterizerTool(File parentDirectory, String characters) {
    this.parentDirectory = parentDirectory;
    this.characters = characters;
  }

  @Override
  public String getCommand() {
    return "rasterizeFont";
  }

  @Override
  public String getHelp() {
    return """
      *arg1 - Font name ["monospaced", "arial", "times"]
      *arg2 - Font size ["16", "24"]
      arg3  - Font style ["NORMAL", "ITALIC", "BOLD"] defaults to NORMAL
      """;
  }

  @Override
  public String execute(String[] args) {
    if (args.length >= 2) {
      String fontName = args[0];
      int fontSize = Integer.parseInt(args[1]);
      String fontStyle = args.length > 2 ? args[2] : "NORMAL";

      String fileCreated = rasterizeFont(fontName, fontStyle, fontSize);

      return "New font info successfully created at [" + fileCreated + "]";
    } else {
      throw new RuntimeException("Must provide at least first two arguments [fontName] [fontSize] [fontStyle]");
    }
  }

  public String rasterizeFont(String fontName, String fontStyle, int fontSize) {
    var fontInformation = new FontInformation(fontName, FontStyle.valueOf(fontStyle), fontSize);

    try (var fontCanvas = prepareFontCanvas(fontInformation)) {
      var fontInfo = prepareFontInfo(fontInformation, fontCanvas);

      File fontImageFile = new File(parentDirectory, fontInformation.getFontFileName());
      File fontInfoFile = new File(parentDirectory, fontInformation.getFontInfoFileName());

      try {
        ImageIO.write(fontCanvas.getBufferedImage(), "png", fontImageFile);
        Files.write(fontInfoFile.toPath(), serializeFontInfo(fontInfo));
        return fontInfoFile.toString();
      } catch (IOException ex) {
        ex.printStackTrace();
        throw new RuntimeException(ex);
      }
    }
  }

  private FontCanvas prepareFontCanvas(FontInformation fontInformation) {
    Rectangle2D fullBounds = fontInformation.getStringBounds(characters);
    int imageWidth = (int)fullBounds.getWidth() / 2;
    int imageHeight = (int)fullBounds.getHeight() * 5;

    return new FontCanvas(fontInformation, imageWidth, imageHeight);
  }

  private FontInfo prepareFontInfo(FontInformation fontInformation, FontCanvas fontCanvas) {
    List<FontGlyph> fontGlyphsWithPositions = fontCanvas.drawFontGlyphs(characters);

    return new FontInfo(
      fontInformation.getFontFileName(), fontInformation.getFontName(),
      FontStyle.valueOf(fontInformation.getFontStyle()), fontInformation.getFontSize(),
      fontInformation.getLeading(), fontGlyphsWithPositions
    );
  }

  private byte[] serializeFontInfo(FontInfo fontInfo) {
    return new SolaJson().stringify(fontInfo, new FontInfoJsonMapper()).getBytes(StandardCharsets.UTF_8);
  }
}
