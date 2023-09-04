package technology.sola.engine.graphics.renderer.pixel;

import technology.sola.engine.graphics.Color;
import technology.sola.math.SolaMath;

public class MultiplyPixelUpdater implements PixelUpdater {
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
