package technology.sola.engine.graphics.gui;

import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.gui.element.GuiElement;
import technology.sola.engine.input.MouseEvent;

public class SolaGui {
  private GuiElement<?> root;

  public void setGuiRoot(GuiElement<?> guiElement) {
    this.root = guiElement;
  }

  public void render(Renderer renderer) {
    if (root != null) {
      root.render(renderer);
    }
  }

  public void onMousePressed(MouseEvent event) {
    if (root != null) {
      root.handleMouseEvent(event, "press");
    }
  }

  public void onMouseReleased(MouseEvent event) {
    if (root != null) {
      root.handleMouseEvent(event, "release");
    }
  }

  public void onMouseMoved(MouseEvent event) {
    if (root != null) {
      root.handleMouseEvent(event, "move");
    }
  }
}
