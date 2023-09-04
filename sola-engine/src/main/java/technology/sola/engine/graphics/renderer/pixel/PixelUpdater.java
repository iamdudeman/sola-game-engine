package technology.sola.engine.graphics.renderer.pixel;

import technology.sola.engine.graphics.Color;

@FunctionalInterface
public interface PixelUpdater {
  void set(int[] pixels, int pixelIndex, Color color);
}
