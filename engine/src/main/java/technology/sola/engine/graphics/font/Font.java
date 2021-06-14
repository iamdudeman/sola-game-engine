package technology.sola.engine.graphics.font;

import technology.sola.engine.graphics.SolaImage;

import java.util.HashMap;
import java.util.Map;

// TODO implement font loading for other platforms than swing
// TODO Font should probably keep a cached copy of the colored glyph image for performance / memory

public class Font {
  private FontInfo fontInfo;
  private Map<Character, SolaImage> characterToGlyphMap = new HashMap<>();

  public static Font createFont(SolaImage fontImage, FontInfo fontInfo) {
    Font font = new Font();

    font.fontInfo = fontInfo;

    fontInfo.getGlyphs().forEach(fontGlyph -> {
      SolaImage glyphImage = fontImage.getSubImage(
        fontGlyph.getX(), fontGlyph.getY(),
        fontGlyph.getWidth(), fontGlyph.getHeight()
      );

      font.characterToGlyphMap.put(fontGlyph.getGlyph(), glyphImage);
    });

    return font;
  }

  public SolaImage getGlyph(char character) {
    return characterToGlyphMap.get(character);
  }

  public FontInfo getFontInfo() {
    return fontInfo;
  }

  public Map<Character, SolaImage> getCharacterToGlyphMap() {
    return characterToGlyphMap;
  }

  private Font() {
  }
}
