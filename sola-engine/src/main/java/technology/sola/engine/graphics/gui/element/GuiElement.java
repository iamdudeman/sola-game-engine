package technology.sola.engine.graphics.gui.element;

import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.gui.element.properties.GuiElementBaseProperties;
import technology.sola.engine.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class GuiElement<T extends GuiElementBaseProperties> {
  protected T properties;
  protected List<GuiElement<?>> children = new ArrayList<>();
  protected List<GuiElementPosition> childPositions = new ArrayList<>();

  private Consumer<MouseEvent> onMouseEnterCallback;
  private Consumer<MouseEvent> onMouseExitCallback;
  private Consumer<MouseEvent> onMouseDownCallback;
  private Consumer<MouseEvent> onMouseUpCallback;

  public abstract int getWidth();

  public abstract int getHeight();

  public abstract void renderSelf(Renderer renderer, int x, int y);

  public void render(Renderer renderer, int x, int y) {
    renderSelf(renderer, x, y);

    recalculateChildPositions(x, y);

    childPositions.forEach(childPosition -> {
      if (childPosition.guiElement.properties().isHidden()) {
        return;
      }

      childPosition.guiElement.render(renderer, childPosition.x, childPosition.y);
    });
  }

  public void addChild(GuiElement<?> guiElement) {
    children.add(guiElement);
    properties.setChanged(true);
  }

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

  public T properties() {
    return properties;
  }

  protected void recalculateChildPositions(int x, int y) {
    if (!properties().isChanged() && children.stream().noneMatch(child -> child.properties().isChanged())) {
      return;
    }

    List<GuiElementPosition> newPositions = new ArrayList<>(children.size());
    int xOffset = 0;

    for (GuiElement<?> guiElement : children) {
      xOffset += guiElement.properties().margin.getLeft();

      newPositions.add(new GuiElementPosition(guiElement, x + xOffset, y));
      guiElement.recalculateChildPositions(x + xOffset, y);

      int width = guiElement.getWidth();

      xOffset += width + guiElement.properties().margin.getRight();
    }

    this.childPositions = newPositions;
    properties.setChanged(false);
  }

  protected record GuiElementPosition(GuiElement<?> guiElement, int x, int y) {
  }
}
