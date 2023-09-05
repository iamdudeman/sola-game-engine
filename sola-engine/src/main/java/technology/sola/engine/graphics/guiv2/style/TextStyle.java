package technology.sola.engine.graphics.guiv2.style;

import technology.sola.engine.graphics.Color;

public class TextStyle extends Style {
  private Color textColor;

  public TextStyle setTextColor(Color textColor) {
    this.textColor = textColor;

    return this;
  }

  public Color getTextColor() {
    return textColor;
  }
}
