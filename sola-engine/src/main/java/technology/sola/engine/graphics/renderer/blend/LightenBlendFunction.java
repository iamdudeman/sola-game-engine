package technology.sola.engine.graphics.renderer.blend;

import technology.sola.engine.graphics.Color;

public class LightenBlendFunction implements BlendFunction {
  @Override
  public void set(int[] pixels, int pixelIndex, Color color) {
    Color currentColor = new Color(pixels[pixelIndex]);

    pixels[pixelIndex] = new Color(
      Math.max(currentColor.getAlpha(), color.getAlpha()),
      Math.max(currentColor.getRed(), color.getRed()),
      Math.max(currentColor.getGreen(), color.getGreen()),
      Math.max(currentColor.getBlue(), color.getBlue())
    ).hexInt();
  }
}