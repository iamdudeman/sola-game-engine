package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.graphics.renderer.Renderer;

class RootGuiElement extends GuiElement {
  @Override
  public void render(Renderer renderer) {
    renderChildren(renderer);
  }
}
