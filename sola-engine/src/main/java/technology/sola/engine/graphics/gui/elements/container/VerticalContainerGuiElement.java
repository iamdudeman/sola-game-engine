package technology.sola.engine.graphics.gui.elements.container;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementProperties;

// TODO consider joining container components with a DIRECTION enum

public class VerticalContainerGuiElement extends GuiElement<VerticalContainerGuiElement.Properties> {
  private final int width;
  private final int height;

  public VerticalContainerGuiElement(int width, int height) {
    super(new Properties());
    this.width = width;
    this.height = height;

    properties.setMaxDimensions(width, height);
  }

  @Override
  public int getContentWidth() {
    return width;
  }

  @Override
  public int getContentHeight() {
    return height;
  }

  @Override
  public void renderSelf(Renderer renderer, int x, int y) {
    if (properties.borderColor != null) {
      renderer.drawRect(x, y, getWidth(), getHeight(), properties.borderColor);
    }
  }

  @Override
  public void recalculateChildPositions() {
    if (!properties().isLayoutChanged() && children.stream().noneMatch(child -> child.properties().isLayoutChanged())) {
      return;
    }

    int xOffset = properties.padding.getLeft();
    int yOffset = properties.padding.getTop();

    for (GuiElement<?> child : children) {
      child.properties().setMaxDimensions(
        getWidth() - properties.padding.getLeft() - properties.padding.getRight(),
        getHeight() - properties.padding.getTop() - properties.padding.getBottom()
      );

      yOffset += child.properties().margin.getTop();

      child.properties().setPosition(properties().getX() + xOffset, properties().getY() + yOffset);
      child.recalculateChildPositions();

      int height = child.getContentHeight();

      yOffset += height + child.properties().margin.getBottom();
    }

    properties.setLayoutChanged(false);
  }

  public static class Properties extends GuiElementProperties {
    private Color borderColor;

    public Color getBorderColor() {
      return borderColor;
    }

    public Properties setBorderColor(Color borderColor) {
      this.borderColor = borderColor;

      return this;
    }
  }
}
