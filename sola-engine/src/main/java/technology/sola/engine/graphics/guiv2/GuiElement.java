package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.graphics.guiv2.event.GuiElementEvents;
import technology.sola.engine.graphics.guiv2.event.GuiKeyEvent;
import technology.sola.engine.graphics.guiv2.event.GuiMouseEvent;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.StyleContainer;
import technology.sola.engine.graphics.guiv2.style.property.Background;
import technology.sola.engine.graphics.guiv2.style.property.Border;
import technology.sola.engine.graphics.guiv2.style.property.Spacing;
import technology.sola.engine.graphics.guiv2.style.property.Visibility;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiElement<Style extends BaseStyles> {
  protected final StyleContainer<Style> styleContainer;
  protected GuiElementBounds bounds;
  GuiDocument guiDocument;
  private final GuiElementEvents events = new GuiElementEvents();
  private final List<GuiElement<?>> children = new ArrayList<>();
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

  public abstract void renderContent(Renderer renderer);

  public abstract int getContentWidth();

  public abstract int getContentHeight();

  public int getHeight() {
    Border border = getStyles().getPropertyValue(BaseStyles::border, Border.NONE);
    Spacing margin = getStyles().getPropertyValue(BaseStyles::margin, Spacing.NONE);
    Spacing padding = getStyles().getPropertyValue(BaseStyles::padding, Spacing.NONE);

    int borderSize = border.left() + border.right();
    int paddingSize = padding.left().getValue(parent.getHeight()) + padding.right().getValue(parent.getHeight());
    int marginSize = margin.left().getValue(parent.getHeight()) + margin.right().getValue(parent.getHeight());

    return getContentHeight() + borderSize + paddingSize + marginSize;
  }

  public int getWidth() {
    Border border = getStyles().getPropertyValue(BaseStyles::border, Border.NONE);
    Spacing margin = getStyles().getPropertyValue(BaseStyles::margin, Spacing.NONE);
    Spacing padding = getStyles().getPropertyValue(BaseStyles::padding, Spacing.NONE);

    int borderSize = border.top() + border.bottom();
    int paddingSize = padding.top().getValue(parent.getWidth()) + padding.bottom().getValue(parent.getWidth());
    int marginSize = margin.top().getValue(parent.getWidth()) + margin.bottom().getValue(parent.getWidth());

    return getContentWidth() + borderSize + paddingSize + marginSize;
  }

  public void render(Renderer renderer) {
    recalculateLayout();

    if (styleContainer.getPropertyValue(BaseStyles::visibility, Visibility.VISIBLE) == Visibility.HIDDEN) {
      return;
    }

    Background background = styleContainer.getPropertyValue(BaseStyles::background);

    if (background != null && background.color() != null) {
      // todo subtract padding from this if padding is included in bounds calculation
      renderer.fillRect(bounds.x(), bounds.y(), bounds.width(), bounds.height(), background.color());
    }

    renderContent(renderer);
  }

  public void renderChildren(Renderer renderer) {
    // todo handle layout stuff and what not
    children.forEach(child -> child.render(renderer));
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

  public StyleContainer<Style> getStyles() {
    return styleContainer;
  }

  public GuiElement<?> getParent() {
    return parent;
  }

  public GuiElementBounds getBounds() {
    return bounds;
  }

  public GuiElement<Style> appendChildren(GuiElement<?>... children) {
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

    return this;
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

  boolean isLayoutChanged() {
    return isLayoutChanged;
  }

  private void recalculateLayout() {
    if (!isLayoutChanged()) {
      return;
    }

//    var layout = styleContainer.getPropertyValue(BaseStyles::layout, new BlockLayout());
//
//    bounds = layout.calculateBounds(this);
//    children.forEach(GuiElement::recalculateLayout);
//
//    isLayoutChanged = false;
  }
}
