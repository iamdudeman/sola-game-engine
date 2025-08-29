package technology.sola.engine.graphics.gui.elements;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementDimensions;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.DefaultStyleValues;
import technology.sola.engine.graphics.gui.style.property.Direction;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;

/**
 * SectionGuiElement is a {@link GuiElement} that is simply a container for its children elements. It has various
 * keyboard shortcuts for navigating its children.
 */
@NullMarked
public class SectionGuiElement extends GuiElement<BaseStyles, SectionGuiElement> {
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
        case ROW -> Key.RIGHT.getCode();
        case ROW_REVERSE -> Key.LEFT.getCode();
        case COLUMN -> Key.DOWN.getCode();
        case COLUMN_REVERSE -> Key.UP.getCode();
      };
      int previousKeyCode = switch (direction) {
        case ROW -> Key.LEFT.getCode();
        case ROW_REVERSE -> Key.RIGHT.getCode();
        case COLUMN -> Key.UP.getCode();
        case COLUMN_REVERSE -> Key.DOWN.getCode();
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
  @Nullable
  public GuiElementDimensions calculateContentDimensions() {
    return null;
  }

  @Override
  public SectionGuiElement self() {
    return this;
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
      var direction = styles().getPropertyValue(BaseStyles::direction, DefaultStyleValues.DIRECTION);

      if (direction == Direction.COLUMN_REVERSE || direction == Direction.ROW_REVERSE) {
        focussedChildren.get(focussedChildren.size() - 1).requestFocus();
      } else {
        focussedChildren.get(0).requestFocus();
      }
    }
  }
}
