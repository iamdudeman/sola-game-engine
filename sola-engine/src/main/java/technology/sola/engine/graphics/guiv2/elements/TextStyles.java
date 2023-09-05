package technology.sola.engine.graphics.guiv2.elements;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;

public class TextStyles extends BaseStyles {
  private Color textColor;

  @Override
  public TextStyles setBackgroundColor(Color backgroundColor) {
    return (TextStyles) super.setBackgroundColor(backgroundColor);
  }

  public TextStyles setTextColor(Color textColor) {
    this.textColor = textColor;

    return this;
  }

  public Color getTextColor() {
    return textColor;
  }
}
