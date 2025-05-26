package technology.sola.engine.graphics.renderer.blend;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.graphics.Color;

import java.util.Random;

/**
 * Random chance of using color of top pixel based on alpha as the probability. An alpha of 127 would be a 50% chance
 * of using the top pixel.
 */
@NullMarked
public class DissolveBlendFunction implements BlendFunction {
  private final Random random = new Random();

  @Override
  public void set(int[] pixels, int pixelIndex, Color color) {
    if (random.nextInt(0, 255) < color.getAlpha()) {
      pixels[pixelIndex] = color.hexInt();
    }
  }
}
