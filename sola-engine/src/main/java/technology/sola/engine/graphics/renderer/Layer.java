package technology.sola.engine.graphics.renderer;

import org.jspecify.annotations.NullMarked;

import java.util.LinkedList;
import java.util.List;

/**
 * Layers contain a queue of {@link DrawItem} to ensure a particular draw order.
 */
public class Layer {
  /**
   * The default order for a {@link DrawItem} that is added to this Layer.
   */
  public static final int DEFAULT_ORDER = 0;
  private final List<OrdererDrawItem> drawItems;
  private final String name;
  private boolean isActive = true;

  /**
   * Creates a Layer with an identifier name.
   *
   * @param name the name of the layer
   */
  public Layer(String name) {
    this.name = name;
    drawItems = new LinkedList<>();
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
    if (isActive) {
      int insertIndex = 0;

      for (var orderedDrawItem : drawItems) {
        if (order < orderedDrawItem.order) {
          break;
        }

        insertIndex++;
      }

      drawItems.add(insertIndex, new OrdererDrawItem(drawItem, order));
    }
  }

  /**
   * Draw this Layer to a {@link Renderer}.
   *
   * @param renderer the {@code Renderer} to draw to
   */
  public void draw(Renderer renderer) {
    if (isActive) {
      for (DrawItem drawItem : drawItems) {
        drawItem.draw(renderer);
      }
    }

    drawItems.clear();
  }

  /**
   * Return if this layer is active or not. A layer that is not active will not render any of its {@link DrawItem}s.
   *
   * @return true if active
   */
  public boolean isActive() {
    return isActive;
  }

  /**
   * Sets the active state of this layer.
   *
   * @param isActive the new active state
   */
  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  /**
   * @return the identifier name of this layer
   */
  public String getName() {
    return name;
  }

  @NullMarked
  private record OrdererDrawItem(
    DrawItem drawItem,
    int order
  ) implements DrawItem, Comparable<OrdererDrawItem> {
    @Override
    public void draw(Renderer renderer) {
      drawItem.draw(renderer);
    }

    @Override
    public int compareTo(OrdererDrawItem ordererDrawItem) {
      return Integer.compare(this.order, ordererDrawItem.order);
    }
  }
}
