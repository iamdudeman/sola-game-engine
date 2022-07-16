package technology.sola.engine.graphics.gui.element;

import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.input.MouseEvent;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class GuiElement<T extends GuiElementBaseProperties> {
  protected T properties;
  protected List<GuiElement<?>> children = new ArrayList<>();

  private Consumer<MouseEvent> onMouseEnterCallback;
  private Consumer<MouseEvent> onMouseExitCallback;
  private Consumer<MouseEvent> onMouseDownCallback;
  private Consumer<MouseEvent> onMouseUpCallback;

  private boolean wasMouseOverElement = false;

  public abstract int getWidth();

  public abstract int getHeight();

  public abstract void renderSelf(Renderer renderer, int x, int y);

  public void render(Renderer renderer) {
    renderSelf(renderer, properties.getX(), properties.getY());

    recalculateChildPositions();

    children.forEach(child -> {
      if (child.properties().isHidden()) {
        return;
      }

      child.render(renderer);
    });
  }

  public void addChild(GuiElement<?> guiElement) {
    children.add(guiElement);
    properties.setLayoutChanged(true);
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

  public boolean handleMouseEvent(MouseEvent event, String eventType) {
    if (properties().isHidden()) {
      return false;
    }

    int x = properties.getX();
    int y = properties.getY();
    int width = getWidth();
    int height = getHeight();

    Rectangle guiElementBounds = new Rectangle(new Vector2D(x, y), new Vector2D(x + width, y + height));

    boolean isMouseCurrentlyOverElement = guiElementBounds.contains(new Vector2D(event.x(), event.y()));

    if (isMouseCurrentlyOverElement) {
      switch (eventType) {
        case "press" -> onMouseDown(event);
        case "release" -> onMouseUp(event);
        case "move" -> onMouseEnter(event);
      }

      for (GuiElement<?> child : children) {
        child.handleMouseEvent(event, eventType);
      }
    } else {
      for (GuiElement<?> child : children) {
        child.onMouseExit(event);
      }

      onMouseExit(event);
    }

    return isMouseCurrentlyOverElement;
  }

  public void recalculateChildPositions() {
    if (!properties().isLayoutChanged() && children.stream().noneMatch(child -> child.properties().isLayoutChanged())) {
      return;
    }

    int xOffset = 0;

    for (GuiElement<?> guiElement : children) {
      xOffset += guiElement.properties().margin.getLeft();

      guiElement.properties.setPosition(properties().getX() + xOffset, properties().getY());
      guiElement.recalculateChildPositions();

      int width = guiElement.getWidth();

      xOffset += width + guiElement.properties().margin.getRight();
    }

    properties.setLayoutChanged(false);
  }
}
