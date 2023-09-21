package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.graphics.guiv2.event.GuiElementEvents;
import technology.sola.engine.graphics.guiv2.event.GuiKeyEvent;
import technology.sola.engine.graphics.guiv2.event.GuiMouseEvent;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.StyleContainer;
import technology.sola.engine.graphics.guiv2.style.property.Background;
import technology.sola.engine.graphics.guiv2.style.property.Border;
import technology.sola.engine.graphics.guiv2.style.property.Padding;
import technology.sola.engine.graphics.guiv2.style.property.StyleValue;
import technology.sola.engine.graphics.guiv2.style.property.Visibility;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiElement<Style extends BaseStyles> {
  protected final StyleContainer<Style> styleContainer;
  protected GuiElementBounds bounds;
  protected GuiElementBounds contentBounds;
  GuiElement<?> parent;
  private final GuiElementEvents events = new GuiElementEvents();
  private final List<GuiElement<?>> children = new ArrayList<>();
  private boolean isLayoutChanged;
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

  public abstract int getContentWidth();

  public abstract int getContentHeight();

  // todo is this needed or will bounds/contentBounds work?
  public int getWidth() {
    StyleValue widthStyle = styleContainer.getPropertyValue(BaseStyles::width, StyleValue.AUTO);

    if (widthStyle != StyleValue.AUTO) {
      return widthStyle.getValue(parent.getWidth());
    }

    Border border = getStyles().getPropertyValue(BaseStyles::border, Border.NONE);
    Padding padding = getStyles().getPropertyValue(BaseStyles::padding, Padding.NONE);

    int borderSize = border.left() + border.right();
    int paddingSize = padding.left().getValue(parent.getWidth()) + padding.right().getValue(parent.getWidth());
//    int paddingSize = 0;

    return getContentWidth() + borderSize + paddingSize;
  }

  // todo is this needed or will bounds/contentBounds work?
  public int getHeight() {
    StyleValue heightStyle = styleContainer.getPropertyValue(BaseStyles::height, StyleValue.AUTO);

    if (heightStyle != StyleValue.AUTO) {
      return heightStyle.getValue(parent.getHeight());
    }

    Border border = getStyles().getPropertyValue(BaseStyles::border, Border.NONE);
    Padding padding = getStyles().getPropertyValue(BaseStyles::padding, Padding.NONE);

    int borderSize = border.top() + border.bottom();
    int paddingSize = padding.top().getValue(parent.getHeight()) + padding.bottom().getValue(parent.getHeight());
//    int paddingSize = 0;

    return getContentHeight() + borderSize + paddingSize;
  }

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

    // content bounds
//    Spacing padding = styleContainer.getPropertyValue(BaseStyles::padding, Spacing.NONE);
//
//    x += padding.left().getValue(parent.getBounds().width()) + border.left();
//    y += padding.top().getValue(parent.getBounds().height()) + border.top();
//    width -= padding.right().getValue(parent.getBounds().width()) + border.right();
//    height -= padding.bottom().getValue(parent.getBounds().height()) + border.bottom();

    renderContent(renderer);
  }

  public void renderChildren(Renderer renderer) {
    children.forEach(child -> child.render(renderer));
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
        if (child.parent == null) {
//          child.guiDocument = this.guiDocument;
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

  public boolean isLayoutChanged() {
    return isLayoutChanged;
  }

  protected void recalculateLayout() {
    if (!isLayoutChanged()) {
      return;
    }

    // set bounds based on specific values
    var parentBounds = parent.getContentBounds();
    var widthStyle = styleContainer.getPropertyValue(BaseStyles::width, StyleValue.AUTO);
    var heightStyle = styleContainer.getPropertyValue(BaseStyles::height, StyleValue.AUTO);

    // todo need to get parent CONTENT bounds instead (bounds - borders - padding)
    // todo probably need method to get initial element bounds from layout as well?
    // initial bounds (grows to parent size if auto temporarily so children have space to use)
    bounds = new GuiElementBounds(
      bounds.x(), bounds.y(),
      widthStyle == StyleValue.AUTO ? parentBounds.width() : widthStyle.getValue(parentBounds.width()),
      heightStyle == StyleValue.AUTO ? parentBounds.height() : heightStyle.getValue(parentBounds.height())
    );
    recalculateContentBounds();

    // final bounds (shrinks to children content size if auto)
    bounds = tempCalcLayoutStuff(this);
    recalculateContentBounds();

    isLayoutChanged = false;
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

  private void recalculateContentBounds() {
    Border border = styleContainer.getPropertyValue(BaseStyles::border, Border.NONE);
    Padding padding = styleContainer.getPropertyValue(BaseStyles::padding, Padding.NONE);

    contentBounds = new GuiElementBounds(
      bounds.x() + border.left() + padding.left().getValue(parent.getWidth()),
      bounds.y() + border.top() + padding.top().getValue(parent.getHeight()),
      bounds.width() - (border.left() + border.right() + padding.left().getValue(parent.getWidth()) + padding.right().getValue(parent.getWidth())),
      bounds.height() - (border.top() + border.bottom() + padding.top().getValue(parent.getHeight()) + padding.bottom().getValue(parent.getHeight()))
    );
  }

  private static void setElementPosition(GuiElement<?> guiElement, int x, int y) {
    guiElement.x = x;
    guiElement.y = y;
    guiElement.bounds = new GuiElementBounds(guiElement.x, guiElement.y, guiElement.bounds.width(), guiElement.bounds.height());
//    guiElement.recalculateContentBounds();
    guiElement.recalculateLayout();
  }

  private GuiElementBounds tempCalcLayoutStuff(GuiElement<?> guiElement) {
    int x = guiElement.getContentBounds().x();
    int y = guiElement.getContentBounds().y();
    int autoHeight = 0;

    int xOffset = x;
    int yOffset = y;

    int gap = guiElement.getStyles().getPropertyValue(BaseStyles::gap, 0);

    for (GuiElement<?> child : guiElement.children) {
      setElementPosition(child, xOffset, yOffset);
//      updateGuiElementPosition.update(child, xOffset, yOffset);

      int childHeight = child.getContentBounds().height();
      yOffset += childHeight + gap;
      autoHeight = yOffset;
    }

    int newWidth = guiElement.getBounds().width();
    int newHeight = guiElement.getBounds().height();

    if (guiElement.getStyles().getPropertyValue(BaseStyles::height, StyleValue.AUTO) == StyleValue.AUTO) {
      newHeight = autoHeight + guiElement.getStyles().getPropertyValue(BaseStyles::border, Border.NONE).top() + guiElement.getStyles().getPropertyValue(BaseStyles::border, Border.NONE).bottom() + guiElement.getStyles().getPropertyValue(BaseStyles::padding, Padding.NONE).bottom().getValue(guiElement.getParent().getContentBounds().height());
    }

    return new GuiElementBounds(guiElement.getBounds().x(), guiElement.getBounds().y(), newWidth, newHeight);
  }
}
