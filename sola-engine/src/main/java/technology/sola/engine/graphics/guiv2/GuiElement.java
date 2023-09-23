package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.graphics.guiv2.event.GuiElementEvents;
import technology.sola.engine.graphics.guiv2.event.GuiKeyEvent;
import technology.sola.engine.graphics.guiv2.event.GuiMouseEvent;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.StyleContainer;
import technology.sola.engine.graphics.guiv2.style.property.Background;
import technology.sola.engine.graphics.guiv2.style.property.Border;
import technology.sola.engine.graphics.guiv2.style.property.Visibility;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiElement<Style extends BaseStyles> {
  protected final StyleContainer<Style> styleContainer;
  /**
   * Includes border, padding and content size.
   */
  protected GuiElementBounds bounds;
  /**
   * Includes only content size.
   */
  protected GuiElementBounds contentBounds; // just content
  final List<GuiElement<?>> children = new ArrayList<>();
  GuiElement<?> parent;
  private final GuiElementEvents events = new GuiElementEvents();
  boolean isLayoutChanged;
  private int x;
  private int y;

  @SafeVarargs
  public GuiElement(Style... styles) {
    styleContainer = new StyleContainer<>();
    bounds = new GuiElementBounds(0, 0, 0, 0);
    contentBounds = bounds;
    setStyle(styles);
  }

  @SafeVarargs
  public final void setStyle(Style... styles) {
    styleContainer.setStyles(styles);
    isLayoutChanged = true;
  }

  public abstract void renderContent(Renderer renderer);

  public void render(Renderer renderer) {
    recalculateLayout();

    if (styleContainer.getPropertyValue(BaseStyles::visibility, Visibility.VISIBLE) == Visibility.HIDDEN) {
      return;
    }

    int x = bounds.x();
    int y = bounds.y();
    int width = bounds.width();
    int height = bounds.height();

    Background background = styleContainer.getPropertyValue(BaseStyles::background);

    if (background != null && background.color() != null) {
      renderer.fillRect(x, y, width, height, background.color());
    }

    // border
    Border border = styleContainer.getPropertyValue(BaseStyles::border, Border.NONE);

    if (border.bottom() > 0) {
      renderer.drawRect(x, y, width - border.right(), height - border.bottom(), border.color());
    }

    if (isFocussed()) {
      Border outline = styleContainer.getPropertyValue(BaseStyles::outline, Border.NONE);

      if (outline.bottom() > 0) {
        renderer.drawRect(x, y, width, height, outline.color());
      }
    }

    renderContent(renderer);
  }

  public boolean isFocussed() {
    return getGuiDocument().isFocussed(this);
  }

  public void requestFocus() {
    getGuiDocument().requestFocus(this);
  }

  public GuiElementEvents events() {
    return events;
  }

  public StyleContainer<Style> getStyles() {
    return styleContainer;
  }

  public GuiElement<?> getParent() {
    return parent;
  }

  public GuiElementBounds getBounds() {
    return bounds;
  }

  public GuiElementBounds getContentBounds() {
    return contentBounds;
  }

  public GuiElement<Style> appendChildren(GuiElement<?>... children) {
    if (children != null) {
      for (GuiElement<?> child : children) {
        if (child.parent != null) {
          child.parent.children.remove(child);
          child.parent.isLayoutChanged = true;
        }

        child.parent = this;
        child.isLayoutChanged = true;
        this.children.add(child);
      }

      this.isLayoutChanged = true;
    }

    return this;
  }

  public boolean isLayoutChanged() {
    return isLayoutChanged;
  }

  protected void renderChildren(Renderer renderer) {
    children.forEach(child -> child.render(renderer));
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

  GuiDocument getGuiDocument() {
    return this.parent.getGuiDocument();
  }

  private void recalculateLayout() {
    if (!isLayoutChanged()) {
      return;
    }

    LayoutUtil.rebuildLayout(this, getParent().contentBounds.x(), getParent().contentBounds.y());
  }
}
