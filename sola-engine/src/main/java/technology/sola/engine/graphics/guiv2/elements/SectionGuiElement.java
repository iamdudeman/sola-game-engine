package technology.sola.engine.graphics.guiv2.elements;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.GuiElementDimensions;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.property.Direction;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;

public class SectionGuiElement extends GuiElement<BaseStyles> {
  public SectionGuiElement() {
    super();

    events().keyPressed().on(keyEvent -> {
      int keyCode = keyEvent.getKeyEvent().keyCode();
      var direction = getStyles().getPropertyValue(BaseStyles::direction, Direction.COLUMN);
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
  public void requestFocus() {
    var focussedChildren = getFocusableChildren();

    if (focussedChildren.isEmpty()) {
      super.requestFocus();
    } else {
      focussedChildren.get(0).requestFocus();
    }
  }
}
