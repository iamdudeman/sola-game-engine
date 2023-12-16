package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.renderer.Renderer;

class RootGuiElement extends GuiElement<BaseStyles> {
  private final GuiDocument guiDocument;
  private final AssetLoaderProvider assetLoaderProvider;
  private GuiElement<?> element;

  RootGuiElement(GuiDocument guiDocument, AssetLoaderProvider assetLoaderProvider, int width, int height) {
    this.guiDocument = guiDocument;
    this.assetLoaderProvider = assetLoaderProvider;
    bounds = new GuiElementBounds(0, 0, width, height);
    contentBounds = bounds;
  }

  public void setElement(GuiElement<?> element) {
    this.element = element;
    element.setParent(this);
  }

  @Override
  public void renderContent(Renderer renderer) {
    element.render(renderer);
  }

  @Override
  public void onRecalculateLayout() {

  }

  @Override
  GuiDocument getGuiDocument() {
    return guiDocument;
  }

  @Override
  public boolean isLayoutChanged() {
    return false;
  }

  @Override
  protected AssetLoaderProvider getAssetLoaderProvider() {
    return assetLoaderProvider;
  }
}
