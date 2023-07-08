package technology.sola.engine.graphics.renderer;

import java.util.PriorityQueue;

/**
 * Layers contain a queue of {@link DrawItem} to ensure a particular draw order.
 */
public class Layer {
  public static final int DEFAULT_ORDER = 0;
  private final PriorityQueue<OrdererDrawItem> drawQueue;
  private final String name;
  private boolean isEnabled = true;

  /**
   * Creates a Layer with an identifier name.
   *
   * @param name the name of the layer
   */
  public Layer(String name) {
    this.name = name;
    drawQueue = new PriorityQueue<>();
  }

  /**
   * Adds a {@link DrawItem} to be drawn when this {@link Layer} is drawn with default order.
   *
   * @param drawItem the {@code DrawItem} to be drawn
   */
  public void add(DrawItem drawItem) {
    add(drawItem, DEFAULT_ORDER);
  }

  /**
   * Adds a {@link DrawItem} to be drawn when this {@link Layer} is drawn with desired order.
   *
   * @param drawItem the {@code DrawItem} to be drawn
   * @param order    higher numbers will be drawn later
   */
  public void add(DrawItem drawItem, int order) {
    drawQueue.add(new OrdererDrawItem(drawItem, order));
  }

  /**
   * Draw this Layer to a {@link Renderer}.
   *
   * @param renderer the {@code Renderer} to draw to
   */
  public void draw(Renderer renderer) {
    if (isEnabled) {
      for (DrawItem drawItem : drawQueue) {
        drawItem.draw(renderer);
      }
    }

    drawQueue.clear();
  }

  /**
   * Return if this layer is enabled or not. A layer that is not enabled will not render.
   *
   * @return true if enabled
   */
  public boolean isEnabled() {
    return isEnabled;
  }

  /**
   * Sets the enabled state of this layer.
   *
   * @param isEnabled the new enabled state
   */
  public void setEnabled(boolean isEnabled) {
    this.isEnabled = isEnabled;
  }

  /**
   * @return the identifier name of this layer
   */
  public String getName() {
    return name;
  }

  private record OrdererDrawItem(
    DrawItem drawItem,
    int order
  ) implements DrawItem, Comparable<OrdererDrawItem> {
    @Override
    public void draw(Renderer renderer) {
      drawItem.draw(renderer);
    }

    @Override
    public int compareTo(OrdererDrawItem o) {
      return Integer.compare(this.order, o.order);
    }
  }
}
