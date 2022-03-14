package technology.sola.engine.graphics.components;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.Layer;

import java.io.Serial;

public class LayerComponent implements Component<LayerComponent> {
  @Serial
  private static final long serialVersionUID = 3288049068995632130L;
  private String layer;
  private int priority;

  public LayerComponent(String layer) {
    this(layer, Layer.DEFAULT_PRIORITY);
  }

  public LayerComponent(String layer, int priority) {
    this.layer = layer;
    this.priority = priority;
  }

  @Override
  public LayerComponent copy() {
    return new LayerComponent(layer, priority);
  }

  public String getLayer() {
    return layer;
  }

  public int getPriority() {
    return priority;
  }

  public void setLayer(String layer) {
    this.layer = layer;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }
}
