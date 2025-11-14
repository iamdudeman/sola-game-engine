package technology.sola.engine.graphics.gui;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.input.MouseEvent;

/**
 * GuiElementBounds holds the information for the bounding box for part of {@link GuiElement}.
 *
 * @param x      the left most coordinate
 * @param y      the top most coordinate
 * @param width  the width
 * @param height the height
 */
@NullMarked
public record GuiElementBounds(int x, int y, int width, int height) {
  /**
   * Creates a new {@link GuiElementBounds} instance with position set to the desired x and y. The width and height
   * are kept the same.
   *
   * @param x the x of the new instance
   * @param y the y of the new instance
   * @return the new instance
   */
  public GuiElementBounds setPosition(int x, int y) {
    return new GuiElementBounds(x, y, width(), height());
  }

  /**
   * Creates a new {@link GuiElementBounds} instance with width and height set to the desired values. The position
   * values are kept the same.
   *
   * @param width  the width of the new instance
   * @param height the height of the new instance
   * @return the new instance
   */
  public GuiElementBounds setDimensions(int width, int height) {
    return new GuiElementBounds(x(), y(), width, height);
  }

  /**
   * Checks to see if a point is within bounds.
   *
   * @param x the x of the point to check
   * @param y the y of the point to check
   * @return true if the point is within the bounds
   */
  public boolean contains(int x, int y) {
    return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height;
  }

  /**
   * Checks to see if a point is within bounds.
   *
   * @param x the x of the point to check
   * @param y the y of the point to check
   * @return true if the point is within the bounds
   */
  public boolean contains(float x, float y) {
    return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height;
  }

  /**
   * Checks to see if a {@link MouseEvent} is within bounds.
   *
   * @param mouseEvent the mouse event
   * @return true if the mouse event is within the bounds
   */
  public boolean contains(MouseEvent mouseEvent) {
    return contains(mouseEvent.x(), mouseEvent.y());
  }
}
