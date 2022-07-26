package technology.sola.engine.graphics.components;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.RenderMode;

import java.io.Serial;

public class CircleRendererComponent implements Component {
  @Serial
  private static final long serialVersionUID = -1490398951286309608L;
  private Color color;
  private boolean isFilled;
  private RenderMode renderMode = null; // TODO should this be defined here?

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

  public void setColor(Color color) {
    this.color = color;
  }

  public void setFilled(boolean filled) {
    isFilled = filled;
  }

  public RenderMode getRenderMode() {
    return renderMode;
  }

  public void setRenderMode(RenderMode renderMode) {
    this.renderMode = renderMode;
  }
}
