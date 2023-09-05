package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.graphics.guiv2.style.StyleContainer;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.List;

public abstract class GuiElement {
  private StyleContainer style;
  private List<GuiElement> children;
  private GuiElement parent;
  private int x;
  private int y;
  private boolean isLayoutChanged;

  // todo when adding child be sure to remove from previous parent if there is one
  public abstract void render(Renderer renderer);

  public void recalculateLayout() {
    isLayoutChanged = true;
  }
}
