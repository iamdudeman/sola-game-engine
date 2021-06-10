package technology.sola.engine.graphics.font;

import java.util.List;

public class FontInfo {
  private final String name;
  private final int fontSize;
  private final FontStyle fontStyle;
  private final List<FontGlyph> fontGlyphList;

  public FontInfo(String name, int fontSize, FontStyle fontStyle, List<FontGlyph> fontGlyphList) {
    this.name = name;
    this.fontSize = fontSize;
    this.fontStyle = fontStyle;
    this.fontGlyphList = fontGlyphList;
  }

  public FontGlyph getFontGlyph(Character character) {
    return fontGlyphList.stream().filter(fontGlyph -> fontGlyph.getGlyph().equals(character)).findFirst().orElse(null);
  }

  public String getName() {
    return name;
  }

  public int getFontSize() {
    return fontSize;
  }

  public FontStyle getFontStyle() {
    return fontStyle;
  }

  public List<FontGlyph> getFontGlyphList() {
    return fontGlyphList;
  }
}
