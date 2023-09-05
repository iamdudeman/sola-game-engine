package technology.sola.engine.graphics.guiv2.style;

import technology.sola.engine.graphics.Color;

public class BaseStyles {
  private Color backgroundColor;

  public BaseStyles setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;

    return this;
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }
}
