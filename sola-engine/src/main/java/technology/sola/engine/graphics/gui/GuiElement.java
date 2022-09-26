package technology.sola.engine.graphics.gui;

import technology.sola.engine.core.module.graphics.gui.SolaGui;
import technology.sola.engine.graphics.gui.event.GuiKeyEvent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.MouseEvent;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

import java.util.function.Consumer;

public abstract class GuiElement<T extends GuiElementProperties> {
  protected final SolaGui solaGui;
  protected final T properties;
  private int x;
  private int y;

  private Consumer<GuiKeyEvent> onKeyPressCallback;
  private Consumer<GuiKeyEvent> onKeyReleaseCallback;
  private Consumer<MouseEvent> onMouseEnterCallback;
  private Consumer<MouseEvent> onMouseExitCallback;
  private Consumer<MouseEvent> onMouseDownCallback;
  private Consumer<MouseEvent> onMouseUpCallback;

  private boolean wasMouseOverElement = false;

  public GuiElement(SolaGui solaGui, T properties) {
    this.solaGui = solaGui;
    this.properties = properties;
  }

  public abstract int getContentWidth();

  public abstract int getContentHeight();

  public int getWidth() {
    return getContentWidth() + properties.padding.getLeft() + properties.padding.getRight();
  }

  public int getHeight() {
    return getContentHeight() + properties.padding.getTop() + properties.padding.getBottom();
  }

  public abstract void recalculateLayout();

  public abstract void renderSelf(Renderer renderer, int x, int y);

  public void render(Renderer renderer) {
    if (properties.isLayoutChanged()) {
      recalculateLayout();
      properties.setLayoutChanged(false);
    }

    if (!properties.isHidden()) {
      renderSelf(renderer, x, y);

      if (properties.getFocusOutlineColor() != null && isFocussed()) {
        renderer.drawRect(x, y - 1, getWidth() + 2, getHeight() + 2, properties.getFocusOutlineColor());
      }
    }
  }

  public void setPosition(int x, int y) {
    this.x = x;
    this.y = y;
    properties.setLayoutChanged(true);
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public boolean isFocussed() {
    return solaGui.isFocussedElement(this);
  }

  public void requestFocus() {
    solaGui.focusElement(this);
  }

  public void onKeyPress(GuiKeyEvent keyEvent) {
    if (onKeyPressCallback != null) {
      onKeyPressCallback.accept(keyEvent);
    }
  }

  public void setOnKeyPressCallback(Consumer<GuiKeyEvent> onKeyPressCallback) {
    this.onKeyPressCallback = onKeyPressCallback;
  }

  public void onKeyRelease(GuiKeyEvent keyEvent) {
    if (onKeyReleaseCallback != null) {
      onKeyReleaseCallback.accept(keyEvent);
    }
  }

  public void setOnKeyReleaseCallback(Consumer<GuiKeyEvent> onKeyReleaseCallback) {
    this.onKeyReleaseCallback = onKeyReleaseCallback;
  }

  public void onMouseEnter(MouseEvent mouseEvent) {
    if (onMouseEnterCallback != null && !wasMouseOverElement) {
      wasMouseOverElement = true;
      onMouseEnterCallback.accept(mouseEvent);
    }
  }

  public void setOnMouseEnterCallback(Consumer<MouseEvent> callback) {
    onMouseEnterCallback = callback;
  }

  public void onMouseExit(MouseEvent mouseEvent) {
    if (onMouseExitCallback != null && wasMouseOverElement) {
      wasMouseOverElement = false;
      onMouseExitCallback.accept(mouseEvent);
    }
  }

  public void setOnMouseExitCallback(Consumer<MouseEvent> callback) {
    onMouseExitCallback = callback;
  }

  public void onMouseDown(MouseEvent mouseEvent) {
    if (onMouseDownCallback != null) {
      onMouseDownCallback.accept(mouseEvent);
    }
  }

  public void setOnMouseDownCallback(Consumer<MouseEvent> callback) {
    onMouseDownCallback = callback;
  }

  public void onMouseUp(MouseEvent mouseEvent) {
    if (onMouseUpCallback != null) {
      onMouseUpCallback.accept(mouseEvent);
    }
  }

  public void setOnMouseUpCallback(Consumer<MouseEvent> callback) {
    onMouseUpCallback = callback;
  }

  public T properties() {
    return properties;
  }

  public void handleKeyEvent(GuiKeyEvent event) {
    if (!isFocussed()) {
      return;
    }

    switch (event.getType()) {
      case PRESS -> onKeyPress(event);
      case RELEASE -> onKeyRelease(event);
    }
  }

  public void handleMouseEvent(MouseEvent event, String eventType) {
    T properties = properties();

    if (properties.isHidden()) {
      return;
    }

    int width = getWidth();
    int height = getHeight();

    Rectangle guiElementBounds = new Rectangle(new Vector2D(x, y), new Vector2D(x + width, y + height));

    if (guiElementBounds.contains(new Vector2D(event.x(), event.y()))) {
      switch (eventType) {
        case "press" -> onMouseDown(event);
        case "release" -> onMouseUp(event);
        case "move" -> onMouseEnter(event);
      }
    } else {
      onMouseExit(event);
    }
  }
}
