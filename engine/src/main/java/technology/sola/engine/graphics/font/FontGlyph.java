package technology.sola.engine.graphics.font;

public class FontGlyph {
  private final Character glyph;
  private final int x;
  private final int y;
  private final int width;
  private final int height;

  public FontGlyph(Character glyph, int x, int y, int width, int height) {
    this.glyph = glyph;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public Character getGlyph() {
    return glyph;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
}
