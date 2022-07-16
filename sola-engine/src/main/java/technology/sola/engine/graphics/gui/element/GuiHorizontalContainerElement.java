package technology.sola.engine.graphics.gui.element;

import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.gui.element.properties.GuiElementBaseProperties;

public class GuiHorizontalContainerElement extends GuiElement<GuiHorizontalContainerElement.Properties> {
  private int width;
  private int height;

  public GuiHorizontalContainerElement(int width, int height) {
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

  }

  public static class Properties extends GuiElementBaseProperties {
  }
}
