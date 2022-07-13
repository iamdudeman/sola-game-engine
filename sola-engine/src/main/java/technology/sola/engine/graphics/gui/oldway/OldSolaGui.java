package technology.sola.engine.graphics.gui.oldway;

import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.gui.SolaGui;
import technology.sola.engine.input.MouseEvent;

// TODO maybe have a stack of GuiPanel based on a z-index of sorts
// TODO mouse event handlers would return a boolean or something for if the event should propagate further or not

/**
 * @deprecated use {@link SolaGui} once built
 */
@Deprecated
public class OldSolaGui {
  private GuiPanel guiPanel;

  public void setGuiPanel(GuiPanel guiPanel) {
    this.guiPanel = guiPanel;
  }

  public void render(Renderer renderer) {
    if (guiPanel != null) {
      guiPanel.render(renderer);
    }
  }

  public void onMousePressed(MouseEvent event) {
    if (guiPanel != null) {
      guiPanel.handleMouseEvent(event, "press");
    }
  }

  public void onMouseReleased(MouseEvent event) {
    if (guiPanel != null) {
      guiPanel.handleMouseEvent(event, "release");
    }
  }

  public void onMouseMoved(MouseEvent event) {
    if (guiPanel != null) {
      guiPanel.handleMouseEvent(event, "move");
    }
  }
}
