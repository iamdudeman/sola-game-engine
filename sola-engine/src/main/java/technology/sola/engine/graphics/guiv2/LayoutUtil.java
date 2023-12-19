package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.property.Border;
import technology.sola.engine.graphics.guiv2.style.property.Padding;
import technology.sola.engine.graphics.guiv2.style.property.StyleValue;

// todo Direction
// todo main and cross axis alignment
// todo absolute positioning

class LayoutUtil {
  // todo should depend on direction and such
  static void rebuildLayout(GuiElement<?> guiElement) {
    int x = guiElement.boundConstraints.x();
    int y = guiElement.boundConstraints.y();

    int xOffset = x;
    int yOffset = y;

    int gap = guiElement.getStyles().getPropertyValue(BaseStyles::gap, 0);

    for (GuiElement<?> child : guiElement.children) {
      child.boundConstraints = recalculateBoundConstraints(child, xOffset, yOffset);
      System.out.println(child.boundConstraints);
      rebuildLayout(child);
      child.setBounds(child.calculateBounds(child.boundConstraints));

      int childHeight = child.getBounds().height();

      yOffset += childHeight + gap;
    }
  }

  private static GuiElementBounds recalculateBoundConstraints(GuiElement<?> guiElement, int x, int y) {
    var parentStyleContainer = guiElement.getParent().getStyles();
    var parentBorder = parentStyleContainer.getPropertyValue(BaseStyles::border, Border.NONE);
    var parentPadding = parentStyleContainer.getPropertyValue(BaseStyles::padding, Padding.NONE);

    // x + y constraints
    final int xConstraint = x + parentBorder.left() + parentPadding.left();
    final int yConstraint = y + parentBorder.top() + parentPadding.top();

    var parentBoundConstraints = guiElement.getParent().boundConstraints;
    var parentWidth = parentStyleContainer.getPropertyValue(BaseStyles::width, StyleValue.FULL);
    var parentHeight = parentStyleContainer.getPropertyValue(BaseStyles::height, StyleValue.FULL);

    // max width + height constraints
    final int widthConstraint = parentWidth.getValue(parentBoundConstraints.width()) - parentBorder.left() - parentBorder.right() - parentPadding.left() - parentPadding.right();
    final int heightConstraint = parentHeight.getValue(parentBoundConstraints.height()) - parentBorder.top() - parentBorder.bottom() - parentPadding.top() - parentPadding.bottom();

    // done!
    return new GuiElementBounds(xConstraint, yConstraint, widthConstraint, heightConstraint);
  }
}
