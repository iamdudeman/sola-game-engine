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

  /**
   * Children are evenly distributed along the axis with the space between each adjacent item being the same. The space
   * before the first and after the last item is half the space between each item. If only one item is present it will
   * be centered.
   */
  SPACE_AROUND,

  /**
   * Children are evenly distributed along the axis with the space between each adjacent item being the same. The space
   * before the first and after the last item is also the same.
   */
  SPACE_EVENLY,
}
