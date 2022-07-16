package technology.sola.engine.graphics.gui.element.container;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.gui.element.GuiElement;
import technology.sola.engine.graphics.gui.element.GuiElementBaseProperties;

public class GuiVerticalContainerElement extends GuiElement<GuiVerticalContainerElement.Properties> {
  private final int width;
  private final int height;

  public GuiVerticalContainerElement(int width, int height) {
    this.properties = new Properties();
    this.width = width;
    this.height = height;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
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

    int yOffset = 0;

    for (GuiElement<?> child : children) {
      yOffset += child.properties().margin.getTop();

      child.properties().setPosition(properties().getX(), properties().getY() + yOffset);
      child.recalculateChildPositions();

      int height = child.getHeight();

      yOffset += height + child.properties().margin.getBottom();
    }

    properties.setLayoutChanged(false);
  }

  public static class Properties extends GuiElementBaseProperties {
    private Color borderColor;

    public Color getBorderColor() {
      return borderColor;
    }

    public void setBorderColor(Color borderColor) {
      this.borderColor = borderColor;
    }
  }
}
