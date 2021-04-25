package technology.sola.engine.graphics;

import java.util.Arrays;

public class SolaImage {
  private final int width;
  private final int height;
  private final int[] pixels;

  public SolaImage(int width, int height) {
    this.width = width;
    this.height = height;
    this.pixels = new int[width * height];

    Arrays.fill(pixels, Color.BLANK.hexInt());
  }

  public SolaImage(int width, int height, int[] pixels) {
    this.width = width;
    this.height = height;
    this.pixels = pixels;
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

  public void setPixel(int x, int y, int argb) {
    this.pixels[x + y * width] = argb;
  }
}
