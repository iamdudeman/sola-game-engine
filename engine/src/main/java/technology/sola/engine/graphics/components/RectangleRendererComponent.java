package technology.sola.engine.graphics.components;

import technology.sola.engine.ecs.Component;
import technology.sola.engine.graphics.Color;

public class RectangleRendererComponent implements Component {
  private final Color color;

  public RectangleRendererComponent(Color color) {
    this.color = color;
  }

  public Color getColor() {
    return color;
  }
}
