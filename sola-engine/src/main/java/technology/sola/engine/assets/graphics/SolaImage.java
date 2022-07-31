package technology.sola.engine.assets.graphics;

import technology.sola.engine.assets.Asset;
import technology.sola.engine.graphics.Canvas;

public class SolaImage extends Canvas implements Asset {
  public SolaImage(int width, int height) {
    super(width, height);
  }

  public SolaImage(int width, int height, int[] pixels) {
    super(width, height, pixels);
  }

  public void setPixels(int width, int height, int[] pixels) {
    this.width = width;
    this.height = height;
    this.pixels = pixels;
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
