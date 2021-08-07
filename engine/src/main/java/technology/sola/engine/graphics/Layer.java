package technology.sola.engine.graphics;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Layer {
  public static final int DEFAULT_PRIORITY = 0;
  private final PriorityQueue<PrioritizedDrawItem> drawQueue;
  private final String name;
  private boolean isEnabled = true;

  public Layer(String name) {
    this.name = name;
    drawQueue = new PriorityQueue<>(Comparator.comparingInt(item -> item.priority));
  }

  public void add(DrawItem drawItem) {
    add(drawItem, DEFAULT_PRIORITY);
  }

  public void add(DrawItem drawItem, int priority) {
    drawQueue.add(new PrioritizedDrawItem(drawItem, priority));
  }

  public void draw(Renderer renderer) {
    if (isEnabled) {
      drawQueue.forEach(drawItem -> drawItem.draw(renderer));
    }
    drawQueue.clear();
  }

  public boolean isEnabled() {
    return isEnabled;
  }

  public void setEnabled(boolean isEnabled) {
    this.isEnabled = isEnabled;
  }

  public String getName() {
    return name;
  }

  private static class PrioritizedDrawItem implements DrawItem {
    private final DrawItem drawItem;
    private final int priority;

    private PrioritizedDrawItem(DrawItem drawItem, int priority) {
      this.drawItem = drawItem;
      this.priority = priority;
    }

    @Override
    public void draw(Renderer renderer) {
      drawItem.draw(renderer);
    }
  }
}
