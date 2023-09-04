package technology.sola.engine.graphics.renderer.pixel;

import technology.sola.engine.graphics.Color;

import java.util.Random;

public class DissolvePixelUpdater implements PixelUpdater {
  private final Random random = new Random();

  @Override
  public void set(int[] pixels, int pixelIndex, Color color) {
    if (random.nextInt(0, 255) < color.getAlpha()) {
      pixels[pixelIndex] = color.hexInt();
    }
  }
}
