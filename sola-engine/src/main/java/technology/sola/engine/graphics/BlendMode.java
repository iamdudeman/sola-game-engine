package technology.sola.engine.graphics;

// TODO consider renaming to BlendMode (NORMAL -> NO_BLEND and ALPHA -> NORMAL maybe?)

public enum BlendMode {
  /**
   * No transparency
   */
  NO_BLENDING,
  /**
   * Transparent if alpha < 255
   */
  MASK,
  /**
   * Full transparency
   */
  NORMAL,

  DISSOLVE,

  LINEAR_DODGE,
}
