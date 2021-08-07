package technology.sola.engine.graphics;

import technology.sola.engine.graphics.layer.DrawItem;

import java.util.ArrayList;
import java.util.List;

public class RenderGroups {
  private final List<RenderGroup> renderGroupList = new ArrayList<>();

  RenderGroups() {
  }

  public RenderGroup drawOn(String name, DrawItem drawItem) {
    return drawOn(name, drawItem, RenderGroup.DEFAULT_PRIORITY);
  }

  public RenderGroup drawOn(String name, DrawItem drawItem, int priority) {
    RenderGroup renderGroup = renderGroupList.stream()
      .filter(rg -> rg.getName().equals(name))
      .findFirst()
      .orElse(new RenderGroup(renderGroupList.size(), name));

    renderGroup.draw(drawItem, priority);

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
