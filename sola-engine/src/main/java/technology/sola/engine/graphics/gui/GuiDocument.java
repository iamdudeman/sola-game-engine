package technology.sola.engine.graphics.gui;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.graphics.gui.event.GuiKeyEvent;
import technology.sola.engine.graphics.gui.event.GuiMouseEvent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseButton;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.input.MouseInput;

/**
 * GuiDocument is a container for {@link GuiElement} that also handles passing various key and mouse events to the
 * currently active root element set via {@link GuiDocument#setRootElement(GuiElement)}.
 */
@NullMarked
public class GuiDocument {
  private final RootGuiElement root;
  private GuiElement<?, ?> focussedElement;
  private final MouseInput mouseInput;

  /**
   * Creates a new GuiDocument instance, registering listeners for key and mouse related events.
   *
   * @param platform            the {@link SolaPlatform}
   * @param assetLoaderProvider the {@link AssetLoaderProvider}
   * @param mouseInput          the {@link MouseInput}
   */
  public GuiDocument(SolaPlatform platform, AssetLoaderProvider assetLoaderProvider, MouseInput mouseInput) {
    root = new RootGuiElement(this, assetLoaderProvider, platform.getRenderer().getWidth(), platform.getRenderer().getHeight());
    focussedElement = root;
    this.mouseInput = mouseInput;

    // register listeners
    platform.onKeyPressed(this::onKeyPressed);
    platform.onKeyReleased(this::onKeyReleased);
    platform.onMouseMoved(this::onMouseMoved);
    platform.onMousePressed(this::onMousePressed);
    platform.onMouseReleased(this::onMouseReleased);
  }

  /**
   * Sets the current root element that is used for rendering and receiving events to the desired {@link GuiElement}.
   * Also updates the currently focussed element.
   *
   * @param rootEle the new root element
   */
  public void setRootElement(GuiElement<?, ?> rootEle) {
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

    var position = mouseInput.getMousePosition();

    if (position != null) {
      rootEle.onMouseMoved(new GuiMouseEvent(new MouseEvent(MouseButton.NONE, (int) position.x(), (int) position.y())));
    }
  }

  /**
   * Searches the document for a {@link GuiElement} with the desired id.
   *
   * @param id           the id of the element
   * @param elementClass the class of the element
   * @param <T>          the type of the element
   * @return the element
   */
  public <T extends GuiElement<?, ?>> T findElementById(String id, Class<T> elementClass) {
    return root.findElementById(id, elementClass);
  }

  /**
   * Checks to see if a {@link GuiElement} currently has focus.
   *
   * @param guiElement the element to check
   * @return true if the element is currently focussed
   */
  public boolean isFocussed(GuiElement<?, ?> guiElement) {
    return this.focussedElement == guiElement;
  }

  /**
   * Gives the desired {@link GuiElement} focus.
   *
   * @param guiElement the new element to be focussed
   */
  public void requestFocus(GuiElement<?, ?> guiElement) {
    if (guiElement.isFocusable()) {
      var previousElement = this.focussedElement;

      this.focussedElement = guiElement;

      previousElement.styleContainer.invalidate();

      guiElement.styleContainer.invalidate();
    }
  }

  /**
   * Renders the root element to the {@link Renderer}.
   *
   * @param renderer the renderer
   */
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
