package technology.sola.engine.graphics;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class RenderGroup {
  private List<Consumer<Renderer>> renderQueue = new LinkedList<>();
  private int order;
  private String name;

  public RenderGroup(int order, String name) {
    this.order = order;
    this.name = name;
  }

  public int getOrder() {
    return order;
  }

  public String getName() {
    return name;
  }

  public void render(Consumer<Renderer> renderable) {
    this.renderQueue.add(renderable);
  }

  void draw(Renderer renderer) {
    renderQueue.forEach(blah -> blah.accept(renderer));
    renderQueue.clear();
  }
}
