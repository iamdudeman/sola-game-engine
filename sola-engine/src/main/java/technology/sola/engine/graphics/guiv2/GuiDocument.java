package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.graphics.guiv2.event.GuiKeyEvent;
import technology.sola.engine.graphics.guiv2.event.GuiMouseEvent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;

public class GuiDocument {
  private final GuiElement<?> root;
  private GuiElement<?> focussedElement;

  public GuiDocument(SolaPlatform platform) {
    root = new RootGuiElement(this, platform.getRenderer().getWidth(), platform.getRenderer().getHeight());
    focussedElement = root;

    // register listeners
    platform.onKeyPressed(this::onKeyPressed);
    platform.onKeyReleased(this::onKeyReleased);
    platform.onMouseMoved(this::onMouseMoved);
    platform.onMousePressed(this::onMousePressed);
    platform.onMouseReleased(this::onMouseReleased);
  }

  public GuiElement<?> getRoot() {
    return root;
  }

  public boolean isFocussed(GuiElement<?> guiElement) {
    return this.focussedElement == guiElement;
  }

  public void requestFocus(GuiElement<?> guiElement) {
    this.focussedElement = guiElement;
  }

  public void render(Renderer renderer) {
    root.render(renderer);
  }

  private void onKeyPressed(KeyEvent keyEvent) {
    focussedElement.onKeyPressed(new GuiKeyEvent(keyEvent));
  }

  private void onKeyReleased(KeyEvent keyEvent) {
    focussedElement.onKeyReleased(new GuiKeyEvent(keyEvent));
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
