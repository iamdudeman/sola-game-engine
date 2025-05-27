package technology.sola.engine.graphics.renderer.blend;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.graphics.Color;

/**
 * Blends top onto bottom based on alpha of top pixel.
 */
@NullMarked
public class NormalBlendFunction implements BlendFunction {
  @Override
  public void set(int[] pixels, int pixelIndex, Color color) {
    if (color.hasAlpha()) {
      Color currentColor = new Color(pixels[pixelIndex]);
      float alphaMod = color.getAlpha() * Color.ONE_DIV_255;
      float oneMinusAlpha = 1 - alphaMod;

      float red = currentColor.getRed() * oneMinusAlpha + color.getRed() * alphaMod;
      float green = currentColor.getGreen() * oneMinusAlpha + color.getGreen() * alphaMod;
      float blue = currentColor.getBlue() * oneMinusAlpha + color.getBlue() * alphaMod;

      pixels[pixelIndex] = new Color((int) red, (int) green, (int) blue).hexInt();
    } else {
      pixels[pixelIndex] = color.hexInt();
    }
  }
}
