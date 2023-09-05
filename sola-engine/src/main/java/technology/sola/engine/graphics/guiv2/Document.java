package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.graphics.renderer.Renderer;

public class Document {
  private final GuiElement root = new RootGuiElement();

  public Document(SolaPlatform platform) {
    // register listeners
    platform.onKeyPressed(root::onKeyPressed);
    platform.onKeyReleased(root::onKeyReleased);
    platform.onMouseMoved(root::onMouseMoved);
    platform.onMousePressed(root::onMousePressed);
    platform.onMouseReleased(root::onMouseReleased);
  }

  public void render(Renderer renderer) {
    root.render(renderer);
  }
}
