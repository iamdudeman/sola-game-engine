package technology.sola.engine.graphics;

import technology.sola.engine.graphics.layer.DrawItem;
import technology.sola.engine.graphics.layer.Layer;

import java.util.ArrayList;
import java.util.List;

public class RenderGroups {
  private final List<Layer> renderGroupList = new ArrayList<>();

  RenderGroups() {
  }

  public Layer drawOn(String name, DrawItem drawItem) {
    return drawOn(name, drawItem, Layer.DEFAULT_PRIORITY);
  }

  public Layer drawOn(String name, DrawItem drawItem, int priority) {
    Layer renderGroup = renderGroupList.stream()
      .filter(rg -> rg.getName().equals(name))
      .findFirst()
      .orElse(new Layer(name));

    renderGroup.add(drawItem, priority);

    return renderGroup;
  }

  public Layer create(String name) {
    Layer renderGroup = new Layer(name);

    renderGroupList.add(renderGroup);

    return renderGroup;
  }

  public Layer get(String name) {
    return renderGroupList.stream()
      .filter(renderGroup -> renderGroup.getName().equals(name))
      .findFirst()
      .orElseThrow();
  }

  void draw(Renderer renderer) {
    renderGroupList.forEach(renderGroup -> renderGroup.draw(renderer));
  }
}
