package technology.sola.engine.graphics.gui.elements;

import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementDimensions;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.DefaultStyleValues;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;

/**
 * SectionGuiElement is a {@link GuiElement} that is simply a container for its children elements. It has various
 * keyboard shortcuts for navigating its children.
 */
public class SectionGuiElement extends GuiElement<BaseStyles> {
  /**
   * Creates a new SectionGuiElement instance and registers key pressed events for navigating its children.
   *
   * <p>
   * Direction: row, row-reverse
   * <ul>
   *   <li>{@link Key#RIGHT} - next element</li>
   *   <li>{@link Key#LEFT} - previous element</li>
   * </ul>
   *
   * <p>
   * Direction: column, column-reverse
   * <ul>
   *   <li>{@link Key#DOWN} - next element</li>
   *   <li>{@link Key#UP} - previous element</li>
   * </ul>
   */
  public SectionGuiElement() {
    super();

    events().keyPressed().on(keyEvent -> {
      int keyCode = keyEvent.getKeyEvent().keyCode();
      var direction = styles().getPropertyValue(BaseStyles::direction, DefaultStyleValues.DIRECTION);
      int nextKeyCode = switch (direction) {
        case ROW, ROW_REVERSE -> Key.RIGHT.getCode();
        case COLUMN, COLUMN_REVERSE -> Key.DOWN.getCode();
      };
      int previousKeyCode = switch (direction) {
        case ROW, ROW_REVERSE -> Key.LEFT.getCode();
        case COLUMN, COLUMN_REVERSE -> Key.UP.getCode();
      };

      if (keyCode == nextKeyCode) {
        var focusableChildren = getFocusableChildren();
        int nextFocussedIndex = findFocussedChildIndex(focusableChildren) + 1;

        if (nextFocussedIndex < focusableChildren.size()) {
          focusableChildren.get(nextFocussedIndex).requestFocus();
          keyEvent.stopPropagation();
        }
      } else if (keyCode == previousKeyCode) {
        var focusableChildren = getFocusableChildren();
        int nextFocussedIndex = findFocussedChildIndex(focusableChildren) - 1;

        if (nextFocussedIndex >= 0) {
          focusableChildren.get(nextFocussedIndex).requestFocus();
          keyEvent.stopPropagation();
        }
      }
    });
  }

  @Override
  public void renderContent(Renderer renderer) {
    renderChildren(renderer);
  }

  @Override
  public GuiElementDimensions calculateContentDimensions() {
    return null;
  }

  @Override
  public boolean isFocusable() {
    return super.isFocusable() && !getFocusableChildren().isEmpty();
  }

  @Override
  public void requestFocus() {
    var focussedChildren = getFocusableChildren();

    if (focussedChildren.isEmpty()) {
      super.requestFocus();
    } else {
      focussedChildren.get(0).requestFocus();
    }
  }
}
