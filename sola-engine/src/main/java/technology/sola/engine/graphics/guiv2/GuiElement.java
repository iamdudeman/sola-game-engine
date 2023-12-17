package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.graphics.guiv2.event.GuiElementEvents;
import technology.sola.engine.graphics.guiv2.event.GuiKeyEvent;
import technology.sola.engine.graphics.guiv2.event.GuiMouseEvent;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.StyleContainer;
import technology.sola.engine.graphics.guiv2.style.property.Background;
import technology.sola.engine.graphics.guiv2.style.property.Border;
import technology.sola.engine.graphics.guiv2.style.property.Padding;
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
  protected GuiElementBounds contentBounds;
  final List<GuiElement<?>> children = new ArrayList<>();
  boolean isLayoutChanged;
  private GuiElement<?> parent;
  private final GuiElementEvents events = new GuiElementEvents();

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

  public abstract void onRecalculateLayout();

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

  public void setBounds(GuiElementBounds bounds) {
    this.bounds = bounds;

    var styleContainer = getStyles();
    var parent = getParent();
    var parentWidth = parent.getContentBounds().width();
    var parentHeight = parent.getContentBounds().height();

    Border border = styleContainer.getPropertyValue(BaseStyles::border, Border.NONE);
    Padding padding = styleContainer.getPropertyValue(BaseStyles::padding, Padding.NONE);

    this.contentBounds = new GuiElementBounds(
      bounds.x() + border.left() + padding.left().getValue(parentWidth),
      bounds.y() + border.top() + padding.top().getValue(parentHeight),
      bounds.width() - (border.left() + border.right() + padding.left().getValue(parentWidth) + padding.right().getValue(parentWidth)),
      bounds.height() - (border.top() + border.bottom() + padding.top().getValue(parentHeight) + padding.bottom().getValue(parentHeight))
    );
  }

  public void setContentBounds(GuiElementBounds contentBounds) {
    this.contentBounds = contentBounds;

    Border border = getStyles().getPropertyValue(BaseStyles::border, Border.NONE);
    Padding padding = getStyles().getPropertyValue(BaseStyles::padding, Padding.NONE);
    GuiElementBounds parentContentBounds = getParent().getContentBounds();

    this.bounds = new GuiElementBounds(
      contentBounds.x() - border.left() - padding.left().getValue(parentContentBounds.width()),
      contentBounds.y() - border.top() - padding.top().getValue(parentContentBounds.height()),
      contentBounds.width() + border.left() + border.right() + padding.left().getValue(parentContentBounds.width()) + padding.right().getValue(parentContentBounds.width()),
      contentBounds.height() + border.top() + border.bottom() + padding.top().getValue(parentContentBounds.height()) + padding.bottom().getValue(parentContentBounds.height())
    );
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

    // todo, this might need to be called on parent or higher depending on auto sizing of content

    LayoutUtil.rebuildLayout(this, getParent().contentBounds.x(), getParent().contentBounds.y());
  }
}
