package technology.sola.engine.graphics.components;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Component;
import technology.sola.engine.graphics.renderer.Layer;

/**
 * LayerComponent is a {@link Component} that contains data for a 2d rendering layers for an {@link technology.sola.ecs.Entity}.
 */
@NullMarked
public class LayerComponent implements Component {
  private String layer;
  private int order;
  private boolean isOrderByVerticalPosition = false;

  /**
   * Creates a LayerComponent for the desired layer with default ordering.
   *
   * @param layer the identifier string for the layer
   */
  public LayerComponent(String layer) {
    this(layer, Layer.DEFAULT_ORDER);
  }

  /**
   * Creates a LayerComponent for the desired layer with default ordering.
   *
   * @param layer the identifier string for the layer
   * @param order the order for which this will be rendered in the layer (lower numbers are rendered earlier)
   */
  public LayerComponent(String layer, int order) {
    this.layer = layer;
    this.order = order;
  }

  /**
   * @return the layer string identifier
   */
  public String getLayer() {
    return layer;
  }

  /**
   * @return the order in the layer
   */
  public int getOrder() {
    return order;
  }

  /**
   * Updates the layer.
   *
   * @param layer the new layer string identifier
   */
  public void setLayer(String layer) {
    this.layer = layer;
  }

  /**
   * Updates the order in the layer.
   *
   * @param order the new order
   */
  public void setOrder(int order) {
    this.order = order;
  }

  /**
   * Sets whether this {@link technology.sola.ecs.Entity} should render based on its vertical position within the layer.
   * This ignores the order that was set.
   *
   * @param isOrderByVerticalPosition whether to render within the layer based on the vertical position
   * @return this
   */
  public LayerComponent setOrderByVerticalPosition(boolean isOrderByVerticalPosition) {
    this.isOrderByVerticalPosition = isOrderByVerticalPosition;

    return this;
  }

  /**
   * @return whether to render within the layer based on the vertical position
   */
  public boolean isOrderByVerticalPosition() {
    return isOrderByVerticalPosition;
  }
}
