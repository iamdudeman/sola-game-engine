package technology.sola.engine.graphics.gui;

import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class SolaGui {
  private List<GuiPanel> guiPanelList = new ArrayList<>();

  public void addGuiPanel(GuiPanel guiPanel) {
    guiPanelList.add(guiPanel);
  }

  public void render(Renderer renderer) {
    guiPanelList.forEach(guiPanel -> guiPanel.render(renderer));
  }

  public void onMousePressed(MouseEvent event) {
    guiPanelList.forEach(guiPanel -> guiPanel.onMousePressed(event));
  }

  public void onMouseReleased(MouseEvent event) {
    guiPanelList.forEach(guiPanel -> guiPanel.onMouseReleased(event));
  }

  public void onMouseMoved(MouseEvent event) {
    guiPanelList.forEach(guiPanel -> guiPanel.onMouseMoved(event));
  }
}
