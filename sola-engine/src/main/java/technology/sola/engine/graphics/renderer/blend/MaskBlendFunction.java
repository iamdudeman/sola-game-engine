package technology.sola.engine.graphics.renderer.blend;

import technology.sola.engine.graphics.Color;

/**
 * Transparent if {@code alpha < 255} (bottom pixel used).
 */
public class MaskBlendFunction implements BlendFunction {
  @Override
  public void set(int[] pixels, int pixelIndex, Color color) {
    if (color.getAlpha() == 255) {
      pixels[pixelIndex] = color.hexInt();
    }
  }
}
