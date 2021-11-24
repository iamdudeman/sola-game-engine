package technology.sola.engine.graphics.gui.components;

import technology.sola.engine.ecs.Component;
import technology.sola.engine.graphics.Color;

public class GuiPanelComponent implements Component {
  private Color backgroundColor;
  private String parentUniqueId;
  // TODO border color
  // TODO padding values

  public GuiPanelComponent() {
  }

  public GuiPanelComponent(Color backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public String getParentUniqueId() {
    return parentUniqueId;
  }

  public void setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public void setParent(String parentUniqueId) {
    this.parentUniqueId = parentUniqueId;
  }
}
