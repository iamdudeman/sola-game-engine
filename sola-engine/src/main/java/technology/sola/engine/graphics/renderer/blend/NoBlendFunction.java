package technology.sola.engine.graphics.renderer.blend;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.graphics.Color;

/**
 * No blending happens. Top pixel is used.
 */
@NullMarked
public class NoBlendFunction implements BlendFunction {
  @Override
  public void set(int[] pixels, int pixelIndex, Color color) {
    pixels[pixelIndex] = color.hexInt();
  }
}
