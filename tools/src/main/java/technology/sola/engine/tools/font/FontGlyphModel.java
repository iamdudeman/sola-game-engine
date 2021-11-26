package technology.sola.engine.tools.font;

class FontGlyphModel {
  private final String glyph;
  private final int width;
  private final int height;
  private int x = -1;
  private int y = -1;

  public FontGlyphModel(String glyph, int width, int height) {
    this.glyph = glyph;
    this.width = width;
    this.height = height;
  }

  public FontGlyphModel(FontGlyphModel fontGlyphModel, int x, int y) {
    this.glyph = fontGlyphModel.glyph;
    this.width = fontGlyphModel.width;
    this.height = fontGlyphModel.height;
    this.x = x;
    this.y = y;
  }

  public String getGlyph() {
    return glyph;
  }

  public int getWidth() {
    return width;
  }
}
