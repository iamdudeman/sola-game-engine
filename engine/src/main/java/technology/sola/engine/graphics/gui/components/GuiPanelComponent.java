package technology.sola.engine.graphics.gui.components;

import technology.sola.engine.ecs.Component;
import technology.sola.engine.graphics.Color;

public class GuiPanelComponent implements Component {
  private Color backgroundColor;
  private Color borderColor = null;

  public GuiPanelComponent() {
  }

  public GuiPanelComponent(Color backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public GuiPanelComponent(Color backgroundColor, Color borderColor) {
    this.backgroundColor = backgroundColor;
    this.borderColor = borderColor;
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public Color getBorderColor() {
    return borderColor;
  }

  public void setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public void setBorderColor(Color borderColor) {
    this.borderColor = borderColor;
  }
}