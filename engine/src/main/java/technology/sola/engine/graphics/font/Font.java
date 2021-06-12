package technology.sola.engine.graphics.font;

import technology.sola.engine.graphics.SolaImage;

import java.util.HashMap;
import java.util.Map;

public class Font {
  private FontInfo fontInfo;
  private Map<Character, SolaImage> characterToGlyphMap = new HashMap<>();

  public static Font createFont(SolaImage fontImage, FontInfo fontInfo) {
    Font font = new Font();

    font.fontInfo = fontInfo;

    fontInfo.getGlyphs().forEach(fontGlyph -> {
      System.out.printf("%s: %s, %s, %s, %s%n", fontGlyph.getGlyph(), fontGlyph.getX(), fontGlyph.getY(), fontGlyph.getWidth(), fontGlyph.getHeight());
      SolaImage glyphImage = fontImage.getSubImage(
        fontGlyph.getX(), fontGlyph.getY(),
        fontGlyph.getWidth(), fontGlyph.getHeight()
      );

      font.characterToGlyphMap.put(fontGlyph.getGlyph(), glyphImage);
    });

    return font;
  }

  public SolaImage getGlyph(char character) {
    return characterToGlyphMap.get(Character.valueOf(character));
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
