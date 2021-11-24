package technology.sola.engine.graphics.components;

import technology.sola.engine.ecs.Component;
import technology.sola.engine.graphics.Layer;

public class LayerComponent implements Component {
  private String layer;
  private int priority;

  public LayerComponent(String layer) {
    this(layer, Layer.DEFAULT_PRIORITY);
  }

  public LayerComponent(String layer, int priority) {
    this.layer = layer;
    this.priority = priority;
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
