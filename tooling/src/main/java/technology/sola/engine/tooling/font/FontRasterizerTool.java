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
import java.nio.file.Path;
import java.util.List;

/**
 * FontRasterizerTool generates a font json and font png file that can be used by the sola game engine.
 */
public class FontRasterizerTool implements Tool {
  /**
   * Default characters used in the font rasterizer tool.
   */
  public static final String DEFAULT_CHARACTERS = "abcdefghijklmnopqrstuvwxyz{|}~ !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`";
  private final File parentDirectory;
  private final String characters;

  /**
   * Creates a FontRasterizerTool using the current directory as the output directory and {@link FontRasterizerTool#DEFAULT_CHARACTERS} characters
   */
  public FontRasterizerTool() {
    this(new File(Path.of("").toAbsolutePath().toString()));
  }

  /**
   * Creates a FontRasterizerTool using desired parentDirectory as output and {@link FontRasterizerTool#DEFAULT_CHARACTERS} characters
   *
   * @param parentDirectory the directory to output the generated files to
   */
  public FontRasterizerTool(File parentDirectory) {
    this(parentDirectory, DEFAULT_CHARACTERS);
  }

  /**
   * Creates a FontRasterizerTool using desired parentDirectory as output and desired characters included.
   *
   * @param parentDirectory the directory to output the generated files to
   * @param characters      the characters to include in the font assets
   */
  public FontRasterizerTool(File parentDirectory, String characters) {
    this.parentDirectory = parentDirectory;
    this.characters = characters;
  }

  @Override
  public String getName() {
    return "fontRasterize";
  }

  @Override
  public String getHelp() {
    return """
      *arg1 - Font family ["monospaced", "arial", "times"]
      *arg2 - Font size ["16", "24"]
      arg3  - Font style ["NORMAL", "ITALIC", "BOLD", "BOLD_ITALIC"] defaults to NORMAL
      """;
  }

  @Override
  public String execute(String... args) {
    if (args.length >= 2) {
      String fontFamily = args[0];
      int fontSize = Integer.parseInt(args[1]);
      String fontStyle = args.length > 2 ? args[2].toUpperCase() : "NORMAL";

      String fileCreated = rasterizeFont(fontFamily, fontStyle, fontSize);

      return "New font info successfully created at [" + fileCreated + "]";
    } else {
      throw new RuntimeException("Must provide at least first two arguments [fontFamily] [fontSize] [fontStyle]");
    }
  }

  /**
   * Generates a font info and font image file for the desired font family with a font style and size.
   * See {@link FontListTool} to view what font families are available.
   *
   * @param fontFamily the font family to generate asset for
   * @param fontStyle  the font style [NORMAL, ITALIC, BOLD]
   * @param fontSize   the font size
   * @return the path to the font info file that was generated
   */
  public String rasterizeFont(String fontFamily, String fontStyle, int fontSize) {
    var fontInformation = new FontInformation(fontFamily, FontStyle.valueOf(fontStyle), fontSize);

    try (var fontCanvas = prepareFontCanvas(fontInformation)) {
      var fontInfo = prepareFontInfo(fontInformation, fontCanvas);

      File fontImageFile = new File(parentDirectory, fontInformation.getFontFileName());
      File fontInfoFile = new File(parentDirectory, fontInformation.getFontInfoFileName());

      try {
        ImageIO.write(fontCanvas.getBufferedImage(), "png", fontImageFile);
        Files.write(fontInfoFile.toPath(), serializeFontInfo(fontInfo));
        return fontInfoFile.toString();
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  private FontCanvas prepareFontCanvas(FontInformation fontInformation) {
    Rectangle2D fullBounds = fontInformation.getStringBounds(characters);
    int imageWidth = (int) fullBounds.getWidth() / 2;
    int imageHeight = (int) fullBounds.getHeight() * 5;

    return new FontCanvas(fontInformation, imageWidth, imageHeight);
  }

  private FontInfo prepareFontInfo(FontInformation fontInformation, FontCanvas fontCanvas) {
    List<FontGlyph> fontGlyphsWithPositions = fontCanvas.drawFontGlyphs(characters);

    return new FontInfo(
      fontInformation.getFontFileName(), fontInformation.getFontFamily(),
      FontStyle.valueOf(fontInformation.getFontStyle()), fontInformation.getFontSize(),
      fontInformation.getLeading(), fontGlyphsWithPositions
    );
  }

  private byte[] serializeFontInfo(FontInfo fontInfo) {
    return new SolaJson().stringify(fontInfo, new FontInfoJsonMapper()).getBytes(StandardCharsets.UTF_8);
  }
}
