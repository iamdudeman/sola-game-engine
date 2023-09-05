package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.graphics.renderer.Renderer;

class RootGuiElement extends GuiElement {
  RootGuiElement(GuiDocument guiDocument, int width, int height) {
    this.guiDocument = guiDocument;
    bounds = new GuiElementBounds(0, 0, width, height);
  }

  @Override
  public void render(Renderer renderer) {
    renderChildren(renderer);
  }
}
