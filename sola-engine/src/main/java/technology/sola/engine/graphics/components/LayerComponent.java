package technology.sola.engine.graphics.components;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.renderer.Layer;

import java.io.Serial;

public class LayerComponent implements Component {
  @Serial
  private static final long serialVersionUID = 3288049068995632130L;
  private String layer;
  private int order;

  public LayerComponent(String layer) {
    this(layer, Layer.DEFAULT_ORDER);
  }

  public LayerComponent(String layer, int order) {
    this.layer = layer;
    this.order = order;
  }

  public String getLayer() {
    return layer;
  }

  public int getOrder() {
    return order;
  }

  public void setLayer(String layer) {
    this.layer = layer;
  }

  public void setOrder(int order) {
    this.order = order;
  }
}
