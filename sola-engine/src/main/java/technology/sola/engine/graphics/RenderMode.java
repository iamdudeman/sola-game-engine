package technology.sola.engine.graphics;

// TODO consider renaming to BlendMode (NORMAL -> NO_BLEND and ALPHA -> NORMAL maybe?)

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


  LINEAR_DODGE,
}
