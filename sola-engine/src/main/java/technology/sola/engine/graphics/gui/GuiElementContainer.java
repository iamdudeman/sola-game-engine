package technology.sola.engine.graphics.gui;

import technology.sola.engine.graphics.gui.event.GuiKeyEvent;
import technology.sola.engine.graphics.gui.properties.Display;
import technology.sola.engine.graphics.gui.properties.GuiElementBaseProperties;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.MouseEvent;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GuiElementContainer<T extends GuiElementBaseProperties<?>> extends GuiElement<T> {
  protected List<GuiElement<?>> children = List.of();

  public GuiElementContainer(SolaGuiDocument document, T properties) {
    super(document, properties);
  }

  @Override
  public void render(Renderer renderer) {
    super.render(renderer);

    if (children.stream().anyMatch(child -> child.properties().isLayoutChanged())) {
      recalculateLayout();
      properties().setLayoutChanged(false);
    }

    if (!properties().isHidden()) {
      children.forEach(child -> child.render(renderer));
    }
  }

  @Override
  public boolean isLayoutChanged() {
    return super.isLayoutChanged() || children.stream().anyMatch(GuiElement::isLayoutChanged);
  }

  @Override
  public void renderSelf(Renderer renderer, int x, int y) {
    // Nothing needed here
  }

  @Override
  public boolean isFocussed() {
    return document.isFocussedElement(this) || children.stream().anyMatch(GuiElement::isFocussed);
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

  @Override
  public GuiElement<?> getElementById(String id) {
    if (id.equals(properties.getId())) {
      return this;
    }

    for (GuiElement<?> child : children) {
      GuiElement<?> possibleMatch = child.getElementById(id);

      if (possibleMatch != null) {
        return possibleMatch;
      }
    }

    return null;
  }

  public List<GuiElement<?>> getChildren() {
    return children.stream().toList();
  }

  public List<GuiElement<?>> getFocusableChildren() {
    return children.stream()
      .filter(child -> child.properties.isFocusable())
      .toList();
  }

  public List<GuiElement<?>> getLayoutChildren() {
    return children.stream()
      .filter(child -> child.properties.getDisplay() != Display.NONE)
      .toList();
  }

  public GuiElementContainer<T> addChild(GuiElement<?>... children) {
    if (children == null) {
      return this;
    }

    List<GuiElement<?>> newChildren = new ArrayList<>(this.children.size() + children.length);

    newChildren.addAll(this.children);
    newChildren.addAll(Arrays.asList(children));

    this.children = newChildren;

    properties.setLayoutChanged(true);

    return this;
  }

  public GuiElementContainer<T> removeChild(GuiElement<?> childToRemove) {
    if (childToRemove != null) {
      this.children = this.children.stream().filter(child -> child != childToRemove).toList();

      if (childToRemove.isFocussed()) {
        this.requestFocus();
      }

      properties.setLayoutChanged(true);
    }

    return this;
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

      children.forEach(child -> child.handleMouseEvent(event, eventType));
    } else {
      children.forEach(child -> child.handleMouseEvent(event, eventType));

      onMouseExit(event);
    }
  }

  protected int calculateChildRequiredHorizontalSpace(GuiElement<?> childEle) {
    if (childEle.properties.getDisplay() == Display.NONE) {
      return 0;
    }

    return childEle.getWidth() + childEle.properties().margin.getLeft() + childEle.properties().margin.getRight();
  }

  protected int calculateChildRequiredVerticalSpace(GuiElement<?> childEle) {
    if (childEle.properties.getDisplay() == Display.NONE) {
      return 0;
    }

    return childEle.getHeight() + childEle.properties().margin.getTop() + childEle.properties().margin.getBottom();
  }
}
