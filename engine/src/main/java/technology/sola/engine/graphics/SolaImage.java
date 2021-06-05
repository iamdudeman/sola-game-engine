package technology.sola.engine.graphics;

import java.util.Arrays;

public class SolaImage {
  private int width;
  private int height;
  private int[] pixels;

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

  public void updateContent(int width, int height, int[] pixels) {
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

  public SolaImage getSubImage(int x, int y, int width, int height) {
    int[] subPixels = new int[width * height];
    int startingIndex = x + y * this.width;
    int parentHorizontalOffset = 0;
    int parentVerticalOffset = 0;

    for (int i = 0; i < subPixels.length; i++) {
      subPixels[i] = this.pixels[startingIndex + parentHorizontalOffset + parentVerticalOffset];

      parentHorizontalOffset++;

      if (parentHorizontalOffset >= width) {
        parentHorizontalOffset = 0;
        parentVerticalOffset += this.width;
      }
    }

    return new SolaImage(width, height, subPixels);
  }
}
