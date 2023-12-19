package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.property.Border;
import technology.sola.engine.graphics.guiv2.style.property.Direction;
import technology.sola.engine.graphics.guiv2.style.property.Padding;
import technology.sola.engine.graphics.guiv2.style.property.StyleValue;

// todo main and cross axis alignment
// todo absolute positioning

class LayoutUtil {
  // todo should depend on direction and such
  static void rebuildLayout(GuiElement<?> guiElement) {
    int x = guiElement.boundConstraints.x();
    int y = guiElement.boundConstraints.y();

    int xOffset = x;
    int yOffset = y;

    var styles = guiElement.getStyles();

    Direction direction = styles.getPropertyValue(BaseStyles::direction, Direction.COLUMN);
    int gap = styles.getPropertyValue(BaseStyles::gap, 0);

    int startIndex = switch (direction) {
      case ROW, COLUMN -> 0;
      case ROW_REVERSE, COLUMN_REVERSE -> guiElement.children.size() - 1;
    };

    for (int i = 0; i < guiElement.children.size(); i++) {
      int index = startIndex > 0 ? startIndex - i : i;
      GuiElement<?> child = guiElement.children.get(index);

      child.boundConstraints = recalculateBoundConstraints(child, xOffset, yOffset);

      rebuildLayout(child);

      child.setBounds(child.calculateBounds(child.boundConstraints));

      if (direction == Direction.COLUMN || direction == Direction.COLUMN_REVERSE) {
        yOffset += child.getBounds().height() + gap;
      } else {
        xOffset += child.getBounds().width() + gap;
      }
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
