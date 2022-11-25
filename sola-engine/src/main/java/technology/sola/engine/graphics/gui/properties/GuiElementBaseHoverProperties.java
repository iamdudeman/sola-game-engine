package technology.sola.engine.graphics.gui.properties;

import technology.sola.engine.graphics.Color;

public class GuiElementBaseHoverProperties {
  private Color borderColor;
  private Color backgroundColor;

  /**
   * Note: hover border color will only render if there is also a border color set. If no hover color is set it will
   * default to the current border color.
   *
   * @return the border color for when the {@link technology.sola.engine.graphics.gui.GuiElement} is hovered
   */
  public Color getBorderColor() {
    return borderColor;
  }

  public GuiElementBaseHoverProperties setBorderColor(Color borderColor) {
    this.borderColor = borderColor;

    return this;
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public GuiElementBaseHoverProperties setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;

    return this;
  }
}
