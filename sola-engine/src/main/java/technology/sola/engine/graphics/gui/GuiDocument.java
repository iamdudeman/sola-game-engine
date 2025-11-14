package technology.sola.engine.graphics.gui;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.graphics.gui.event.GuiKeyEvent;
import technology.sola.engine.graphics.gui.event.GuiMouseEvent;
import technology.sola.engine.graphics.gui.event.GuiTouchEvent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.*;

/**
 * GuiDocument is a container for {@link GuiElement} that also handles passing various key and mouse events to the
 * currently active root element set via {@link GuiDocument#setRootElement(GuiElement)}.
 */
@NullMarked
public class GuiDocument {
  RootGuiElement root;
  private final SolaPlatform platform;
  private final AssetLoaderProvider assetLoaderProvider;
  private final MouseInput mouseInput;
  private GuiElement<?, ?> focussedElement;
  private boolean isVisible = true;

  /**
   * Creates a new GuiDocument instance, registering listeners for key and mouse-related events.
   *
   * @param platform            the {@link SolaPlatform}
   * @param assetLoaderProvider the {@link AssetLoaderProvider}
   * @param mouseInput          the {@link MouseInput}
   */
  public GuiDocument(SolaPlatform platform, AssetLoaderProvider assetLoaderProvider, MouseInput mouseInput) {
    this.platform = platform;
    this.assetLoaderProvider = assetLoaderProvider;
    this.mouseInput = mouseInput;
    root = new RootGuiElement(this, assetLoaderProvider, platform.getRenderer().getWidth(), platform.getRenderer().getHeight());
    focussedElement = root;

    // register listeners
    platform.onKeyPressed(this::onKeyPressed);
    platform.onKeyReleased(this::onKeyReleased);
    platform.onMouseMoved(this::onMouseMoved);
    platform.onMousePressed(this::onMousePressed);
    platform.onMouseReleased(this::onMouseReleased);
    platform.onTouch(this::onTouch);
  }

  /**
   * @return Whether the {@link GuiDocument} will render or not
   */
  public boolean isVisible() {
    return isVisible;
  }

  /**
   * Sets whether the {@link GuiDocument} will render or not.
   *
   * @param isVisible the new visible state
   */
  public void setVisible(boolean isVisible) {
    this.isVisible = isVisible;
  }

  /**
   * Sets the current root element used for rendering and receiving events to the desired {@link GuiElement}.
   * Also updates the currently focused element.
   *
   * @param rootEle the new root element
   */
  public void setRootElement(GuiElement<?, ?> rootEle) {
    root = new RootGuiElement(this, assetLoaderProvider, platform.getRenderer().getWidth(), platform.getRenderer().getHeight());
    focussedElement = root;

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
  @Nullable
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
   * Sets the visibility of the virtual keyboard.
   *
   * @param visible whether the virtual keyboard should be visible or not
   */
  public void setVirtualKeyboardVisible(boolean visible) {
    platform.setVirtualKeyboardVisible(visible);
  }

  /**
   * Updates the GuiDocument's layout state. This is usually called via {@link GuiDocumentSystem} and should not be
   * called manually.
   */
  public void update() {
    root.recalculateLayout();
  }

  /**
   * Renders the root element to the {@link Renderer}.
   *
   * @param renderer the renderer
   */
  public void render(Renderer renderer) {
    if (!isVisible) {
      return;
    }

    root.render(renderer);
  }

  private void onKeyPressed(KeyEvent keyEvent) {
    if (!isVisible) {
      return;
    }

    focussedElement.onKeyPressed(new GuiKeyEvent(keyEvent));
  }

  private void onKeyReleased(KeyEvent keyEvent) {
    if (!isVisible) {
      return;
    }

    focussedElement.onKeyReleased(new GuiKeyEvent(keyEvent));
  }

  private void onMousePressed(MouseEvent mouseEvent) {
    if (!isVisible) {
      return;
    }

    root.onMousePressed(new GuiMouseEvent(mouseEvent));
  }

  private void onMouseReleased(MouseEvent mouseEvent) {
    if (!isVisible) {
      return;
    }

    root.onMouseReleased(new GuiMouseEvent(mouseEvent));
  }

  private void onMouseMoved(MouseEvent mouseEvent) {
    if (!isVisible) {
      return;
    }

    root.onMouseMoved(new GuiMouseEvent(mouseEvent));
  }

  private void onTouch(TouchEvent touchEvent) {
    if (!isVisible) {
      return;
    }

    root.onTouch(new GuiTouchEvent(touchEvent));
  }
}
