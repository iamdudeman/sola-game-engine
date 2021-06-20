package technology.sola.engine.tools.font;

import java.util.List;

public class FontModel {
  private String file;
  private final String font;
  private final String fontStyle;
  private final int fontSize;
  private final int maxAscent;
  private final int leading;
  private final List<FontGlyphModel> glyphs;

  public FontModel(FontInformation fontInformation, List<FontGlyphModel> glyphs) {
    this.file = fontInformation.getFontFileName();
    this.font = fontInformation.getFontName();
    this.fontStyle = fontInformation.getFontStyle();
    this.fontSize = fontInformation.getFontSize();
    this.maxAscent = fontInformation.getMaxAscent();
    this.glyphs = glyphs;
    this.leading = fontInformation.getLeading();
  }
}
