package technology.sola.engine.graphics.renderer;

/**
 * BlendMode controls how the pixel being drawn (top) and the destination pixel (bottom will be blended together to
 * produce a final color that is drawn.
 */
public enum BlendMode {
  /**
   * No blending happens. Top pixel is used.
   */
  NO_BLENDING,

  /**
   * Transparent if {@code alpha < 255} (bottom pixel used).
   */
  MASK,

  /**
   * Blends top onto bottom based on alpha of top pixel.
   */
  NORMAL,

  /**
   * Random chance of using color of top pixel based on alpha as the probability. An alpha of 127 would be a 50% chance
   * of using the top pixel.
   */
  DISSOLVE,

  /**
   * Adds the top and bottom pixels together ignoring alpha.
   */
  LINEAR_DODGE,

  /**
   * Takes the RGB channel values from 0 to 1 of each pixel in the top layer and multiples them with the values for the
   * corresponding pixel from the bottom layer.
   */
  MULTIPLY,

  /**
   * Takes the max value of each channel for the destination pixel; alpha, red, green, blue.
   */
  LIGHTEN,
}
