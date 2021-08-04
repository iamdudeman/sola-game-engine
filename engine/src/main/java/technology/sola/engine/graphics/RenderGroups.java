package technology.sola.engine.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RenderGroups {
  private final List<RenderGroup> renderGroupList = new ArrayList<>();

  RenderGroups() {
  }

  public RenderGroup drawOn(String name, Consumer<Renderer> renderItem) {
    return drawOn(name, renderItem, RenderGroup.DEFAULT_PRIORITY);
  }

  public RenderGroup drawOn(String name, Consumer<Renderer> renderItem, int priority) {
    RenderGroup renderGroup = renderGroupList.stream()
      .filter(rg -> rg.getName().equals(name))
      .findFirst()
      .orElse(new RenderGroup(renderGroupList.size(), name));

    renderGroup.draw(renderItem, priority);

    return renderGroup;
  }

  public RenderGroup create(String name) {
    int order = renderGroupList.size();
    RenderGroup renderGroup = new RenderGroup(order, name);

    renderGroupList.add(renderGroup);

    return renderGroup;
  }

  public RenderGroup get(String name) {
    return renderGroupList.stream()
      .filter(renderGroup -> renderGroup.getName().equals(name))
      .findFirst()
      .orElseThrow();
  }

  void draw(Renderer renderer) {
    renderGroupList.forEach(renderGroup -> renderGroup.draw(renderer));
  }
}
