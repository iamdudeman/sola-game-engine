package technology.sola.engine.graphics;

public enum RenderMode {
  /**
   * No transparency
   */
  NORMAL,
  /**
   * Transparent if alpha < 255
   */
  MASK,
  /**
   * Full transparency
   */
  ALPHA,
}
