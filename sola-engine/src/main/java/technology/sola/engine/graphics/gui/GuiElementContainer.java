package technology.sola.engine.graphics.gui;

import technology.sola.engine.core.module.graphics.gui.SolaGui;
import technology.sola.engine.graphics.gui.event.GuiKeyEvent;
import technology.sola.engine.graphics.gui.properties.GuiElementProperties;
import technology.sola.engine.graphics.renderer.Renderer;
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

  @Override
  public void render(Renderer renderer) {
    if (children.stream().anyMatch(child -> child.properties().isLayoutChanged())) {
      recalculateLayout();
      properties().setLayoutChanged(false);
    }

    super.render(renderer);

    if (!properties().isHidden()) {
      children.stream()
        .filter(child -> !child.properties.isHidden())
        .forEach(child -> child.render(renderer));
    }
  }

  @Override
  public void renderSelf(Renderer renderer, int x, int y) {
    // Nothing needed here
  }

  @Override
  public boolean isFocussed() {
    return solaGui.isFocussedElement(this) || children.stream().anyMatch(GuiElement::isFocussed);
  }

  @Override
  public void requestFocus() {
    if (properties.isFocusable()) {
      List<GuiElement<?>> focusableChildren = getFocusableChildren();

      if (focusableChildren.isEmpty()) {
        super.requestFocus();
      } else {
        focusableChildren.get(0).requestFocus();
      }
    }
  }

  public List<GuiElement<?>> getChildren() {
    return children;
  }

  public List<GuiElement<?>> getFocusableChildren() {
    return getChildren().stream()
      .filter(child -> child.properties.isFocusable())
      .toList();
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

  @Override
  public void handleKeyEvent(GuiKeyEvent event) {
    if (!isFocussed()) {
      return;
    }

    for (GuiElement<?> child : children) {
      if (event.isAbleToPropagate()) {
        child.handleKeyEvent(event);
      } else {
        break;
      }
    }

    if (event.isAbleToPropagate()) {
      switch (event.getType()) {
        case PRESS -> onKeyPress(event);
        case RELEASE -> onKeyRelease(event);
      }
    }
  }

  @Override
  public void handleMouseEvent(MouseEvent event, String eventType) {
    T properties = properties();

    if (properties.isHidden()) {
      return;
    }

    int width = getWidth();
    int height = getHeight();

    Rectangle guiElementBounds = new Rectangle(new Vector2D(getX(), getY()), new Vector2D(getX() + width, getY() + height));

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
}
