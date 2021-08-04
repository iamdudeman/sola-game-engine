package technology.sola.engine.graphics;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.Consumer;

public class RenderGroup {
  public static final int DEFAULT_PRIORITY = 0;
  private final PriorityQueue<PrioritizedRenderItem> renderQueue;
  private final int order;
  private final String name;

  RenderGroup(int order, String name) {
    this.order = order;
    this.name = name;
    renderQueue = new PriorityQueue<>(Comparator.comparingInt(item -> item.priority));
  }

  public int getOrder() {
    return order;
  }

  public String getName() {
    return name;
  }

  public void render(Consumer<Renderer> renderItem) {
    render(renderItem, DEFAULT_PRIORITY);
  }

  public void render(Consumer<Renderer> renderItem, int priority) {
    renderQueue.add(new PrioritizedRenderItem(renderItem, priority));
  }

  void draw(Renderer renderer) {
    renderQueue.forEach(prioritizedRenderItem -> prioritizedRenderItem.item.accept(renderer));
    renderQueue.clear();
  }

  private static class PrioritizedRenderItem {
    private final Consumer<Renderer> item;
    private final int priority;

    private PrioritizedRenderItem(Consumer<Renderer> item, int priority) {
      this.item = item;
      this.priority = priority;
    }
  }
}
