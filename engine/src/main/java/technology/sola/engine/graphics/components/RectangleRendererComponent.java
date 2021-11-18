package technology.sola.engine.graphics.components;

import technology.sola.engine.ecs.Component;
import technology.sola.engine.graphics.Color;

public class RectangleRendererComponent implements Component {
  private final Color color;
  private final boolean isFilled;

  public RectangleRendererComponent(Color color) {
    this(color, true);
  }

  public RectangleRendererComponent(Color color, boolean isFilled) {
    this.color = color;
    this.isFilled = isFilled;
  }

  public Color getColor() {
    return color;
  }

  public boolean isFilled() {
    return isFilled;
  }
}
