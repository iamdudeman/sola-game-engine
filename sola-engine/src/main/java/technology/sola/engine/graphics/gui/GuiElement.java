package technology.sola.engine.graphics.gui;

import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.graphics.gui.event.GuiElementEvents;
import technology.sola.engine.graphics.gui.event.GuiKeyEvent;
import technology.sola.engine.graphics.gui.event.GuiMouseEvent;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.engine.graphics.gui.style.DefaultStyleValues;
import technology.sola.engine.graphics.gui.style.StyleContainer;
import technology.sola.engine.graphics.gui.style.property.*;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * GuiElement is the base class for all elements that can be rendered in a {@link GuiDocument}.
 *
 * @param <Style> the style type for the element
 * @param <ElementType> this element's type, so it can be used for method chaining
 */
public abstract class GuiElement<Style extends BaseStyles, ElementType extends GuiElement<Style, ElementType>> {
  /**
   * The {@link StyleContainer} for the element.
   */
  protected final StyleContainer<Style> styleContainer;
  /**
   * The list of children {@link GuiElement}s.
   */
  protected final List<GuiElement<?, ?>> children = new ArrayList<>();
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
  private GuiElement<?, ?> parent;
  private final GuiElementEvents events = new GuiElementEvents();
  private boolean isHovered = false;
  private boolean isActive = false;
  private String id;

  /**
   * Creates and initializes a new GuiElement instance.
   */
  protected GuiElement() {
    styleContainer = new StyleContainer<>(this);
    boundConstraints = new GuiElementBounds(0, 0, 0, 0);
    bounds = boundConstraints;
    contentBounds = bounds;
  }

  /**
   * Convenience method that calls {@link StyleContainer#setStyles(List)} for this element's {@link StyleContainer}.
   *
   * @param styles the styles to set
   * @return this
   */
  @SuppressWarnings("unchecked")
  public final ElementType setStyle(List<ConditionalStyle<Style>> styles) {
    styleContainer.setStyles(styles);

    return (ElementType) this;
  }

  /**
   * Method to render the main content of the gui element (not borders, backgrounds, etc.).
   *
   * @param renderer the {@link Renderer} instance
   */
  public abstract void renderContent(Renderer renderer);

  /**
   * Calculates content dimensions for this element. If its dimensions are derived from only its
   * children then it should return null.
   *
   * @return the dimensions of the content of this element or null if only children dimensions matter
   */
  public abstract GuiElementDimensions calculateContentDimensions();

