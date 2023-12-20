package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.property.*;

// todo absolute positioning

class LayoutUtil {
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

    int usedWidth = 0;
    int usedHeight = 0;

    for (int i = 0; i < guiElement.children.size(); i++) {
      int index = startIndex > 0 ? startIndex - i : i;
      GuiElement<?> child = guiElement.children.get(index);

      child.boundConstraints = recalculateBoundConstraints(child, xOffset, yOffset);

      rebuildLayout(child);

      child.setBounds(child.calculateBounds(child.boundConstraints));

      switch (direction) {
        case COLUMN:
        case COLUMN_REVERSE:
          yOffset += child.getBounds().height() + gap;
          usedHeight = yOffset;
          break;
        case ROW:
        case ROW_REVERSE:
          xOffset += child.getBounds().width() + gap;
          usedWidth = xOffset;
          break;
      }
    }

    var mainAxisChildren = styles.getPropertyValue(BaseStyles::mainAxisChildren, MainAxisChildren.START);
    var crossAxisChildren = styles.getPropertyValue(BaseStyles::crossAxisChildren, CrossAxisChildren.START);

    if (mainAxisChildren != MainAxisChildren.START || crossAxisChildren != CrossAxisChildren.START) {
      for (int i = 0; i < guiElement.children.size(); i++) {
        int index = startIndex > 0 ? startIndex - i : i;
        GuiElement<?> child = guiElement.children.get(index);

        int xAlignment = switch (mainAxisChildren) {
          case START -> 0;
          case CENTER -> (child.boundConstraints.width() - usedWidth) / 2;
          case END -> child.boundConstraints.width() - usedWidth;
        };
        int yAlignment = switch (crossAxisChildren) {
          case START, STRETCH -> 0;
          case CENTER -> (child.boundConstraints.height() - child.bounds.height()) / 2;
          case END -> child.boundConstraints.height() - child.bounds.height();
        };

        int newX = child.bounds.x();
        int newY = child.bounds.y();

        if (xAlignment > 0) {
          newX += xAlignment;
        }

        if (yAlignment > 0) {
          newY += yAlignment;
        }

        child.setBounds(child.bounds.setPosition(newX, newY));

        if (crossAxisChildren == CrossAxisChildren.STRETCH) {
          child.setBounds(child.bounds.setDimensions(child.bounds.width(), child.boundConstraints.height()));
        }
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
