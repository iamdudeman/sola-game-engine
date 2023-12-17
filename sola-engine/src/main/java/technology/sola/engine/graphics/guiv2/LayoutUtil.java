package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.property.StyleValue;

class LayoutUtil {
  static void rebuildLayout(GuiElement<?> guiElement, int x, int y) {
    guiElement.setBounds(calculateMaxBounds(guiElement, x, y));

    rebuildChildLayouts(guiElement);

    guiElement.onRecalculateLayout();

    guiElement.isLayoutChanged = false;
  }

  private static GuiElementBounds calculateMaxBounds(GuiElement<?> guiElement, int x, int y) {
    int width;
    int height;
    var styleContainer = guiElement.getStyles();
    StyleValue heightStyle = styleContainer.getPropertyValue(BaseStyles::height, StyleValue.FULL);
    StyleValue widthStyle = styleContainer.getPropertyValue(BaseStyles::width, StyleValue.FULL);

    int parentWidth = guiElement.getParent().getContentBounds().width();

    width = widthStyle.getValue(parentWidth);

    int parentHeight = guiElement.getParent().getContentBounds().height();

    height = heightStyle.getValue(parentHeight);

    return new GuiElementBounds(x, y, width, height);
  }

  // todo should depend on direction and such
  private static void rebuildChildLayouts(GuiElement<?> guiElement) {
    int x = guiElement.getContentBounds().x();
    int y = guiElement.getContentBounds().y();

    int xOffset = x;
    int yOffset = y;

    int gap = guiElement.getStyles().getPropertyValue(BaseStyles::gap, 0);

    for (GuiElement<?> child : guiElement.children) {
      rebuildLayout(child, xOffset, yOffset);

      int childHeight = child.getBounds().height();

      yOffset += childHeight + gap;
    }
  }
}
