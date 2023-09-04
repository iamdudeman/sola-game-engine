package technology.sola.engine.graphics.renderer.pixel;

import technology.sola.engine.graphics.Color;

public class MaskPixelUpdater implements PixelUpdater {
  @Override
  public void set(int[] pixels, int pixelIndex, Color color) {
    if (color.getAlpha() == 255) {
      pixels[pixelIndex] = color.hexInt();
    }
  }
}
