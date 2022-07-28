package technology.sola.engine.graphics.gui;

import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.input.MouseEvent;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GuiElementContainer<T extends GuiElementProperties> extends GuiElement<T> {
  protected List<GuiElement<?>> children = new ArrayList<>();

  public GuiElementContainer(SolaGui solaGui, T properties) {
    super(solaGui, properties);
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

  @Override
  public void render(Renderer renderer) {
    T properties = properties();

    if (properties.isLayoutChanged() || children.stream().anyMatch(child -> child.properties().isLayoutChanged())) {
      recalculateLayout();
    }

    if (!properties.isHidden()) {
      renderSelf(renderer, properties.getX(), properties.getY());

      children.stream()
        .filter(child -> !child.properties.isHidden())
        .forEach(child -> child.render(renderer));
    }
  }

  @Override
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

      for (GuiElement<?> child : children) {
        child.handleMouseEvent(event, eventType);
      }
    } else {
      for (GuiElement<?> child : children) {
        child.onMouseExit(event);
      }

      onMouseExit(event);
    }
  }

  public List<GuiElement<?>> getChildren() {
    return children;
  }

  public void addChild(GuiElement<?> childOne) {
    children.add(childOne);
    properties.setLayoutChanged(true);
  }

  public void addChild(GuiElement<?> childOne, GuiElement<?> childTwo) {
    children.add(childOne);
    children.add(childTwo);
    properties.setLayoutChanged(true);
  }

  public void addChild(GuiElement<?> childOne, GuiElement<?> childTwo, GuiElement<?> childThree) {
    children.add(childOne);
    children.add(childTwo);
    children.add(childThree);
    properties.setLayoutChanged(true);
  }

  public void addChild(GuiElement<?>... children) {
    this.children.addAll(Arrays.asList(children));
    properties.setLayoutChanged(true);
  }
}
