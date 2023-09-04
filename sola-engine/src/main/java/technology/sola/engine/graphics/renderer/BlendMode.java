package technology.sola.engine.graphics.renderer;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.blend.BlendFunction;
import technology.sola.engine.graphics.renderer.blend.DissolveBlendFunction;
import technology.sola.engine.graphics.renderer.blend.LightenBlendFunction;
import technology.sola.engine.graphics.renderer.blend.LinearDodgeBlendFunction;
import technology.sola.engine.graphics.renderer.blend.MaskBlendFunction;
import technology.sola.engine.graphics.renderer.blend.MultiplyBlendFunction;
import technology.sola.engine.graphics.renderer.blend.NoBlendFunction;
import technology.sola.engine.graphics.renderer.blend.NormalBlendFunction;

/**
 * BlendMode is an enum of several prebuilt {@link BlendFunction}s.
 */
public enum BlendMode implements BlendFunction {
  /**
   * No blending happens. Top pixel is used.
   */
  NO_BLENDING(new NoBlendFunction()),

  /**
   * Transparent if {@code alpha < 255} (bottom pixel used).
   */
  MASK(new MaskBlendFunction()),

  /**
   * Blends top onto bottom based on alpha of top pixel.
   */
  NORMAL(new NormalBlendFunction()),

  /**
   * Random chance of using color of top pixel based on alpha as the probability. An alpha of 127 would be a 50% chance
   * of using the top pixel.
   */
  DISSOLVE(new DissolveBlendFunction()),

  /**
   * Adds the top and bottom pixels together ignoring alpha.
   */
  LINEAR_DODGE(new LinearDodgeBlendFunction()),

  /**
   * Takes the RGB channel values from 0 to 1 of each pixel in the top layer and multiples them with the values for the
   * corresponding pixel from the bottom layer.
   */
  MULTIPLY(new MultiplyBlendFunction()),

  /**
   * Takes the max value of each channel for the destination pixel; alpha, red, green, blue.
   */
  LIGHTEN(new LightenBlendFunction());

  private final BlendFunction blendFunction;

  BlendMode(BlendFunction blendFunction) {
    this.blendFunction = blendFunction;
  }

  @Override
  public void set(int[] pixels, int pixelIndex, Color color) {
    blendFunction.set(pixels, pixelIndex, color);
  }
}
