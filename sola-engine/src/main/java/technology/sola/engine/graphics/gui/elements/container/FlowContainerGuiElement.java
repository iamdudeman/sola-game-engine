package technology.sola.engine.graphics.gui.elements.container;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementGlobalProperties;
import technology.sola.engine.graphics.gui.GuiElementProperties;

public class FlowContainerGuiElement extends GuiElement<FlowContainerGuiElement.Properties> {
  public FlowContainerGuiElement(Properties properties) {
    super(properties);
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
  public void recalculateChildPositions() {
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
      child.recalculateChildPositions();

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
