package technology.sola.engine.tools.font;

public class FontGlyphModel {
  private String glyph;
  private int x;
  private int y;
  private int width;
  private int height;

  public FontGlyphModel(String glyph, int width, int height) {
    this.glyph = glyph;
    this.width = width;
    this.height = height;
  }

  public String getGlyph() {
    return glyph;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getX() {
    return x;
  }

  // TODO using this is hacky
  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  // TODO using this is hacky
  public void setY(int y) {
    this.y = y;
  }
}
