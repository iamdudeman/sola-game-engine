package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.graphics.guiv2.event.GuiKeyEvent;
import technology.sola.engine.graphics.guiv2.event.GuiMouseEvent;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.renderer.Renderer;

class RootGuiElement extends GuiElement<BaseStyles> {
  // internals
  private final GuiDocument guiDocument;
  private final AssetLoaderProvider assetLoaderProvider;

  RootGuiElement(GuiDocument guiDocument, AssetLoaderProvider assetLoaderProvider, int width, int height) {
    this.guiDocument = guiDocument;
    this.assetLoaderProvider = assetLoaderProvider;
    boundConstraints = new GuiElementBounds(0, 0, width, height);
    bounds = boundConstraints;
    contentBounds = bounds;
  }

  @Override
  public void renderContent(Renderer renderer) {
    renderChildren(renderer);
  }

  @Override
  public GuiElementDimensions calculateContentDimensions() {
    return null;
  }

  @Override
  GuiDocument getGuiDocument() {
    return guiDocument;
  }

  @Override
  protected AssetLoaderProvider getAssetLoaderProvider() {
    return assetLoaderProvider;
  }

  @Override
  void onKeyPressed(GuiKeyEvent event) {
    event.stopPropagation();
  }

  @Override
  void onKeyReleased(GuiKeyEvent event) {
    event.stopPropagation();
  }

  @Override
  void onMousePressed(GuiMouseEvent event) {
    event.stopPropagation();
  }

  @Override
  void onMouseReleased(GuiMouseEvent event) {
    event.stopPropagation();
  }

  @Override
  void onMouseMoved(GuiMouseEvent event) {
    event.stopPropagation();
  }
}
