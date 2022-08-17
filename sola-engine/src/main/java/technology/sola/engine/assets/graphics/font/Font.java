package technology.sola.engine.assets.graphics.font;

import technology.sola.engine.assets.Asset;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.exception.MissingGlyphException;
import technology.sola.engine.graphics.Color;

import java.util.HashMap;
import java.util.Map;

public class Font implements Asset {
  private final FontInfo fontInfo;
  private final Map<Color, Map<Character, SolaImage>> colorToGlyphsMap = new HashMap<>();
  private final Map<Character, SolaImage> blackCharacterToGlyphMap = new HashMap<>();
  private Map<Character, SolaImage> cachedCharacterToGlyphMap;
  private Color cachedColor;

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

  public SolaImage getGlyph(char character, Color color) {
    if (color.equals(Color.BLACK)) {
      return getBaseGlyph(character);
    }

    if (!color.equals(cachedColor)) {
      cachedColor = color;
      cachedCharacterToGlyphMap = colorToGlyphsMap.computeIfAbsent(color, key -> new HashMap<>());
    }

    return cachedCharacterToGlyphMap.computeIfAbsent(character, key -> getGlyphAsColor(key, color));
  }

  public TextDimensions getDimensionsForText(String text) {
    int width = 0;
    int height = 0;

    for (char c : text.toCharArray()) {
      SolaImage glyphImage = getBaseGlyph(c);

      width += glyphImage.getWidth() + getFontInfo().leading();

      if (glyphImage.getHeight() > height) {
        height = glyphImage.getHeight();
      }
    }

    width -= getFontInfo().leading();

    return new TextDimensions(width, height);
  }

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

  public record TextDimensions(int width, int height) {
  }
}
