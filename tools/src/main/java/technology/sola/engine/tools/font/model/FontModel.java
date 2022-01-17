package technology.sola.engine.tools.font.model;

import java.util.List;

public class FontModel {
  private String fontGlyphFile;
  private final String fontName;
  private final String fontStyle;
  private final int fontSize;
  private final int leading;
  private final List<FontGlyphModel> glyphs;

  public FontModel(String fontGlyphFile, String fontName, String fontStyle, int fontSize, int leading, List<FontGlyphModel> glyphs) {
    this.fontGlyphFile = fontGlyphFile;
    this.fontName = fontName;
    this.fontStyle = fontStyle;
    this.fontSize = fontSize;
    this.leading = leading;
    this.glyphs = glyphs;
  }
}
