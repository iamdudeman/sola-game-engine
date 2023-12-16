package technology.sola.engine.graphics.guiv2.elements.layout;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public class VerticalBoxGuiLayout extends GuiLayout<VerticalBoxStyles> {
  private final List<GuiElement<?>> children = new ArrayList<>();

  @Override
  public void renderContent(Renderer renderer) {
    for (var child : children) {
      child.render(renderer);
    }
  }

  @Override
  public void onRecalculateLayout() {
    if (isLayoutChanged()) {
      // todo
    }
  }

  public VerticalBoxGuiLayout addChild(GuiElement<?> child) {
    children.add(child);
    invalidateLayout();
    return this;
  }
}
