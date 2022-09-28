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
      int forwardKeyCode = properties.flow == Flow.HORIZONTAL ? Key.RIGHT.getCode() : Key.DOWN.getCode();
      int backwardKeyCode = properties.flow == Flow.HORIZONTAL ? Key.LEFT.getCode() : Key.UP.getCode();

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
    if (properties.flow == Flow.HORIZONTAL) {
      return children.stream().mapToInt(this::calculateChildRequiredHorizontalSpace).sum() + calculateSpaceFromGap();
    }

    return children.stream().mapToInt(this::calculateChildRequiredHorizontalSpace).max().orElse(0);
  }

  @Override
  public int getContentHeight() {
    if (properties.flow == Flow.HORIZONTAL) {
      return children.stream().mapToInt(this::calculateChildRequiredVerticalSpace).max().orElse(0);
    }

    return children.stream().mapToInt(this::calculateChildRequiredVerticalSpace).sum() + calculateSpaceFromGap();
  }

  @Override
  public void recalculateLayout() {
    int borderOffset = properties.getBorderColor() == null ? 0 : 1;
    int xOffset = getX() + properties.padding.getLeft() + borderOffset;
    int yOffset = getY() + properties.padding.getTop() + borderOffset;

    for (GuiElement<?> child : children) {
      var childMargin = child.properties().margin;

      child.setPosition(xOffset + childMargin.getLeft(), yOffset + childMargin.getTop());
      child.recalculateLayout();

      if (properties().flow == Flow.HORIZONTAL) {
        xOffset += childMargin.getLeft();
        xOffset += childMargin.getRight();
        xOffset += child.getWidth() + properties.gap;
      } else if (properties.flow == Flow.VERTICAL) {
        yOffset += childMargin.getTop();
        yOffset += childMargin.getBottom();
        yOffset += child.getHeight() + properties.gap;
      }
    }
  }

  private int calculateSpaceFromGap() {
    if (children.isEmpty()) {
      return 0;
    }

    return ((children.size() - 1) * properties.gap);
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
    private Flow flow = Flow.HORIZONTAL;
    private int gap = 0;

    public Properties(GuiElementGlobalProperties globalProperties) {
      super(globalProperties);
    }

    public Flow getFlow() {
      return flow;
    }

    public Properties setFlow(Flow flow) {
      this.flow = flow;
      setLayoutChanged(true);

      return this;
    }

    public int getGap() {
      return gap;
    }

    public Properties setGap(int gap) {
      this.gap = gap;
      setLayoutChanged(true);

      return this;
    }
  }

  public enum Flow {
    HORIZONTAL,
    VERTICAL
  }
}
