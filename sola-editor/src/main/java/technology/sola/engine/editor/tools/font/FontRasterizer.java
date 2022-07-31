package technology.sola.engine.editor.tools.font;

import technology.sola.engine.assets.graphics.font.FontGlyph;
import technology.sola.engine.assets.graphics.font.FontInfo;
import technology.sola.engine.assets.graphics.font.FontStyle;
import technology.sola.engine.assets.graphics.font.mapper.FontInfoJsonMapper;
import technology.sola.json.SolaJson;

import javax.imageio.ImageIO;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class FontRasterizer {
  public static final String DEFAULT_CHARACTERS = "abcdefghijklmnopqrstuvwxyz{|}~ !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`";
  private final File parentDirectory;
  private final String characters;

  public FontRasterizer(File parentDirectory) {
    this(parentDirectory, DEFAULT_CHARACTERS);
  }

  public FontRasterizer(File parentDirectory, String characters) {
    this.parentDirectory = parentDirectory;
    this.characters = characters;
  }

  public void rasterizeFont(String fontName, String fontStyle, int fontSize) {
    var fontInformation = new FontInformation(fontName, FontStyle.valueOf(fontStyle), fontSize);

    try (var fontCanvas = prepareFontCanvas(fontInformation)) {
      var fontInfo = prepareFontInfo(fontInformation, fontCanvas);

      File fontImageFile = new File(parentDirectory, fontInformation.getFontFileName());
      File fontInfoFile = new File(parentDirectory, fontInformation.getFontInfoFileName());

      try {
        ImageIO.write(fontCanvas.getBufferedImage(), "png", fontImageFile);
        Files.write(fontInfoFile.toPath(), serializeFontInfo(fontInfo));
      } catch (IOException ex) {
        ex.printStackTrace();
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
