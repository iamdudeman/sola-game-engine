package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.graphics.guiv2.event.GuiKeyEvent;
import technology.sola.engine.graphics.guiv2.event.GuiMouseEvent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;

public class GuiDocument {
  private final RootGuiElement root;
  private GuiElement<?> focussedElement;

  public GuiDocument(SolaPlatform platform, AssetLoaderProvider assetLoaderProvider) {
    root = new RootGuiElement(this, assetLoaderProvider, platform.getRenderer().getWidth(), platform.getRenderer().getHeight());
    focussedElement = root;

    // register listeners
    platform.onKeyPressed(this::onKeyPressed);
    platform.onKeyReleased(this::onKeyReleased);
    platform.onMouseMoved(this::onMouseMoved);
    platform.onMousePressed(this::onMousePressed);
    platform.onMouseReleased(this::onMouseReleased);
  }

  public void setRootElement(GuiElement<?> rootEle) {
    var previousElement = this.focussedElement;

    focussedElement = root;

    if (previousElement != root) {
      previousElement.styleContainer.invalidate();
    }

    for (var child : root.children.stream().toList()) {
      root.removeChild(child);
    }

    root.appendChildren(rootEle);
    rootEle.requestFocus();
  }

  public <T extends GuiElement<?>> T findElementById(String id, Class<T> elementClass) {
    return root.findElementById(id, elementClass);
  }

  public boolean isFocussed(GuiElement<?> guiElement) {
    return this.focussedElement == guiElement;
  }

  public void requestFocus(GuiElement<?> guiElement) {
    if (guiElement.isFocusable()) {
      var previousElement = this.focussedElement;

      this.focussedElement = guiElement;

      if (previousElement != null) {
        previousElement.styleContainer.invalidate();
      }

      guiElement.styleContainer.invalidate();
    }
  }

  public void render(Renderer renderer) {
    root.render(renderer);
  }

  private void onKeyPressed(KeyEvent keyEvent) {
    if (focussedElement != null) {
      focussedElement.onKeyPressed(new GuiKeyEvent(keyEvent));
    }
  }

  private void onKeyReleased(KeyEvent keyEvent) {
    if (focussedElement != null) {
      focussedElement.onKeyReleased(new GuiKeyEvent(keyEvent));
    }
  }

  private void onMousePressed(MouseEvent mouseEvent) {
    root.onMousePressed(new GuiMouseEvent(mouseEvent));
  }

  private void onMouseReleased(MouseEvent mouseEvent) {
    root.onMouseReleased(new GuiMouseEvent(mouseEvent));
  }

  private void onMouseMoved(MouseEvent mouseEvent) {
    root.onMouseMoved(new GuiMouseEvent(mouseEvent));
  }
}
