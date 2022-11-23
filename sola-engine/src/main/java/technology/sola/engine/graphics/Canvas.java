package technology.sola.engine.graphics;

/**
 * Canvas is an array of pixels with a width and height defined.
 */
public class Canvas {
  protected int width;
  protected int height;
  protected int[] pixels;

  /**
   * Creates a Canvas instance with an empty array of pixels.
   *
   * @param width  the width of the Canvas
   * @param height the height of the canvas
   */
  public Canvas(int width, int height) {
    this.width = width;
    this.height = height;
    this.pixels = new int[width * height];
  }

  /**
   * Creates a canvas instance with desired array of pixels.
   *
   * @param width  the width of the Canvas
   * @param height the height of the canvas
   * @param pixels the array of pixels to use for this canvas
   */
  public Canvas(int width, int height, int[] pixels) {
    this.width = width;
    this.height = height;
    this.pixels = pixels;
  }

  /**
   * Returns the hex value of the pixel at desired coordinate on the canvas.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @return the hex value of the pixel
   */
  public int getPixel(int x, int y) {
    if (x >= 0 && x < width && y >= 0 && y < height) {
      return pixels[x + y * width];
    } else {
      return Color.BLANK.hexInt();
    }
  }

  /**
   * Returns the hex value of the pixel at desired coordinate on the canvas. Since pixels coordinates are integers this
   * rounds the float values half up.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @return the hex value of the pixel
   */
  public int getPixel(float x, float y) {
    return getPixel((int) (x + 0.5f), (int) (y + 0.5f));
  }

  /**
   * @return the array of pixels in the canvas
   */
  public int[] getPixels() {
    return pixels;
  }

  /**
   * @return the width of the canvas
   */
  public int getWidth() {
    return width;
  }

  /**
   * @return the height of the canvas
   */
  public int getHeight() {
    return height;
  }
}
