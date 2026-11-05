package technology.sola.engine.editor.tools.font.utils;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.graphics.font.FontGlyph;
import technology.sola.engine.assets.graphics.font.FontInfo;
import technology.sola.engine.assets.graphics.font.FontStyle;
import technology.sola.engine.assets.graphics.font.mapper.FontInfoJsonMapper;
import technology.sola.json.SolaJson;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FontRasterizerTool generates a font json and font png file that can be used by the sola game engine.
 */
@NullMarked
public class FontToolUtils {
  /**
   * Default characters used in the font rasterizer tool.
   */
  public static final String DEFAULT_CHARACTERS = "abcdefghijklmnopqrstuvwxyz{|}~ !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`";

  /**
   * @return a Set of available font families
   */
  public static java.util.Set<String> getLocalFontFamilies() {
    java.util.Set<String> fontFamilies = new HashSet<>();
    GraphicsEnvironment ge = GraphicsEnvironment
      .getLocalGraphicsEnvironment();

    Font[] allFonts = ge.getAllFonts();

    for (Font font : allFonts) {
      fontFamilies.add(font.getFamily());
    }

    return fontFamilies.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
  }

  /**
   * Generates a font info and font image file for the desired font family with a font style and size for desired
   * characters.
   * See {@link FontToolUtils#getLocalFontFamilies} to view what font families are available.
   *
   * @param parentDirectory the directory to output the generated files to
   * @param fontFamily      the font family to generate asset for
   * @param fontStyle       the font style [NORMAL, ITALIC, BOLD, BOLD_ITALIC]
   * @param fontSize        the font size
   * @param characters      the characters to rasterize
   */
  public static void rasterizeFont(File parentDirectory, String fontFamily, String fontStyle, int fontSize, String characters) {
    var fontInformation = new FontInformation(fontFamily, FontStyle.valueOf(fontStyle), fontSize);

    try (var fontCanvas = prepareFontCanvas(fontInformation, characters)) {
      var fontInfo = prepareFontInfo(fontInformation, fontCanvas, characters);

      File fontImageFile = new File(parentDirectory, fontInformation.getFontFileName());
      File fontInfoFile = new File(parentDirectory, fontInformation.getFontInfoFileName());

      try {
        ImageIO.write(fontCanvas.getBufferedImage(), "png", fontImageFile);
        Files.write(fontInfoFile.toPath(), serializeFontInfo(fontInfo));
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  private static FontCanvas prepareFontCanvas(FontInformation fontInformation, String characters) {
    Rectangle2D fullBounds = fontInformation.getStringBounds(characters);
    int imageWidth = (int) fullBounds.getWidth() / 2;
    int imageHeight = (int) fullBounds.getHeight() * 5;

    return new FontCanvas(fontInformation, imageWidth, imageHeight);
  }

  private static FontInfo prepareFontInfo(FontInformation fontInformation, FontCanvas fontCanvas, String characters) {
    List<FontGlyph> fontGlyphsWithPositions = fontCanvas.drawFontGlyphs(characters);

    return new FontInfo(
      fontInformation.getFontFileName(), fontInformation.getFontFamily(),
      FontStyle.valueOf(fontInformation.getFontStyle()), fontInformation.getFontSize(),
      fontInformation.getLeading(), fontGlyphsWithPositions
    );
  }

  private static byte[] serializeFontInfo(FontInfo fontInfo) {
    return new SolaJson().stringify(fontInfo, new FontInfoJsonMapper()).getBytes(StandardCharsets.UTF_8);
  }

  private FontToolUtils() {
  }
}
