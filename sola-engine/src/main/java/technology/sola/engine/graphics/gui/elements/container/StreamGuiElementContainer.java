package technology.sola.engine.graphics.gui.elements.container;

import technology.sola.engine.core.module.graphics.gui.SolaGui;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementContainer;
import technology.sola.engine.graphics.gui.properties.GuiElementGlobalProperties;
import technology.sola.engine.graphics.gui.properties.GuiElementProperties;
import technology.sola.engine.input.Key;

import java.util.List;

public class StreamGuiElementContainer extends GuiElementContainer<StreamGuiElementContainer.Properties> {
  public StreamGuiElementContainer(SolaGui solaGui, Properties properties) {
    super(solaGui, properties);

    setOnKeyPressCallback(keyEvent -> {
      int forwardKeyCode = properties.direction == Direction.HORIZONTAL ? Key.RIGHT.getCode() : Key.DOWN.getCode();
      int backwardKeyCode = properties.direction == Direction.HORIZONTAL ? Key.LEFT.getCode() : Key.UP.getCode();

      if (keyEvent.getKeyCode() == forwardKeyCode) {
        List<GuiElement<?>> focusableChildren = getFocusableChildren();
        int nextFocussedIndex = findFocussedChild(focusableChildren) + 1;

        if (nextFocussedIndex < focusableChildren.size()) {
          focusableChildren.get(nextFocussedIndex).requestFocus();
          keyEvent.stopPropagation();
        }
      }

      if (keyEvent.getKeyCode() == backwardKeyCode) {
        List<GuiElement<?>> focusableChildren = getFocusableChildren();
        int nextFocussedIndex = findFocussedChild(focusableChildren) - 1;

        if (nextFocussedIndex >= 0) {
          focusableChildren.get(nextFocussedIndex).requestFocus();
          keyEvent.stopPropagation();
        }
      }
    });
  }

  @Override
  public int getContentWidth() {
    if (properties.direction == Direction.HORIZONTAL) {
      return children.stream().mapToInt(ele -> ele.getWidth() + ele.properties().margin.getLeft() + ele.properties().margin.getRight()).sum();
    }

    return children.stream().mapToInt(ele -> ele.getWidth() + ele.properties().margin.getLeft() + ele.properties().margin.getRight()).max().orElse(0);
  }

  @Override
  public int getContentHeight() {
    if (properties.direction == Direction.HORIZONTAL) {
      return children.stream().mapToInt(ele -> ele.getHeight() + ele.properties().margin.getTop() + ele.properties().margin.getBottom()).max().orElse(0);
    }

    return children.stream().mapToInt(ele -> ele.getHeight() + ele.properties().margin.getTop() + ele.properties().margin.getBottom()).sum();
  }

  @Override
  public void recalculateLayout() {
    int borderOffset = properties.getBorderColor() == null ? 0 : 1;
    int xOffset = properties.padding.getLeft() + borderOffset;
    int yOffset = properties.padding.getTop() + borderOffset;

    for (GuiElement<?> child : children) {
      xOffset += child.properties().margin.getLeft();
      yOffset += child.properties().margin.getTop();

      child.setPosition(getX() + xOffset, getY() + yOffset);
      child.recalculateLayout();

      xOffset += child.properties().margin.getRight();
      yOffset += child.properties().margin.getBottom();

      if (properties().direction == Direction.HORIZONTAL) {
        xOffset += child.getWidth();
      } else if (properties.direction == Direction.VERTICAL) {
        yOffset += child.getHeight();
      }
    }
  }

  private int findFocussedChild(List<GuiElement<?>> children) {
    int focussedIndex = 0;

    for (GuiElement<?> child : children) {
      if (child.isFocussed()) {
        return focussedIndex;
      }

      focussedIndex++;
    }

    return -1;
  }

  public static class Properties extends GuiElementProperties {
    private Direction direction = Direction.HORIZONTAL;

    public Properties(GuiElementGlobalProperties globalProperties) {
      super(globalProperties);
    }

    public Direction getDirection() {
      return direction;
    }

    public Properties setDirection(Direction direction) {
      this.direction = direction;
      setLayoutChanged(true);

      return this;
    }
  }

  public enum Direction {
    HORIZONTAL,
    VERTICAL
  }
}
