package technology.sola.engine.graphics.gui.element;

import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.gui.element.properties.GuiElementProperties;

public abstract class GuiElement {
  private GuiElementProperties properties;

  public abstract int getWidth();

  public abstract int getHeight();

  public abstract void render(Renderer renderer, int x, int y);
}
