package technology.sola.engine.graphics.gui.components;

import technology.sola.engine.ecs.Component;
import technology.sola.engine.graphics.Color;

public class GuiTextComponent implements Component {
  private String text;
  private Color color = Color.BLACK;

  public GuiTextComponent() {
  }

  public GuiTextComponent(String text) {
    this.text = text;
  }

  public GuiTextComponent(String text, Color color) {
    this.text = text;
    this.color = color;
  }

  public String getText() {
    return text;
  }

  public Color getColor() {
    return color;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setColor(Color color) {
    this.color = color;
  }
}
