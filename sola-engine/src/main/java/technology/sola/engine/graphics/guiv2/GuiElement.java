package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.graphics.guiv2.event.GuiElementEvents;
import technology.sola.engine.graphics.guiv2.event.GuiKeyEvent;
import technology.sola.engine.graphics.guiv2.event.GuiMouseEvent;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.StyleContainer;
import technology.sola.engine.graphics.guiv2.style.property.*;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiElement<Style extends BaseStyles> {
  protected final StyleContainer<Style> styleContainer;
  /**
   * Includes only content size. Do not manually update this unless you know what you are doing!
   */
  protected GuiElementBounds contentBounds;
  /**
   * The maximum allowed size an element can be. {@link GuiElement#bounds} may be smaller than this.
   */
  GuiElementBounds boundConstraints;
  /**
   * Includes border, padding and content size.
   */
  GuiElementBounds bounds;
  final List<GuiElement<?>> children = new ArrayList<>();
  boolean isLayoutChanged;
  private GuiElement<?> parent;
  private final GuiElementEvents events = new GuiElementEvents();
  private boolean isMouseInside = false;

  @SafeVarargs
  public GuiElement(Style... styles) {
    styleContainer = new StyleContainer<>();
    boundConstraints = new GuiElementBounds(0, 0, 0, 0);
    bounds = boundConstraints;
    contentBounds = bounds;
    setStyle(styles);
  }

  @SafeVarargs
  public final void setStyle(Style... styles) {
    styleContainer.setStyles(styles);
    isLayoutChanged = true;
  }

  public abstract void renderContent(Renderer renderer);

  /**
   * Calculates content dimensions for this element. If its dimensions are derived from only its
   * children then it should return null.
   *
   * @return the dimensions of the content of this element or null if only children dimensions matter
   */
  public abstract GuiElementDimensions calculateContentDimensions();

  public void render(Renderer renderer) {
    recalculateLayout();

    if (styleContainer.getPropertyValue(BaseStyles::visibility, Visibility.VISIBLE) != Visibility.VISIBLE) {
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

    // checking any border size for now since they will all be 0 or 1
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

  public GuiElement<Style> removeChild(GuiElement<?> child) {
    if (children.contains(child)) {
      child.parent = null;
      children.remove(child);
      invalidateLayout();
    }

    return this;
  }

  public GuiElement<Style> appendChildren(GuiElement<?>... children) {
    if (children != null) {
      for (GuiElement<?> child : children) {
        if (child.parent != null) {
          child.parent.children.remove(child);
          child.parent.invalidateLayout();
        }

        child.parent = this;
        child.invalidateLayout();
        this.children.add(child);
      }

      this.invalidateLayout();
    }

    return this;
  }

  public boolean isLayoutChanged() {
    return isLayoutChanged;
  }

  public void invalidateLayout() {
    isLayoutChanged = true;
  }

  protected void renderChildren(Renderer renderer) {
    for (var child : children) {
      child.render(renderer);
    }
  }

  protected AssetLoaderProvider getAssetLoaderProvider() {
    return this.parent.getAssetLoaderProvider();
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
    for (var child : children) {
      child.onMousePressed(event);
    }

    if (event.isAbleToPropagate() && bounds.contains(event.getMouseEvent().x(), event.getMouseEvent().y())) {
      events.mousePressed().emit(event);
    }

//    if (bounds.contains(event.getMouseEvent().x(), event.getMouseEvent().y())) {
//      children.forEach(child -> child.onMousePressed(event));
//
//      if (event.isAbleToPropagate()) {
//        events.mousePressed().emit(event);
//      }
//    }
  }

  void onMouseReleased(GuiMouseEvent event) {
    for (var child : children) {
      child.onMouseReleased(event);
    }

    if (event.isAbleToPropagate() && bounds.contains(event.getMouseEvent().x(), event.getMouseEvent().y())) {
      events.mouseReleased().emit(event);
    }

//    if (bounds.contains(event.getMouseEvent().x(), event.getMouseEvent().y())) {
//      children.forEach(child -> child.onMouseReleased(event));
//
//      if (event.isAbleToPropagate()) {
//        events.mouseReleased().emit(event);
//      }
//    }
  }

  void onMouseMoved(GuiMouseEvent event) {
    for (var child : children) {
      child.onMouseMoved(event);
    }

    if (event.isAbleToPropagate()) {
      if (bounds.contains(event.getMouseEvent().x(), event.getMouseEvent().y())) {
        events.mouseMoved().emit(event);

        if (!isMouseInside) {
          isMouseInside = true;
          events.mouseEntered().emit(event);
        }
      } else if (isMouseInside) {
        isMouseInside = false;
        events.mouseExited().emit(event);
      }
    }

//    if (bounds.contains(event.getMouseEvent().x(), event.getMouseEvent().y())) {
//      children.forEach(child -> child.onMouseMoved(event));
//
//      if (event.isAbleToPropagate()) {
//        events.mouseMoved().emit(event);
//      }
//    }
  }

  void setBounds(GuiElementBounds bounds) {
    this.bounds = bounds;

    var styleContainer = getStyles();

    Border border = styleContainer.getPropertyValue(BaseStyles::border, Border.NONE);
    Padding padding = styleContainer.getPropertyValue(BaseStyles::padding, Padding.NONE);

    this.contentBounds = new GuiElementBounds(
      bounds.x() + border.left() + padding.left(),
      bounds.y() + border.top() + padding.top(),
      bounds.width() - (border.left() + border.right() + padding.left() + padding.right()),
      bounds.height() - (border.top() + border.bottom() + padding.top() + padding.bottom())
    );
  }

  void setContentBounds(GuiElementBounds contentBounds) {
    this.contentBounds = contentBounds;

    Border border = getStyles().getPropertyValue(BaseStyles::border, Border.NONE);
    Padding padding = getStyles().getPropertyValue(BaseStyles::padding, Padding.NONE);

    this.bounds = new GuiElementBounds(
      contentBounds.x() - border.left() - padding.left(),
      contentBounds.y() - border.top() - padding.top(),
      contentBounds.width() + border.left() + border.right() + padding.left() + padding.right(),
      contentBounds.height() + border.top() + border.bottom() + padding.top() + padding.bottom()
    );
  }

  GuiDocument getGuiDocument() {
    return this.parent.getGuiDocument();
  }

  private void recalculateLayout() {
    if (isLayoutChanged()) {
      LayoutUtil.rebuildLayout(this);

      isLayoutChanged = false;
    }
  }
}
