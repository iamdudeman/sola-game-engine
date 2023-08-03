package technology.sola.engine.graphics.gui.properties;

import technology.sola.engine.graphics.Color;

/**
 * GuiElementBaseHoverProperties defines the base hover properties for
 * {@link technology.sola.engine.graphics.gui.GuiElement}s.
 */
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

  /**
   * Sets the border color rendered when hovered.
   *
   * @param borderColor the new hover border color
   * @return this
   */
  public GuiElementBaseHoverProperties setBorderColor(Color borderColor) {
    this.borderColor = borderColor;

    return this;
  }

  /**
   * @return the background color for when the {@link technology.sola.engine.graphics.gui.GuiElement} is hovered
   */
  public Color getBackgroundColor() {
    return backgroundColor;
  }

  /**
   * Sets the background color rendered when hovered.
   *
   * @param backgroundColor the new hover background color
   * @return this
   */
  public GuiElementBaseHoverProperties setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;

    return this;
  }
}
