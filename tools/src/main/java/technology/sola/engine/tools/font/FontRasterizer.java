package technology.sola.engine.tools.font;

import com.google.gson.Gson;
import technology.sola.engine.tools.font.model.FontGlyphModel;
import technology.sola.engine.tools.font.model.FontModel;
import technology.sola.engine.tools.font.model.FontStyle;

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
      var fontModel = prepareFontModel(fontInformation, fontCanvas);

      File fontImageFile = new File(parentDirectory, fontInformation.getFontFileName());
      File fontInfoFile = new File(parentDirectory, fontInformation.getFontInfoFileName());

      try {
        ImageIO.write(fontCanvas.getBufferedImage(), "png", fontImageFile);
        Files.write(fontInfoFile.toPath(), serializeFontModel(fontModel));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private FontCanvas prepareFontCanvas(FontInformation fontInformation) {
    Rectangle2D fullBounds = fontInformation.getStringBounds(characters);
    int imageWidth = (int)fullBounds.getWidth() / 2;
    int imageHeight = (int)fullBounds.getHeight() * 5;

    return new FontCanvas(fontInformation, imageWidth, imageHeight);
  }

  private FontModel prepareFontModel(FontInformation fontInformation, FontCanvas fontCanvas) {
    List<FontGlyphModel> fontGlyphModelsWithPositions = fontCanvas.drawFontGlyphs(characters);

    return new FontModel(
      fontInformation.getFontFileName(), fontInformation.getFontName(),
      fontInformation.getFontStyle(), fontInformation.getFontSize(),
      fontInformation.getLeading(), fontGlyphModelsWithPositions
    );
  }

  private byte[] serializeFontModel(FontModel fontModel) {
    return new Gson().toJson(fontModel).getBytes(StandardCharsets.UTF_8);
  }
}
