package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.property.Border;
import technology.sola.engine.graphics.guiv2.style.property.Padding;
import technology.sola.engine.graphics.guiv2.style.property.StyleValue;

class LayoutUtils {
  static void rebuildLayout(GuiElement<?> guiElement, int x, int y) {
    GuiElementBounds bounds = calculateMaxBounds(guiElement, x, y);
    guiElement.bounds = bounds;
    GuiElementBounds contentBounds = calculateContentBounds(guiElement);
    guiElement.contentBounds = contentBounds;

    Dimensions childDimensions = calculateChildDimensionsForElement(guiElement);

    resizeAfterChildrenUpdates(guiElement, childDimensions);
  }

  private static GuiElementBounds calculateMaxBounds(GuiElement<?> guiElement, int x, int y) {
    int width;
    int height;
    var styleContainer = guiElement.getStyles();
    StyleValue heightStyle = styleContainer.getPropertyValue(BaseStyles::height, StyleValue.AUTO);
    StyleValue widthStyle = styleContainer.getPropertyValue(BaseStyles::width, StyleValue.AUTO);

    if (widthStyle == StyleValue.AUTO) {
      width = guiElement.getParent().getContentBounds().width();
    } else {
      Border border = styleContainer.getPropertyValue(BaseStyles::border, Border.NONE);
      Padding padding = styleContainer.getPropertyValue(BaseStyles::padding, Padding.NONE);
      int parentWidth = guiElement.getParent().getContentBounds().width();

      width = widthStyle.getValue(parentWidth)
        - border.left() - border.right()
        - padding.left().getValue(parentWidth) - padding.right().getValue(parentWidth);
    }

    if (heightStyle == StyleValue.AUTO) {
      height = guiElement.getParent().getContentBounds().height();
    } else {
      Border border = styleContainer.getPropertyValue(BaseStyles::border, Border.NONE);
      Padding padding = styleContainer.getPropertyValue(BaseStyles::padding, Padding.NONE);
      int parentHeight = guiElement.getParent().getContentBounds().height();

      height = heightStyle.getValue(parentHeight)
        - border.top() - border.bottom()
        - padding.top().getValue(parentHeight) - padding.bottom().getValue(parentHeight);
    }

    return new GuiElementBounds(x, y, width, height);
  }

  private static Dimensions calculateChildDimensionsForElement(GuiElement<?> guiElement) {

    int x = guiElement.getContentBounds().x();
    int y = guiElement.getContentBounds().y();
    int autoHeight = 0;

    int xOffset = x;
    int yOffset = y;

    int gap = guiElement.getStyles().getPropertyValue(BaseStyles::gap, 0);

    for (GuiElement<?> child : guiElement.children) {
      rebuildLayout(child, xOffset, yOffset);

      int childHeight = child.getContentBounds().height();
      yOffset += childHeight + gap;
      autoHeight = yOffset;
    }

    int newWidth = guiElement.getBounds().width();

    return new Dimensions(newWidth, autoHeight);
  }

  private static void resizeAfterChildrenUpdates(GuiElement<?> guiElement, Dimensions childDimensions) {
    // if auto shrink to max of child size or content size
    var styleContainer = guiElement.getStyles();
    StyleValue heightStyle = styleContainer.getPropertyValue(BaseStyles::height, StyleValue.AUTO);
    StyleValue widthStyle = styleContainer.getPropertyValue(BaseStyles::width, StyleValue.AUTO);

    int newWidth = guiElement.getBounds().width();
    int newHeight = guiElement.getBounds().height();

    if (heightStyle == StyleValue.AUTO) {
      Border border = guiElement.getStyles().getPropertyValue(BaseStyles::border, Border.NONE);
      Padding padding = guiElement.getStyles().getPropertyValue(BaseStyles::padding, Padding.NONE);
      int parentHeight = guiElement.getParent().getContentBounds().height();

      newHeight = childDimensions.height() + border.top() + border.bottom() + padding.bottom().getValue(parentHeight);
    }

    guiElement.bounds = guiElement.bounds.setDimensions(newWidth, newHeight);
    guiElement.contentBounds = calculateContentBounds(guiElement);
  }

  private static GuiElementBounds calculateContentBounds(GuiElement<?> guiElement) {
    var styleContainer = guiElement.getStyles();
    var parent = guiElement.getParent();
    var bounds = guiElement.getBounds();

    Border border = styleContainer.getPropertyValue(BaseStyles::border, Border.NONE);
    Padding padding = styleContainer.getPropertyValue(BaseStyles::padding, Padding.NONE);

    return new GuiElementBounds(
      bounds.x() + border.left() + padding.left().getValue(parent.getWidth()),
      bounds.y() + border.top() + padding.top().getValue(parent.getHeight()),
      bounds.width() - (border.left() + border.right() + padding.left().getValue(parent.getWidth()) + padding.right().getValue(parent.getWidth())),
      bounds.height() - (border.top() + border.bottom() + padding.top().getValue(parent.getHeight()) + padding.bottom().getValue(parent.getHeight()))
    );
  }
}
