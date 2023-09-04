package technology.sola.engine.graphics.renderer.blend;

import technology.sola.engine.graphics.Color;

public class LinearDodgeBlendFunction implements BlendFunction {
  @Override
  public void set(int[] pixels, int pixelIndex, Color color) {
    Color currentColor = new Color(pixels[pixelIndex]);

    pixels[pixelIndex] = new Color(
      Math.min(255, currentColor.getRed() + color.getRed()),
      Math.min(255, currentColor.getGreen() + color.getGreen()),
      Math.min(255, currentColor.getBlue() + color.getBlue())
    ).hexInt();
  }
}
