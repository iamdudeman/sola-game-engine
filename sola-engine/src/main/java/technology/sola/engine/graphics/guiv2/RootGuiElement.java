package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.renderer.Renderer;

class RootGuiElement extends GuiElement<BaseStyles> {
  private final GuiDocument guiDocument;

  RootGuiElement(GuiDocument guiDocument, int width, int height) {
    this.guiDocument = guiDocument;
    bounds = new GuiElementBounds(0, 0, width, height);
    contentBounds = bounds;
  }

  @Override
  public void renderContent(Renderer renderer) {
    renderChildren(renderer);
  }

  @Override
  GuiDocument getGuiDocument() {
    return guiDocument;
  }

  @Override
  public boolean isLayoutChanged() {
    return false;
  }
}
