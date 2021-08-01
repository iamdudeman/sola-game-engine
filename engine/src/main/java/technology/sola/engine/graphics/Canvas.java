package technology.sola.engine.graphics;

public class Canvas {
  protected int width;
  protected int height;
  protected int[] pixels;

  Canvas(int width, int height) {
    this.width = width;
    this.height = height;
    this.pixels = new int[width * height];
  }

  Canvas(int width, int height, int[] pixels) {
    this.width = width;
    this.height = height;
    this.pixels = pixels;
  }

  public int getPixel(int x, int y) {
    if (x >= 0 && x < width && y >= 0 && y < height) {
      return pixels[x + y * width];
    } else {
      return Color.BLANK.hexInt();
    }
  }

  public int getPixel(float x, float y) {
    return getPixel((int)(x + 0.5f), (int)(y + 0.5f));
  }

  public int[] getPixels() {
    return pixels;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
}
