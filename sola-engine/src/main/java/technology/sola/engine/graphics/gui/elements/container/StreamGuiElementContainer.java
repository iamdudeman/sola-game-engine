package technology.sola.engine.graphics.gui.elements.container;

import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementContainer;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.properties.GuiElementBaseHoverProperties;
import technology.sola.engine.graphics.gui.properties.GuiElementBaseProperties;
import technology.sola.engine.graphics.gui.properties.GuiElementGlobalProperties;
import technology.sola.engine.input.Key;

import java.util.List;

public class StreamGuiElementContainer extends GuiElementContainer<StreamGuiElementContainer.Properties> {
  public StreamGuiElementContainer(SolaGuiDocument document) {
    super(document, new StreamGuiElementContainer.Properties(document.globalProperties));

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
    var childrenHorizontalSpaceStream = children.stream().mapToInt(this::calculateChildRequiredHorizontalSpace);

    if (properties.direction == Direction.HORIZONTAL) {
      return childrenHorizontalSpaceStream.sum() + calculateSpaceFromGap();
    }

    return childrenHorizontalSpaceStream.max().orElse(0);
  }

  @Override
  public int getContentHeight() {
    var childrenVerticalSpaceStream = children.stream().mapToInt(this::calculateChildRequiredVerticalSpace);

    if (properties.direction == Direction.HORIZONTAL) {
      return childrenVerticalSpaceStream.max().orElse(0);
    }

    return childrenVerticalSpaceStream.sum() + calculateSpaceFromGap();
  }

  @Override
  public void recalculateLayout() {
    Properties properties = this.properties;
    int borderOffset = properties.getBorderColor() == null ? 0 : 1;
    int xOffset = getX() + properties.padding.getLeft() + borderOffset + getHorizontalAlignmentOffset();
    int yOffset = getY() + properties.padding.getTop() + borderOffset + getVerticalAlignmentOffset();

    for (GuiElement<?> child : children) {
      var childMargin = child.properties().margin;

      child.setPosition(xOffset + childMargin.getLeft(), yOffset + childMargin.getTop());
      child.recalculateLayout();

      if (properties().direction == Direction.HORIZONTAL) {
        xOffset += childMargin.getLeft() + childMargin.getRight() + child.getWidth() + properties.gap;
      } else if (properties.direction == Direction.VERTICAL) {
        yOffset += childMargin.getTop() + childMargin.getBottom() + child.getHeight() + properties.gap;
      }
    }
  }

  private int getHorizontalAlignmentOffset() {
    Integer width = properties.getWidth();

    if (width == null) {
      return 0;
    }

    return switch (properties.horizontalAlignment) {
      case LEFT -> 0;
      case CENTER -> width / 2 - (getContentWidth() + properties.padding.getLeft() + properties.padding.getRight()) / 2;
      case RIGHT -> (width - ((properties.padding.getLeft() + properties.padding.getRight() + getContentWidth())));
    };
  }

  private int getVerticalAlignmentOffset() {
    Integer height = properties.getHeight();

    if (height == null) {
      return 0;
    }

    return switch (properties.verticalAlignment) {
      case TOP -> 0;
      case CENTER -> height / 2 - (getContentHeight() + properties.padding.getTop() + properties.padding.getBottom()) / 2;
      case BOTTOM -> (height - ((properties.padding.getTop() + properties.padding.getBottom() + getContentHeight())));
    };
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

  public static class Properties extends GuiElementBaseProperties<GuiElementBaseHoverProperties> {
    private Direction direction = Direction.HORIZONTAL;
    private HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;
    private VerticalAlignment verticalAlignment = VerticalAlignment.TOP;
    private int gap = 0;

    public Properties(GuiElementGlobalProperties globalProperties) {
      super(globalProperties, new GuiElementBaseHoverProperties());
    }

    public Direction getDirection() {
      return direction;
    }

    public Properties setDirection(Direction direction) {
      this.direction = direction;
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

  public enum Direction {
    HORIZONTAL,
    VERTICAL
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
}
