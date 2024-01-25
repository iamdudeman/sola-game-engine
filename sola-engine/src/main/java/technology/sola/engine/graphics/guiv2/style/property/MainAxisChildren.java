package technology.sola.engine.graphics.guiv2.style.property;

/**
 * MainAxisChildren defines how a {@link technology.sola.engine.graphics.guiv2.GuiElement}'s children when be positioned
 * when extra space is available on the main axis defined by its {@link Direction}.
 */
public enum MainAxisChildren {
  /**
   * Children are aligned to the start of the axis.
   */
  START,
  /**
   * Children are aligned to the center of the axis.
   */
  CENTER,
  /**
   * Children are aligned to the end of the axis.
   */
  END,
}