  /**
   * Method to render the gui element. It handles rendering the background and border before then calling the
   * {@link GuiElement#renderContent(Renderer)} method.
   *
   * @param renderer the {@link Renderer} instance
   */
  public void render(Renderer renderer) {
    recalculateLayout();

    if (styleContainer.getPropertyValue(BaseStyles::visibility, DefaultStyleValues.VISIBILITY) != Visibility.VISIBLE) {
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
    Border border = styleContainer.getPropertyValue(BaseStyles::border, DefaultStyleValues.BORDER);

    // checking any border size for now since they will all be 0 or 1
    if (border.bottom() > 0) {
      renderer.drawRect(x, y, width - border.right(), height - border.bottom(), border.color());
    }

    renderContent(renderer);
  }

  /**
   * @return true if the element is currently hovered
   */
  public boolean isHovered() {
    return isHovered;
  }

  /**
   * The active state is for when an element is being interacted with (space key press or mouse pressed).
   *
   * @return true if element is currently active
   */
  public boolean isActive() {
    if (!isFocussed()) {
      setActive(false);
    }

    return isActive;
  }

  /**
   * @return true if element currently has keyboard focus
   */
  public boolean isFocussed() {
    return getGuiDocument().isFocussed(this);
  }

  /**
   * Focuses this element if it is able to have focus.
   */
  public void requestFocus() {
    if (isFocusable()) {
      getGuiDocument().requestFocus(this);
    }
  }

  /**
   * @return true if the element is currently focusable
   */
  public boolean isFocusable() {
    return styleContainer.getPropertyValue(BaseStyles::visibility, DefaultStyleValues.VISIBILITY) == Visibility.VISIBLE;
  }

  /**
   * @return the {@link GuiElementEvents} for the element
   */
  public GuiElementEvents events() {
    return events;
  }

  /**
   * @return the {@link StyleContainer} for the element
   */
  public StyleContainer<Style> styles() {
    return styleContainer;
  }

  /**
   * @return the parent element
   */
  public GuiElement<?, ?> getParent() {
    return parent;
  }

  /**
   * Gets the {@link GuiElementBounds} that contains the top, left and full width and height of the element.
   *
   * @return the bounds of the element
   */
  public GuiElementBounds getBounds() {
    return bounds;
  }

  /**
   * Gets the {@link GuiElementBounds} that contains the top, left of where the content area begins and the width and
   * height of the content area.
   *
   * @return the content bounds of the element
   */
  public GuiElementBounds getContentBounds() {
    return contentBounds;
  }

  /**
   * @return the id of the element
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the id of the element.
   *
   * @param id the new id
   * @return this
   */
  @SuppressWarnings("unchecked")
  public ElementType setId(String id) {
    this.id = id;

    return (ElementType) this;
  }

  /**
   * Searches the element tree for an element with the desired id. Returns null if not found.
   *
   * @param id           the id of the element to search for
   * @param elementClass the class of the element being searched
   * @param <T>          the element type
   * @return the element with the desired id or null if not found
   */
  public <T extends GuiElement<?, ?>> T findElementById(String id, Class<T> elementClass) {
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

  /**
   * Searches the element tree for elements with the desired type.
   *
   * @param elementClass the class of the elements being searched
   * @param <T>          the element type
   * @return the list of elements with the desired type
   */
  @SuppressWarnings("unchecked")
  public <T extends GuiElement<?, ?>> List<T> findElementsByType(Class<T> elementClass) {
    List<T> elements = new ArrayList<>();

    if (this.getClass().equals(elementClass)) {
      elements.add((T) this);
    }

    for (var child : children) {
      elements.addAll(child.findElementsByType(elementClass));
    }

    return elements;
  }

  /**
   * Removes a child element from this element.
   *
   * @param child the child element to remove
   * @return this
   */
  @SuppressWarnings("unchecked")
  public ElementType removeChild(GuiElement<?, ?> child) {
    if (children.contains(child)) {
      child.parent = null;
      children.remove(child);
      invalidateLayout();
    }

    return (ElementType) this;
  }

  /**
   * @return an immutable list of children GuiElements
   */
  public List<GuiElement<?, ?>> getChildren() {
    return Collections.unmodifiableList(children);
  }

  /**
   * Adds child {@link GuiElement}s to this element. How or if they will be rendered depends on the implementing element.
   *
   * @param children the child elements to add
   * @return this
   */
  @SuppressWarnings("unchecked")
  public ElementType appendChildren(GuiElement<?, ?>... children) {
    if (children != null) {
      for (GuiElement<?, ?> child : children) {
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

    return (ElementType) this;
  }

  /**
   * @return true if the layout of this element has been changed
   */
  public boolean isLayoutChanged() {
    return isLayoutChanged || children.stream().anyMatch(GuiElement::isLayoutChanged);
  }

  /**
   * Invalidates the layout for this element so that it will be recalculated next frame.
   */
  public void invalidateLayout() {
    isLayoutChanged = true;
  }

  /**
   * Renders all the element's children.
   *
   * @param renderer the {@link Renderer} instance
   */
  protected void renderChildren(Renderer renderer) {
    for (var child : children) {
      child.render(renderer);
    }
  }

  /**
   * @return the list of child elements that are focusable
   */
  protected List<GuiElement<?, ?>> getFocusableChildren() {
    return children.stream().filter(GuiElement::isFocusable).toList();
  }

  /**
   * @return the {@link AssetLoaderProvider} instance
   */
  protected AssetLoaderProvider getAssetLoaderProvider() {
    return this.parent.getAssetLoaderProvider();
  }

  /**
   * Utility method to search an array of elements for which one currently has focus and get its index.
   *
   * @param children the list of elements to search
   * @return the index of the element with focus or -1 if not found
   */
  protected int findFocussedChildIndex(List<GuiElement<?, ?>> children) {
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
      setActive(true);
    }

    events.keyPressed().emit(event);

    if (event.isAbleToPropagate() && parent != null) {
      parent.onKeyPressed(event);
    }
  }

  void onKeyReleased(GuiKeyEvent event) {
    events.keyReleased().emit(event);
    setActive(false);

    if (event.isAbleToPropagate() && parent != null) {
      parent.onKeyReleased(event);
    }
  }

  void onMousePressed(GuiMouseEvent event) {
    for (var child : children) {
      child.onMousePressed(event);
    }

    if (event.isAbleToPropagate() && shouldHandleMousePressedEvents() && bounds.contains(event.getMouseEvent())) {
      events.mousePressed().emit(event);
      setActive(true);
    }
  }

  void onMouseReleased(GuiMouseEvent event) {
    for (var child : children) {
      child.onMouseReleased(event);
    }

    if (event.isAbleToPropagate() && shouldHandleMouseReleasedEvents() && bounds.contains(event.getMouseEvent())) {
      events.mouseReleased().emit(event);
    }

    setActive(false);
  }

  void onMouseMoved(GuiMouseEvent event) {
    for (var child : children) {
      child.onMouseMoved(event);
    }

    if (event.isAbleToPropagate()) {
      if (shouldHandleMouseMoveEvents() && bounds.contains(event.getMouseEvent())) {
        events.mouseMoved().emit(event);

        if (!isHovered) {
          setHovered(true);
          events.mouseEntered().emit(event);
        }
      } else if (isHovered) {
        setHovered(false);
        events.mouseExited().emit(event);
      }
    }
  }

  void setBounds(GuiElementBounds bounds) {
    this.bounds = bounds;

    Border border = styleContainer.getPropertyValue(BaseStyles::border, DefaultStyleValues.BORDER);
    Padding padding = styleContainer.getPropertyValue(BaseStyles::padding, DefaultStyleValues.PADDING);

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
      Visibility visibility = styles().getPropertyValue(BaseStyles::visibility, DefaultStyleValues.VISIBILITY);

      // If no visibility then clear out all bounds so no layout space is taken
      if (visibility == Visibility.NONE) {
        boundConstraints = new GuiElementBounds(0, 0, 0, 0);
        bounds = boundConstraints;
        contentBounds = bounds;
        LayoutUtil.clearChildLayoutChanged(this);
      } else {
        // layouts must be built first before absolute positioning or alignment is applied
        LayoutUtil.rebuildLayout(this);
        LayoutUtil.updateAbsolute(this);
        LayoutUtil.updateAlignment(this);
        isLayoutChanged = false;
      }
    }
  }

  private void setContentBounds(GuiElementBounds contentBounds) {
    this.contentBounds = contentBounds;

    Border border = styleContainer.getPropertyValue(BaseStyles::border, DefaultStyleValues.BORDER);
    Padding padding = styleContainer.getPropertyValue(BaseStyles::padding, DefaultStyleValues.PADDING);

    this.bounds = new GuiElementBounds(
      contentBounds.x() - border.left() - padding.left(),
      contentBounds.y() - border.top() - padding.top(),
      contentBounds.width() + border.left() + border.right() + padding.left() + padding.right(),
      contentBounds.height() + border.top() + border.bottom() + padding.top() + padding.bottom()
    );
  }

  private void setActive(boolean isActive) {
    if (this.isActive != isActive) {
      this.isActive = isActive;

      styleContainer.invalidate();
    }
  }

  private void setHovered(boolean isHovered) {
    if (this.isHovered != isHovered) {
      this.isHovered = isHovered;

      styleContainer.invalidate();
    }
  }

  private boolean shouldHandleMouseMoveEvents() {
    return styleContainer.hasHoverCondition() || events.mouseMoved().hasListeners() || events.mouseEntered().hasListeners() || events.mouseExited().hasListeners();
  }

  private boolean shouldHandleMousePressedEvents() {
    return styleContainer.hasActiveCondition() || events().mousePressed().hasListeners();
  }

  private boolean shouldHandleMouseReleasedEvents() {
    return styleContainer.hasActiveCondition() || events().mouseReleased().hasListeners();
  }
}
