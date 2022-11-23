package technology.sola.engine.graphics.screen;

/**
 * AspectMode specifies how rendered output will be scaled when the window resizes.
 */
public enum AspectMode {
  /**
   * Resizing the window will not cause a change in the rendered output.
   */
  IGNORE_RESIZING,
  /**
   * Resizing the window will cause the rendered output to change while maintaining the original aspect ratio.
   */
  MAINTAIN,
  /**
   * Resizing the window will cause the rendered output to stretch to fill the screen.
   */
  STRETCH,
}
