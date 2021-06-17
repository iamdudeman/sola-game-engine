package technology.sola.engine.tools.font;

import java.util.List;

public class FontModel {
  private String file;
  private String font;
  private String fontStyle;
  private int fontSize;
  private int maxAscent;
  private int leading;
  private List<FontGlyphModel> glyphs;

  public FontModel(String font, String fontStyle, int fontSize, int maxAscent, int leading, List<FontGlyphModel> glyphs) {
    this.font = font;
    this.fontStyle = fontStyle;
    this.fontSize = fontSize;
    this.maxAscent = maxAscent;
    this.leading = leading;
    this.glyphs = glyphs;
    // TODO clean this up
    this.file = font + "_" + fontStyle + "_" + fontSize + ".png";
  }

  public String getFile() {
    return file;
  }

  public String getFont() {
    return font;
  }

  public String getFontStyle() {
    return fontStyle;
  }

  public int getFontSize() {
    return fontSize;
  }

  public int getMaxAscent() {
    return maxAscent;
  }

  public int getLeading() {
    return leading;
  }

  public List<FontGlyphModel> getGlyphs() {
    return glyphs;
  }
}
