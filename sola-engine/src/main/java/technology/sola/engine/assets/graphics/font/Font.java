package technology.sola.engine.assets.graphics.font;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.Asset;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.exception.MissingGlyphException;
import technology.sola.engine.graphics.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Font is an {@link Asset} containing the information required to render strings of text for a particular font that
 * has been rasterized into an image.
 */
@NullMarked
public class Font implements Asset {
  private static final SolaImage INVISIBLE_GLYPH = new SolaImage(0, 0);
  private final FontInfo fontInfo;
  private final Map<Color, Map<Character, SolaImage>> colorToGlyphsMap = new HashMap<>();
  private final Map<Character, SolaImage> blackCharacterToGlyphMap = new HashMap<>();
  private Map<Character, SolaImage> cachedCharacterToGlyphMap;
  private Color cachedColor;

  /**
   * Creates a Font instance from a {@link SolaImage} with {@link FontInfo}.
   *
   * @param fontImage the image for the rasterized font
   * @param fontInfo  the information regarding the rasterized font
   */
  public Font(SolaImage fontImage, FontInfo fontInfo) {
    this.fontInfo = fontInfo;

    fontInfo.glyphs().forEach(fontGlyph -> {
      SolaImage glyphImage = fontImage.getSubImage(
        fontGlyph.x(), fontGlyph.y(),
        fontGlyph.width(), fontGlyph.height()
      );

      this.blackCharacterToGlyphMap.put(fontGlyph.glyph(), glyphImage);
    });

    this.colorToGlyphsMap.put(Color.BLACK, this.blackCharacterToGlyphMap);
    this.cachedCharacterToGlyphMap = this.blackCharacterToGlyphMap;
    this.cachedColor = Color.BLACK;
  }

  /**
   * Gets a {@link SolaImage} for a glyph to render in desired {@link Color}.
   *
   * @param character the character to render
   * @param color     the color to render the character
   * @return the image for the character's glyph in desired color
   */
  public SolaImage getGlyph(char character, Color color) {
    // check for invisible characters which should be 0x0
    if (character == '\n') {
      return INVISIBLE_GLYPH;
    }

    if (color.equals(Color.BLACK)) {
      return getBaseGlyph(character);
    }

    if (!color.equals(cachedColor)) {
      cachedColor = color;
      cachedCharacterToGlyphMap = colorToGlyphsMap.computeIfAbsent(color, key -> new HashMap<>());
    }

    return cachedCharacterToGlyphMap.computeIfAbsent(character, key -> getGlyphAsColor(key, color));
  }

  /**
   * Checks to see if this Font has a glyph registered for rendering.
   *
   * @param character the character to check for a glyph
   * @return true if the Font has a glyph for the character
   */
  public boolean hasGlyph(char character) {
    // check for invisible characters
    if (character == '\n') {
      return true;
    }

    return blackCharacterToGlyphMap.containsKey(character);
  }

  /**
   * Calculates and returns the {@link TextDimensions} for a string of text that would be rendered with this font.
   *
   * @param text the string of text
   * @return the text dimensions
   */
  public TextDimensions getDimensionsForText(String text) {
    int width = 0;
    int height = 0;

    for (char c : text.toCharArray()) {
      SolaImage glyphImage = getBaseGlyph(c);

      width += glyphImage.getWidth();

      if (glyphImage.getHeight() > height) {
        height = glyphImage.getHeight();
      }
    }

    return new TextDimensions(width, height);
  }

  /**
   * @return the {@link FontInfo}
   */
  public FontInfo getFontInfo() {
    return fontInfo;
  }

  private SolaImage getGlyphAsColor(char character, Color color) {
    SolaImage blackGlyph = getBaseGlyph(character);

    int newGlyphColor = color.hexInt();
    int[] originalPixels = blackGlyph.getPixels();
    int[] coloredGlyphPixels = new int[originalPixels.length];

    for (int i = 0; i < originalPixels.length; i++) {
      int pixel = originalPixels[i];

      if (pixel == Color.BLACK.hexInt()) {
        pixel = newGlyphColor;
      }

      coloredGlyphPixels[i] = pixel;
    }

    return new SolaImage(blackGlyph.getWidth(), blackGlyph.getHeight(), coloredGlyphPixels);
  }

  private SolaImage getBaseGlyph(char character) {
    SolaImage glyph = blackCharacterToGlyphMap.get(character);

    if (glyph == null) {
      throw new MissingGlyphException(character, fontInfo);
    }

    return glyph;
  }

  /**
   * Represents the width and height that a string of text that would be rendered with a particular {@link Font}.
   *
   * @param width  the width of the string of text
   * @param height the height of the string of text
   */
  public record TextDimensions(int width, int height) {
  }
}
