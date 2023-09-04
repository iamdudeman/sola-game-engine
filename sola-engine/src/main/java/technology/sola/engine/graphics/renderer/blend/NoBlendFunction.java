package technology.sola.engine.graphics.renderer.blend;

import technology.sola.engine.graphics.Color;

/**
 * No blending happens. Top pixel is used.
 */
public class NoBlendFunction implements BlendFunction {
  @Override
  public void set(int[] pixels, int pixelIndex, Color color) {
    pixels[pixelIndex] = color.hexInt();
  }
}
