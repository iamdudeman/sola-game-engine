package technology.sola.engine.graphics.font;

import java.util.List;

public class FontInfo {
  private String fontGlyphFile;
  private String fontName;
  private FontStyle fontStyle;
  private int fontSize;
  private int leading;
  private List<FontGlyph> glyphs;

  public FontGlyph getFontGlyph(char character) {
    return glyphs.stream().filter(fontGlyph -> fontGlyph.getGlyph() == character).findFirst().orElse(null);
  }

  public String getFontGlyphFile() {
    return fontGlyphFile;
  }

  public String getFontName() {
    return fontName;
  }

  public FontStyle getFontStyle() {
    return fontStyle;
  }

  public int getFontSize() {
    return fontSize;
  }

  public int getLeading() {
    return leading;
  }

  public List<FontGlyph> getGlyphs() {
    return glyphs;
  }
}
