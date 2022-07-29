package technology.sola.engine.graphics.gui.elements.container;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementContainer;
import technology.sola.engine.graphics.gui.GuiElementGlobalProperties;
import technology.sola.engine.graphics.gui.GuiElementProperties;
import technology.sola.engine.graphics.gui.SolaGui;
import technology.sola.engine.input.Key;

import java.util.List;

public class StreamGuiElementContainer extends GuiElementContainer<StreamGuiElementContainer.Properties> {
  public StreamGuiElementContainer(SolaGui solaGui, Properties properties) {
    super(solaGui, properties);

    setOnKeyPressCallback(keyEvent -> {
      List<GuiElement<?>> focusableChildren = getChildren().stream().filter(child -> child.properties().isFocusable()).toList();

      int forwardKeyCode = properties.direction == Direction.HORIZONTAL ? Key.RIGHT.getCode() : Key.DOWN.getCode();
      int backwardKeyCode = properties.direction == Direction.HORIZONTAL ? Key.LEFT.getCode() : Key.UP.getCode();

      if (keyEvent.getKeyCode() == forwardKeyCode) {
        int focussedIndex = 0;
        boolean noChildFocus = true;

        for (GuiElement<?> child : focusableChildren) {
          if (child.isFocussed()) {
            noChildFocus = false;
            break;
          }
          focussedIndex++;
        }

        if (noChildFocus && !focusableChildren.isEmpty()) {
          focusableChildren.get(0).requestFocus();
          keyEvent.stopPropagation();
        } else if (focussedIndex + 1 < focusableChildren.size()) {
          System.out.println(properties.direction + " " + focussedIndex);
          focusableChildren.get(focussedIndex + 1).requestFocus();
          keyEvent.stopPropagation();
        }
      }

      if (keyEvent.getKeyCode() == backwardKeyCode) {
        int focussedIndex;
        boolean noChildFocus = true;

        for (focussedIndex = focusableChildren.size() - 1; focussedIndex >= 0; focussedIndex--) {
          GuiElement<?> child = focusableChildren.get(focussedIndex);

          if (child.isFocussed()) {
            noChildFocus = false;
            break;
          }
        }

        if (noChildFocus && !focusableChildren.isEmpty()) {
          focusableChildren.get(focusableChildren.size() - 1).requestFocus();
          keyEvent.stopPropagation();
        } else if (focussedIndex - 1 >= 0) {
          focusableChildren.get(focussedIndex - 1).requestFocus();
          keyEvent.stopPropagation();
        }
      }
    });
  }

  @Override
  public int getContentWidth() {
    return properties().preferredWidth;
  }

  @Override
  public int getContentHeight() {
    return properties().preferredHeight;
  }

  @Override
  public void renderSelf(Renderer renderer, int x, int y) {
    if (properties.borderColor != null) {
      renderer.drawRect(x, y, getWidth(), getHeight(), properties.getBorderColor());
    }
  }

  @Override
  public void recalculateLayout() {
    int xOffset = properties.padding.getLeft();
    int yOffset = properties.padding.getTop();

    for (GuiElement<?> child : children) {
      child.properties().setMaxDimensions(
        getWidth() - properties.padding.getLeft() - properties.padding.getRight(),
        getHeight() - properties.padding.getTop() - properties.padding.getBottom()
      );

      xOffset += child.properties().margin.getLeft();
      yOffset += child.properties().margin.getTop();

      child.properties().setPosition(properties().getX() + xOffset, properties().getY() + yOffset);
      child.recalculateLayout();

      xOffset += child.properties().margin.getRight();
      yOffset += child.properties().margin.getBottom();

      if (properties().direction == Direction.HORIZONTAL) {
        xOffset += child.getContentWidth();
      } else if (properties.direction == Direction.VERTICAL) {
        yOffset += child.getContentHeight();
      }
    }

    properties.setLayoutChanged(false);
  }

  public static class Properties extends GuiElementProperties {
    private Color borderColor;
    private int preferredWidth;
    private int preferredHeight;
    private Direction direction = Direction.HORIZONTAL;

    public Properties(GuiElementGlobalProperties globalProperties) {
      super(globalProperties);
    }

    public Color getBorderColor() {
      return borderColor;
    }

    public Properties setBorderColor(Color borderColor) {
      this.borderColor = borderColor;

      return this;
    }

    public Properties setPreferredDimensions(int preferredWidth, int preferredHeight) {
      this.preferredWidth = preferredWidth;
      this.preferredHeight = preferredHeight;
      setMaxDimensions(preferredWidth, preferredHeight);
      setLayoutChanged(true);

      return this;
    }

    public int getPreferredWidth() {
      return preferredWidth;
    }

    public int getPreferredHeight() {
      return preferredHeight;
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
