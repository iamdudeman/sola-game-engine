package technology.sola.engine.graphics.renderer.blend;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.graphics.Color;

/**
 * Adds the top and bottom pixels together ignoring alpha.
 */
@NullMarked
public class LinearDodgeBlendFunction implements BlendFunction {
  @Override
  public void set(int[] pixels, int pixelIndex, Color color) {
    Color currentColor = Color.of(pixels[pixelIndex]);

    pixels[pixelIndex] = new Color(
      Math.min(255, currentColor.getRed() + color.getRed()),
      Math.min(255, currentColor.getGreen() + color.getGreen()),
      Math.min(255, currentColor.getBlue() + color.getBlue())
    ).hexInt();
  }
}
