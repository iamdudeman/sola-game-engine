package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.graphics.guiv2.event.GuiElementEvents;
import technology.sola.engine.graphics.guiv2.event.GuiKeyEvent;
import technology.sola.engine.graphics.guiv2.event.GuiMouseEvent;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.StyleContainer;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.List;

public abstract class GuiElement<Style extends BaseStyles> {
  protected final StyleContainer<Style> styleContainer;
  protected GuiElementBounds bounds;
  GuiDocument guiDocument;
  private final GuiElementEvents events = new GuiElementEvents();
  private List<GuiElement<?>> children;
  private GuiElement<?> parent;
  private boolean isLayoutChanged;

  @SafeVarargs
  public GuiElement(Style... styles) {
    styleContainer = new StyleContainer<>();
    setStyle(styles);
  }

  @SafeVarargs
  public final void setStyle(Style... styles) {
    styleContainer.setStyles(styles);
    isLayoutChanged = true;
  }

  public abstract void render(Renderer renderer);

  public void renderChildren(Renderer renderer) {
    // todo handle layout stuff and what not
  }

  public boolean isFocussed() {
    return this.guiDocument.isFocussed(this);
  }

  public void requestFocus() {
    this.guiDocument.requestFocus(this);
  }

  public GuiElementEvents events() {
    return events;
  }

  public void appendChildren(GuiElement<?>... children) {
    if (children != null) {
      for (GuiElement<?> child : children) {
        if (child.parent == null) {
          child.guiDocument = this.guiDocument;
        } else {
          child.parent.children.remove(child);
          child.parent.isLayoutChanged = true;
        }

        child.parent = this;
        child.isLayoutChanged = true;
        this.children.add(child);
      }

      this.isLayoutChanged = true;
    }
  }

  void onKeyPressed(GuiKeyEvent event) {
    events.keyPressed().emit(event);

    if (event.isAbleToPropagate()) {
      parent.onKeyPressed(event);
    }
  }

  void onKeyReleased(GuiKeyEvent event) {
    events.keyReleased().emit(event);

    if (event.isAbleToPropagate()) {
      parent.onKeyReleased(event);
    }
  }

  void onMousePressed(GuiMouseEvent event) {
    if (bounds.contains(event.getMouseEvent().x(), event.getMouseEvent().y())) {
      children.forEach(child -> child.onMousePressed(event));

      if (event.isAbleToPropagate()) {
        events.mousePressed().emit(event);
      }
    }
  }

  void onMouseReleased(GuiMouseEvent event) {
    if (bounds.contains(event.getMouseEvent().x(), event.getMouseEvent().y())) {
      children.forEach(child -> child.onMouseReleased(event));

      if (event.isAbleToPropagate()) {
        events.mouseReleased().emit(event);
      }
    }
  }

  void onMouseMoved(GuiMouseEvent event) {
    if (bounds.contains(event.getMouseEvent().x(), event.getMouseEvent().y())) {
      children.forEach(child -> child.onMouseMoved(event));

      if (event.isAbleToPropagate()) {
        events.mouseMoved().emit(event);
      }
    }
  }
}
