package technology.sola.engine.graphics.guiv2.style.property;

import technology.sola.engine.graphics.Color;

public record Border(Color color, int top, int left, int bottom, int right) {
  public static final Border NONE = new Border(0, Color.BLACK);

  public Border(int size, Color color) {
    this(color, size, size, size, size);
  }
}
