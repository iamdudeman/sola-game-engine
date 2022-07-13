package technology.sola.engine.graphics.gui.oldway;

import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.input.MouseEvent;

/**
 * @deprecated use {@link technology.sola.engine.graphics.gui.element.GuiElement} once built
 */
@Deprecated
public abstract class GuiElement {
  private boolean showing = true;
  private GuiElementMouseEventCallback onMouseEnterCallback;
  private GuiElementMouseEventCallback onMouseExitCallback;
  private GuiElementMouseEventCallback onMouseDownCallback;
  private GuiElementMouseEventCallback onMouseUpCallback;

  public boolean isShowing() {
    return showing;
  }

  public void show() {
    showing = true;
  }

  public void hide() {
    showing = false;
  }

  public void onMouseEnter(MouseEvent mouseEvent) {
    if (onMouseEnterCallback != null) {
      onMouseEnterCallback.call(mouseEvent);
    }
  }

  public void setOnMouseEnterCallback(GuiElementMouseEventCallback callback) {
    onMouseEnterCallback = callback;
  }

  public void onMouseExit(MouseEvent mouseEvent) {
    if (onMouseExitCallback != null) {
      onMouseExitCallback.call(mouseEvent);
    }
  }

  public void setOnMouseExitCallback(GuiElementMouseEventCallback callback) {
    onMouseExitCallback = callback;
  }

  public void onMouseDown(MouseEvent mouseEvent) {
    if (onMouseDownCallback != null) {
      onMouseDownCallback.call(mouseEvent);
    }
  }

  public void setOnMouseDownCallback(GuiElementMouseEventCallback callback) {
    onMouseDownCallback = callback;
  }

  public void onMouseUp(MouseEvent mouseEvent) {
    if (onMouseUpCallback != null) {
      onMouseUpCallback.call(mouseEvent);
    }
  }

  public void setOnMouseUpCallback(GuiElementMouseEventCallback callback) {
    onMouseUpCallback = callback;
  }

  public abstract int getWidth();

  public abstract int getHeight();

  public abstract void render(Renderer renderer, int x, int y);
}
