package technology.sola.engine.graphics.gui;

import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

import java.util.function.Consumer;

public abstract class GuiElement<T extends GuiElementProperties> {
  protected final T properties;

  private Consumer<MouseEvent> onMouseEnterCallback;
  private Consumer<MouseEvent> onMouseExitCallback;
  private Consumer<MouseEvent> onMouseDownCallback;
  private Consumer<MouseEvent> onMouseUpCallback;

  private boolean wasMouseOverElement = false;

  public GuiElement(T properties) {
    this.properties = properties;
  }

  public abstract int getContentWidth();

  public abstract int getContentHeight();

  public int getWidth() {
    return Math.min(properties().getMaxWidth(), getContentWidth());
  }

  public int getHeight() {
    return Math.min(properties().getMaxHeight(), getContentHeight());
  }

  public abstract void recalculateLayout();

  public abstract void renderSelf(Renderer renderer, int x, int y);

  public void render(Renderer renderer) {
    if (properties.isLayoutChanged()) {
      recalculateLayout();
    }

    if (!properties.isHidden()) {
      renderSelf(renderer, properties.getX(), properties.getY());
    }
  }

  public void requestFocus() {
    // todo how to "unfocus" previously focussed element
  }

  public void handleKeyEvent(KeyEvent event, String eventType) {
    // todo check if has focus

    switch (eventType) {
      case "press":

        break;
      case "release":

        break;
    }
  }

  public void handleMouseEvent(MouseEvent event, String eventType) {
    T properties = properties();

    if (properties.isHidden()) {
      return;
    }

    int x = properties.getX();
    int y = properties.getY();
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
}
