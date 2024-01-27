package technology.sola.engine.graphics.gui.style.property;

/**
 * CrossAxisChildren defines how a {@link technology.sola.engine.graphics.gui.GuiElement}'s children when be positioned
 * when extra space is available on the cross axis defined by its {@link Direction}.
 */
public enum CrossAxisChildren {
  /**
   * Children are stretched across the axis.
   */
  STRETCH,
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
