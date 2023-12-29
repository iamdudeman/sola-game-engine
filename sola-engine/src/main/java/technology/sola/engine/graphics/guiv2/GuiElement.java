package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.graphics.guiv2.event.GuiElementEvents;
import technology.sola.engine.graphics.guiv2.event.GuiKeyEvent;
import technology.sola.engine.graphics.guiv2.event.GuiMouseEvent;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.StyleContainer;
import technology.sola.engine.graphics.guiv2.style.property.*;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiElement<Style extends BaseStyles> {
  protected final StyleContainer<Style> styleContainer;
  protected final List<GuiElement<?>> children = new ArrayList<>();
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
  boolean isLayoutChanged;
  private GuiElement<?> parent;
  private final GuiElementEvents events = new GuiElementEvents();
  private boolean isHovered = false;
  private boolean isActive = false;
  private String id;

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
      // todo consider not having a default focus outline
      Border outline = styleContainer.getPropertyValue(BaseStyles::outline, Border.DEFAULT_FOCUS_OUTLINE);

      if (outline.bottom() > 0) {
        renderer.drawRect(x, y, width - outline.right(), height - outline.bottom(), outline.color());
      }
    }

    renderContent(renderer);
  }

  public boolean isHovered() {
    return isHovered;
  }

  public boolean isActive() {
    return isActive;
  }

  public boolean isFocussed() {
    return getGuiDocument().isFocussed(this);
  }

  public void requestFocus() {
    if (isFocusable()) {
      getGuiDocument().requestFocus(this);
    }
  }

  public boolean isFocusable() {
    return styleContainer.getPropertyValue(BaseStyles::visibility, Visibility.VISIBLE) == Visibility.VISIBLE;
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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public <T extends GuiElement<?>> T findElementById(String id, Class<T> elementClass) {
    if (id.equals(this.id)) {
      return elementClass.cast(this);
    }

    for (var child : children) {
      var childElement = child.findElementById(id, elementClass);

      if (childElement != null) {
        return childElement;
      }
    }

    return null;
  }

  public <T extends GuiElement<?>> List<T> findElementsByType(Class<T> elementClass) {
    List<T> elements = new ArrayList<>();

    if (this.getClass().equals(elementClass)) {
      elements.add((T) this);
    }

    for (var child : children) {
      elements.addAll(child.findElementsByType(elementClass));
    }

    return elements;
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
    return isLayoutChanged || children.stream().anyMatch(GuiElement::isLayoutChanged);
  }

  public void invalidateLayout() {
    isLayoutChanged = true;
  }

  protected void renderChildren(Renderer renderer) {
    for (var child : children) {
      child.render(renderer);
    }
  }

  protected List<GuiElement<?>> getFocusableChildren() {
    return children.stream().filter(GuiElement::isFocusable).toList();
  }

  protected AssetLoaderProvider getAssetLoaderProvider() {
    return this.parent.getAssetLoaderProvider();
  }

  protected int findFocussedChildIndex(List<GuiElement<?>> children) {
    int focussedIndex = 0;

    for (var child : children) {
      if (child.isFocussed()) {
        return focussedIndex;
      }

      int nestedChildValue = findFocussedChildIndex(child.children);

      if (nestedChildValue > -1) {
        return focussedIndex;
      }

      focussedIndex++;
    }

    return -1;
  }

  void onKeyPressed(GuiKeyEvent event) {
    if (event.getKeyEvent().keyCode() == Key.SPACE.getCode()) {
      isActive = true;
    }
    events.keyPressed().emit(event);

    if (event.isAbleToPropagate()) {
      parent.onKeyPressed(event);
    }
  }

  void onKeyReleased(GuiKeyEvent event) {
    events.keyReleased().emit(event);
    isActive = false;

    if (event.isAbleToPropagate()) {
      parent.onKeyReleased(event);
    }
  }

  void onMousePressed(GuiMouseEvent event) {
    for (var child : children) {
      child.onMousePressed(event);
    }

    if (event.isAbleToPropagate() && bounds.contains(event.getMouseEvent().x(), event.getMouseEvent().y())) {
      isActive = true;
      events.mousePressed().emit(event);
    }
  }

  void onMouseReleased(GuiMouseEvent event) {
    for (var child : children) {
      child.onMouseReleased(event);
    }

    if (event.isAbleToPropagate() && bounds.contains(event.getMouseEvent().x(), event.getMouseEvent().y())) {
      events.mouseReleased().emit(event);
      isActive = false;
    }
  }

  void onMouseMoved(GuiMouseEvent event) {
    for (var child : children) {
      child.onMouseMoved(event);
    }

    if (event.isAbleToPropagate()) {
      if (bounds.contains(event.getMouseEvent().x(), event.getMouseEvent().y())) {
        events.mouseMoved().emit(event);

        if (!isHovered) {
          isHovered = true;
          events.mouseEntered().emit(event);
        }
      } else if (isHovered) {
        isHovered = false;
        events.mouseExited().emit(event);
      }
    }
  }

  void setBounds(GuiElementBounds bounds) {
    this.bounds = bounds;

    Border border = styleContainer.getPropertyValue(BaseStyles::border, Border.NONE);
    Padding padding = styleContainer.getPropertyValue(BaseStyles::padding, Padding.NONE);

    this.contentBounds = new GuiElementBounds(
      bounds.x() + border.left() + padding.left(),
      bounds.y() + border.top() + padding.top(),
      bounds.width() - (border.left() + border.right() + padding.left() + padding.right()),
      bounds.height() - (border.top() + border.bottom() + padding.top() + padding.bottom())
    );
  }

  void setPosition(int x, int y) {
    int diffX = x - bounds.x();
    int diffY = y - bounds.y();

    if (diffX != 0 || diffY != 0) {
      setBounds(bounds.setPosition(x, y));

      for (var child : children) {
        child.setPosition(child.bounds.x() + diffX, child.bounds.y() + diffY);
      }
    }
  }

  void resizeBounds(int width, int height) {
    setBounds(bounds.setDimensions(width, height));
  }

  void resizeContent(int width, int height) {
    setContentBounds(contentBounds.setDimensions(width, height));
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

  private void setContentBounds(GuiElementBounds contentBounds) {
    this.contentBounds = contentBounds;

    Border border = styleContainer.getPropertyValue(BaseStyles::border, Border.NONE);
    Padding padding = styleContainer.getPropertyValue(BaseStyles::padding, Padding.NONE);

    this.bounds = new GuiElementBounds(
      contentBounds.x() - border.left() - padding.left(),
      contentBounds.y() - border.top() - padding.top(),
      contentBounds.width() + border.left() + border.right() + padding.left() + padding.right(),
      contentBounds.height() + border.top() + border.bottom() + padding.top() + padding.bottom()
    );
  }
}
