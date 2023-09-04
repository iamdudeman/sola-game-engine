package technology.sola.engine.graphics.renderer.blend;

import technology.sola.engine.graphics.Color;

@FunctionalInterface
public interface BlendFunction {
  void set(int[] pixels, int pixelIndex, Color color);
}
