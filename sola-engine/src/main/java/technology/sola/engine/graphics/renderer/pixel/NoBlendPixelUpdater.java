package technology.sola.engine.graphics.renderer.pixel;

import technology.sola.engine.graphics.Color;

public class NoBlendPixelUpdater implements PixelUpdater {
  @Override
  public void set(int[] pixels, int pixelIndex, Color color) {
    pixels[pixelIndex] = color.hexInt();
  }
}
