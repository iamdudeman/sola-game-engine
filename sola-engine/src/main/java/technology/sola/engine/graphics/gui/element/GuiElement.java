package technology.sola.engine.graphics.gui.element;

import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.gui.element.properties.GuiElementProperties;
import technology.sola.engine.input.MouseEvent;

import java.util.function.Consumer;

public abstract class GuiElement {
  protected GuiElementProperties properties = new GuiElementProperties();

  private Consumer<MouseEvent> onMouseEnterCallback;
  private Consumer<MouseEvent> onMouseExitCallback;
  private Consumer<MouseEvent> onMouseDownCallback;
  private Consumer<MouseEvent> onMouseUpCallback;


  public abstract int getWidth();

  public abstract int getHeight();

  public abstract void render(Renderer renderer, int x, int y);

  public void onMouseEnter(MouseEvent mouseEvent) {
    if (onMouseEnterCallback != null) {
      onMouseEnterCallback.accept(mouseEvent);
    }
  }

  public void setOnMouseEnterCallback(Consumer<MouseEvent> callback) {
    onMouseEnterCallback = callback;
  }

  public void onMouseExit(MouseEvent mouseEvent) {
    if (onMouseExitCallback != null) {
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

  public GuiElementProperties properties() {
    return properties;
  }
}
