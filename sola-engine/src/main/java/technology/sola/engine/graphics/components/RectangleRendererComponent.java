package technology.sola.engine.graphics.components;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.Color;

import java.io.Serial;

public class RectangleRendererComponent implements Component {
  @Serial
  private static final long serialVersionUID = -1301447777016343699L;
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
