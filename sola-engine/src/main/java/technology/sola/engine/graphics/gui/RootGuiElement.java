package technology.sola.engine.graphics.gui;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.graphics.gui.event.GuiKeyEvent;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;

@NullMarked
class RootGuiElement extends GuiElement<BaseStyles, RootGuiElement> {
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
    var currentBlendFunction = renderer.getBlendFunction();

    renderer.setBlendFunction(BlendMode.NORMAL);
    renderChildren(renderer);
    renderer.setBlendFunction(currentBlendFunction);
  }

  @Override
  @Nullable
  public GuiElementDimensions calculateContentDimensions() {
    return null;
  }

  @Override
  public RootGuiElement self() {
    return this;
  }

  @Override
  public GuiElement<?, ?> getParent() {
    return this;
  }

  @Override
  public boolean isAttached() {
    return guiDocument.root == this;
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
}
