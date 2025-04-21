package technology.sola.engine.graphics.gui.style.property;

/**
 * MainAxisChildren defines how a {@link technology.sola.engine.graphics.gui.GuiElement}'s children when be positioned
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
  /**
   * Children are evenly distributed with spacing between each pair of adjacent items being the same. The first item
   * is flush with the start of the axis and the last item is flush with the end of the axis.
   */
  SPACE_BETWEEN,
}
