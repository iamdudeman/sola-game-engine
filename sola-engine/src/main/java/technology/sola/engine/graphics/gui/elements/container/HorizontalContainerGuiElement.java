package technology.sola.engine.graphics.gui.elements.container;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementProperties;

public class HorizontalContainerGuiElement extends GuiElement<HorizontalContainerGuiElement.Properties> {
  private final int width;
  private final int height;

  public HorizontalContainerGuiElement(int width, int height) {
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
      renderer.drawRect(x, y, getWidth(), getHeight(), properties.getBorderColor());
    }
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
