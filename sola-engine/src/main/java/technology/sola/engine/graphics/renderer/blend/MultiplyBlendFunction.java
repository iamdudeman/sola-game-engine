package technology.sola.engine.graphics.renderer.blend;

import technology.sola.engine.graphics.Color;
import technology.sola.math.SolaMath;

/**
 * Takes the RGB channel values from 0 to 1 of each pixel in the top layer and multiples them with the values for the
 * corresponding pixel from the bottom layer.
 */
public class MultiplyBlendFunction implements BlendFunction {
  @Override
  public void set(int[] pixels, int pixelIndex, Color color) {
    Color currentColor = new Color(pixels[pixelIndex]);

    pixels[pixelIndex] = new Color(
      currentColor.getAlpha(),
      SolaMath.fastRound(currentColor.getRed() * (color.getRed() * Color.ONE_DIV_255)),
      SolaMath.fastRound(currentColor.getGreen() * (color.getGreen() * Color.ONE_DIV_255)),
      SolaMath.fastRound(currentColor.getBlue() * (color.getBlue() * Color.ONE_DIV_255))
    ).hexInt();
  }
}
