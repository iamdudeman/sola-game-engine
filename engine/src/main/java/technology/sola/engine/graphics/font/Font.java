package technology.sola.engine.graphics.font;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.SolaImage;

import java.util.HashMap;
import java.util.Map;

public class Font {
  private final FontInfo fontInfo;
  private final Map<Color, Map<Character, SolaImage>> colorToGlyphsMap = new HashMap<>();
  private final Map<Character, SolaImage> blackCharacterToGlyphMap = new HashMap<>();
  private Map<Character, SolaImage> cachedCharacterToGlyphMap;
  private Color cachedColor;

  public Font(SolaImage fontImage, FontInfo fontInfo) {
    this.fontInfo = fontInfo;

    fontInfo.getGlyphs().forEach(fontGlyph -> {
      SolaImage glyphImage = fontImage.getSubImage(
        fontGlyph.getX(), fontGlyph.getY(),
        fontGlyph.getWidth(), fontGlyph.getHeight()
      );

      this.blackCharacterToGlyphMap.put(fontGlyph.getGlyph(), glyphImage);
    });

    this.colorToGlyphsMap.put(Color.BLACK, this.blackCharacterToGlyphMap);
    this.cachedCharacterToGlyphMap = this.blackCharacterToGlyphMap;
    this.cachedColor = Color.BLACK;
  }

  public SolaImage getGlyph(char character, Color color) {
    Map<Character, SolaImage> mapToUse = cachedCharacterToGlyphMap;

    if (color.equals(Color.BLACK)) {
      mapToUse = blackCharacterToGlyphMap;
    } else if (!color.equals(cachedColor)) {
      cachedColor = color;
      cachedCharacterToGlyphMap = colorToGlyphsMap.computeIfAbsent(color, key -> new HashMap<>());
      mapToUse = cachedCharacterToGlyphMap;
    }

    return mapToUse.computeIfAbsent(character, key -> getGlyphAsColor(key, color));
  }

  public FontInfo getFontInfo() {
    return fontInfo;
  }

  private SolaImage getGlyphAsColor(char character, Color color) {
    SolaImage blackGlyph = getGlyph(character, Color.BLACK);

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
}
