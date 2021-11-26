package technology.sola.engine.tools.font;

import java.util.List;

class FontModel {
  private String fontGlyphFile;
  private final String fontName;
  private final String fontStyle;
  private final int fontSize;
  private final int leading;
  private final List<FontGlyphModel> glyphs;

  public FontModel(FontInformation fontInformation, List<FontGlyphModel> glyphs) {
    this.fontGlyphFile = fontInformation.getFontFileName();
    this.fontName = fontInformation.getFontName();
    this.fontStyle = fontInformation.getFontStyle();
    this.fontSize = fontInformation.getFontSize();
    this.glyphs = glyphs;
    this.leading = fontInformation.getLeading();
  }
}
