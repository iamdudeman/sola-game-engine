package technology.sola.engine.graphics.guiv2.style;

import technology.sola.engine.graphics.Color;

public class Style {
  private static int counter = 0;
  final String styleId;
  boolean isActive = false;
  private Color backgroundColor;

  public Style() {
    this("" + counter++);
  }

  public Style(String styleId) {
    this.styleId = styleId;
  }

  public boolean isActive() {
    return isActive;
  }

  public Style setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;

    return this;
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }
}
