package technology.sola.engine.graphics.gui.element.container;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.gui.element.GuiElement;
import technology.sola.engine.graphics.gui.element.GuiElementBaseProperties;

public class HorizontalContainerGuiElement extends GuiElement<HorizontalContainerGuiElement.Properties> {
  private final int width;
  private final int height;

  public HorizontalContainerGuiElement(int width, int height) {
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
