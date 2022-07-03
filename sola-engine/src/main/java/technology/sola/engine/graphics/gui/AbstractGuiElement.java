package technology.sola.engine.graphics.gui;

import technology.sola.engine.input.MouseEvent;

public abstract class AbstractGuiElement {
  private boolean showing = true;
  private GuiElementMouseEventCallback onMouseEnterCallback = null;
  private GuiElementMouseEventCallback onMouseExitCallback = null;
  private GuiElementMouseEventCallback onMouseClickCallback = null;
  private GuiElementMouseEventCallback onMouseDownCallback = null;
  private GuiElementMouseEventCallback onMouseUpCallback = null;

  /**
   * @return True if this GameGuiComponent is visible
   */
  public final boolean isShowing() { return showing; }

  /**
   * Shows this GameGuiComponent if it was hidden.
   */
  public final void show() { showing = true; }

  /**
   * Hides this GameGuiComponent if it was showing.
   */
  public final void hide() { showing = false; }

  /**
   * Event that is triggered once whenever the mouse first hovers over the bounds of this GameGuiComponent.
   */
  public final void onMouseEnter(MouseEvent mouseEvent) {
    if (onMouseEnterCallback != null) {
      onMouseEnterCallback.call(mouseEvent);
    }
  }

  /**
   * Sets the onMouseEnter callback.
   *
   * @param callback - A {@link GuiElementMouseEventCallback}
   */
  public final void setOnMouseEnterCallback(GuiElementMouseEventCallback callback) {
    onMouseEnterCallback = callback;
  }

  /**
   * Event that is triggered once whenever the mouse first leaves the bounds of this GameGuiComponent.
   */
  public final void onMouseExit(MouseEvent mouseEvent) {
    if (onMouseExitCallback != null) {
      onMouseExitCallback.call(mouseEvent);
    }
  }

  /**
   * Sets the onMouseExit callback.
   *
   * @param callback - A {@link GuiElementMouseEventCallback}
   */
  public final void setOnMouseExitCallback(GuiElementMouseEventCallback callback) {
    onMouseExitCallback = callback;
  }

  /**
   * Event that is triggered once whenever the mouse is clicked down and then released within the bounds of the
   * GameGuiComponent.
   */
  public final void onMouseClick(MouseEvent mouseEvent) {
    if (onMouseClickCallback != null) {
      onMouseClickCallback.call(mouseEvent);
    }
  }

  /**
   * Sets the onMouseClick callback.
   *
   * @param callback - A {@link GuiElementMouseEventCallback}
   */
  public final void setOnMouseClickCallback(GuiElementMouseEventCallback callback) {
    onMouseClickCallback = callback;
  }

  /**
   * Event listener that is triggered whenever the mouse is pressed down within the bounds of this
   * GameGuiComponent.
   */
  public final void onMouseDown(MouseEvent mouseEvent) {
    if (onMouseDownCallback != null) {
      onMouseDownCallback.call(mouseEvent);
    }
  }

  /**
   * Sets the onMouseDown callback.
   *
   * @param callback - A {@link GuiElementMouseEventCallback}
   */
  public final void setOnMouseDownCallback(GuiElementMouseEventCallback callback) {
    onMouseDownCallback = callback;
  }

  /**
   * Event listener that is triggered whenever the mouse is released up within the bounds of this
   * GameGuiComponent.
   */
  public final void onMouseUp(MouseEvent mouseEvent) {
    if (onMouseUpCallback != null) {
      onMouseUpCallback.call(mouseEvent);
    }
  }

  /**
   * Sets the onMouseUp callback.
   *
   * @param callback - A {@link GuiElementMouseEventCallback}
   */
  public final void setOnMouseUpCallback(GuiElementMouseEventCallback callback) {
    onMouseUpCallback = callback;
  }


  /**
   * @return The width value of the bounds of this GameGuiComponent
   */
  public abstract Integer getWidth();

  /**
   * @return The height value of the bounds of this GameGuiComponent
   */
  public abstract Integer getHeight();
}
