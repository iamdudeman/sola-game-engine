package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.renderer.Renderer;

class RootGuiElement extends GuiElement<BaseStyles> {
  private final GuiDocument guiDocument;

  RootGuiElement(GuiDocument guiDocument, int width, int height) {
    this.guiDocument = guiDocument;
    setStyle(BaseStyles.create().setHeight("100%").setWidth("100%").build());
    bounds = new GuiElementBounds(0, 0, width, height);
    contentBounds = bounds;
    parent = new GuiElement<BaseStyles>() {
      {
        bounds = new GuiElementBounds(0, 0, width, height);
        contentBounds = bounds;
      }

      @Override
      public void renderContent(Renderer renderer) {

      }

      @Override
      public int getContentWidth() {
        return 0;
      }

      @Override
      public int getContentHeight() {
        return 0;
      }

      @Override
      public boolean isLayoutChanged() {
        return false;
      }

    };
  }

  @Override
  public void renderContent(Renderer renderer) {
    renderChildren(renderer);
  }

  @Override
  public int getContentWidth() {
    return 0;
  }

  @Override
  public int getContentHeight() {
    return 0;
  }

  @Override
  GuiDocument getGuiDocument() {
    return guiDocument;
  }
}
