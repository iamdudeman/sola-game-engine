package technology.sola.engine.graphics.renderer.blend;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.graphics.Color;

/**
 * BlendFunction controls how the pixel being drawn (top) and the destination pixel (bottom) will be blended together to
 * produce a final color that is drawn.
 */
@NullMarked
@FunctionalInterface
public interface BlendFunction {
  /**
   * Method called to set the color of the pixel at pixelIndex.
   *
   * @param pixels     the array of pixels
   * @param pixelIndex the index of the destination pixel to modify
   * @param color      the {@link Color} of the top pixel being applied
   */
  void set(int[] pixels, int pixelIndex, Color color);
}
