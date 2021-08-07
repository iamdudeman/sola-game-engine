package technology.sola.engine.graphics;

import technology.sola.engine.graphics.layer.DrawItem;

import java.util.Comparator;
import java.util.PriorityQueue;

public class RenderGroup {
  public static final int DEFAULT_PRIORITY = 0;
  private final PriorityQueue<PrioritizedDrawItem> renderQueue;
  private final int order;
  private final String name;
  private boolean isEnabled = true;

  RenderGroup(int order, String name) {
    this.order = order;
    this.name = name;
    renderQueue = new PriorityQueue<>(Comparator.comparingInt(drawItem -> drawItem.priority));
  }

  public boolean isEnabled() {
    return isEnabled;
  }

  public void setEnabled(boolean isEnabled) {
    this.isEnabled = isEnabled;
  }

  public int getOrder() {
    return order;
  }

  public String getName() {
    return name;
  }

  public void draw(DrawItem drawItem) {
    draw(drawItem, DEFAULT_PRIORITY);
  }

  public void draw(DrawItem drawItem, int priority) {
    renderQueue.add(new PrioritizedDrawItem(drawItem, priority));
  }

  void draw(Renderer renderer) {
    renderQueue.forEach(prioritizedDrawItem -> prioritizedDrawItem.drawItem.draw(renderer));
    renderQueue.clear();
  }

  private static class PrioritizedDrawItem {
    private final DrawItem drawItem;
    private final int priority;

    private PrioritizedDrawItem(DrawItem drawItem, int priority) {
      this.drawItem = drawItem;
      this.priority = priority;
    }
  }
}
