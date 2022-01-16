package technology.sola.engine.graphics.font;

// TODO convert to record if we don't use GSON for deserializing
public class FontGlyph {
  private final char glyph;
  private final int x;
  private final int y;
  private final int width;
  private final int height;

  public FontGlyph(char glyph, int x, int y, int width, int height) {
    this.glyph = glyph;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public char getGlyph() {
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
