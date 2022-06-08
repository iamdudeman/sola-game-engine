package technology.sola.engine.graphics.components;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.Color;

import java.io.Serial;

public class CircleRendererComponent implements Component {
  @Serial
  private static final long serialVersionUID = -1490398951286309608L;
  private final Color color;
  private final boolean isFilled;

  public CircleRendererComponent(Color color) {
    this(color, true);
  }

  public CircleRendererComponent(Color color, boolean isFilled) {
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
