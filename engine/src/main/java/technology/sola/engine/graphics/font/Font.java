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

    fontInfo.getFontGlyphList().forEach(fontGlyph -> {
      SolaImage glyphImage = fontImage.getSubImage(
        fontGlyph.getX(), fontGlyph.getY() - fontGlyph.getHeight(),
        fontGlyph.getWidth(), fontGlyph.getHeight()
      );

      font.characterToGlyphMap.put(fontGlyph.getGlyph(), glyphImage);
    });

    return font;
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
