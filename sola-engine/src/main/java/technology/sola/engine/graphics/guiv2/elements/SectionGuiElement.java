package technology.sola.engine.graphics.guiv2.elements;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.GuiElementDimensions;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.property.Direction;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;

import java.util.List;

public class SectionGuiElement extends GuiElement<BaseStyles> {
  public SectionGuiElement(BaseStyles... styles) {
    super(styles);

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
        int nextFocussedIndex = findFocussedChild(children) + 1;

        // todo should only be focusable children
        if (nextFocussedIndex < children.size()) {
          // todo should only be focusable children
          children.get(nextFocussedIndex).requestFocus();
          keyEvent.stopPropagation();
        }
      } else if (keyCode == previousKeyCode) {
        // todo should only be focusable children
        int nextFocussedIndex = findFocussedChild(children) - 1;

        if (nextFocussedIndex >= 0) {
          // todo should only be focusable children
          children.get(nextFocussedIndex).requestFocus();
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
    if (children.isEmpty()) {
      super.requestFocus();
    } else {
      children.get(0).requestFocus();
    }
  }

  // todo does this work if there is a nested focus?
  private int findFocussedChild(List<GuiElement<?>> children) {
    int focussedIndex = 0;

    for (var child : children) {
      if (child.isFocussed()) {
        return focussedIndex;
      }

      focussedIndex++;
    }

    return -1;
  }
}
