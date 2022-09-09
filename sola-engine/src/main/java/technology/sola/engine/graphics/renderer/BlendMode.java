package technology.sola.engine.graphics.renderer;

public enum BlendMode {
  /**
   * No blending happens
   */
  NO_BLENDING,
  /**
   * Transparent if {@code alpha < 255}
   */
  MASK,

  /**
   * Blends based on alpha of pixel being drawn
   */
  NORMAL,

  /**
   * Random chance of using color of pixel being drawn based on alpha
   */
  DISSOLVE,

  /**
   * Adds the two colors together ignoring alpha
   */
  LINEAR_DODGE,
}
