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
    var childrenHorizontalSpaceStream = children.stream().mapToInt(this::calculateChildRequiredHorizontalSpace);

    if (properties.flow == Flow.HORIZONTAL) {
      return childrenHorizontalSpaceStream.sum() + calculateSpaceFromGap();
    }

    return childrenHorizontalSpaceStream.max().orElse(0);
  }

  @Override
  public int getContentHeight() {
    var childrenVerticalSpaceStream = children.stream().mapToInt(this::calculateChildRequiredVerticalSpace);

    if (properties.flow == Flow.HORIZONTAL) {
      return childrenVerticalSpaceStream.max().orElse(0);
    }

    return childrenVerticalSpaceStream.sum() + calculateSpaceFromGap();
  }

  @Override
  public void recalculateLayout() {
    Integer width = properties.getWidth();
    int alignOffsetX = 0;

    if (width != null) {
      alignOffsetX = switch (properties.horizontalAlignment) {
        case LEFT -> 0;
        case CENTER -> width / 2 - (getContentWidth() + properties.padding.getLeft() + properties.padding.getRight()) / 2;
        case RIGHT -> (width - ((properties.padding.getLeft() + properties.padding.getRight() + getContentWidth())));
      };
    }

    Integer height = properties.getHeight();
    int alignOffsetY = 0;

    if (height != null) {
      alignOffsetY = switch (properties.verticalAlignment) {
        case TOP -> 0;
        case CENTER -> height / 2 - (getContentHeight() + properties.padding.getTop() + properties.padding.getBottom()) / 2;
        case BOTTOM -> (height - ((properties.padding.getTop() + properties.padding.getBottom() + getContentHeight())));
      };
    }

    int borderOffset = properties.getBorderColor() == null ? 0 : 1;
    int xOffset = getX() + properties.padding.getLeft() + borderOffset + alignOffsetX;
    int yOffset = getY() + properties.padding.getTop() + borderOffset + alignOffsetY;

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
    private HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;
    private VerticalAlignment verticalAlignment = VerticalAlignment.TOP;
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

    public HorizontalAlignment getHorizontalAlignment() {
      return horizontalAlignment;
    }

    public Properties setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
      this.horizontalAlignment = horizontalAlignment;
      setLayoutChanged(true);

      return this;
    }

    public VerticalAlignment getVerticalAlignment() {
      return verticalAlignment;
    }

    public Properties setVerticalAlignment(VerticalAlignment verticalAlignment) {
      this.verticalAlignment = verticalAlignment;
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

  public enum VerticalAlignment {
    TOP,
    CENTER,
    BOTTOM
  }

  public enum HorizontalAlignment {
    LEFT,
    CENTER,
    RIGHT
  }

  public enum Flow {
    HORIZONTAL,
    VERTICAL
  }
}
