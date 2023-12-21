package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.property.*;

class LayoutUtil {
  static void rebuildLayout(GuiElement<?> guiElement) {
    Visibility visibility = guiElement.getStyles().getPropertyValue(BaseStyles::visibility, Visibility.VISIBLE);

    // If no visibility then clear out all bounds so no layout space is taken
    if (visibility == Visibility.NONE) {
      guiElement.boundConstraints = new GuiElementBounds(0, 0, 0, 0);
      guiElement.bounds = guiElement.boundConstraints;
      guiElement.contentBounds = guiElement.bounds;
      return;
    }

    int xOffset = guiElement.boundConstraints.x();
    int yOffset = guiElement.boundConstraints.y();

    var styles = guiElement.getStyles();

    Direction direction = styles.getPropertyValue(BaseStyles::direction, Direction.COLUMN);
    int gap = styles.getPropertyValue(BaseStyles::gap, 0);
    var guiElementChildren = guiElement.children;

    int startIndex = switch (direction) {
      case ROW, COLUMN -> 0;
      case ROW_REVERSE, COLUMN_REVERSE -> guiElementChildren.size() - 1;
    };

    int usedWidthAccumulator = 0;
    int usedHeightAccumulator = 0;

    for (int i = 0; i < guiElementChildren.size(); i++) {
      int index = startIndex > 0 ? startIndex - i : i;
      GuiElement<?> child = guiElementChildren.get(index);

      // calculate constraints
      child.boundConstraints = recalculateBoundConstraints(child, xOffset, yOffset);

      // recursively build child layouts
      rebuildLayout(child);

      // update bounds after children layouts determined
      child.setBounds(calculateDefaultLayoutBounds(child));

      // update content bounds if it should shrink
      updateContentBounds(child);

      // calculate where in the layout flow the element should go or if it is outside the flow
      var position = child.getStyles().getPropertyValue(BaseStyles::position, Position.NONE);

      if (position.isAbsolute()) {
        int absoluteX = position.x() == null
          ? child.bounds.x()
          : position.x().getValue(child.boundConstraints.x() + child.boundConstraints.width());
        int absoluteY = position.y() == null
          ? child.bounds.y()
          : position.y().getValue(child.boundConstraints.y() + child.boundConstraints.height());

        child.setBounds(child.bounds.setPosition(absoluteX, absoluteY));
      } else {
        switch (direction) {
          case COLUMN, COLUMN_REVERSE: {
            yOffset += child.getBounds().height() + gap;
            usedHeightAccumulator = yOffset;
            break;
          }
          case ROW, ROW_REVERSE: {
            xOffset += child.getBounds().width() + gap;
            usedWidthAccumulator = xOffset;
            break;
          }
        }
      }
    }

    // calculate alignment of elements in the flow
    final int usedWidth = usedWidthAccumulator;
    final int usedHeight = usedHeightAccumulator;
    var mainAxisChildren = styles.getPropertyValue(BaseStyles::mainAxisChildren, MainAxisChildren.START);
    var crossAxisChildren = styles.getPropertyValue(BaseStyles::crossAxisChildren, CrossAxisChildren.START);

    AlignmentFunction alignmentFunction = switch (direction) {
      case ROW, ROW_REVERSE -> child -> calculateRowChildAlignmentBounds(child, mainAxisChildren, crossAxisChildren, usedWidth);
      case COLUMN, COLUMN_REVERSE -> child -> calculateColumnChildAlignmentBounds(child, mainAxisChildren, crossAxisChildren, usedHeight);
    };

    // only need apply alignment if either axis is not START
    if (mainAxisChildren != MainAxisChildren.START || crossAxisChildren != CrossAxisChildren.START) {
      for (int i = 0; i < guiElementChildren.size(); i++) {
        int index = startIndex > 0 ? startIndex - i : i;
        GuiElement<?> child = guiElementChildren.get(index);

        var position = child.getStyles().getPropertyValue(BaseStyles::position, Position.NONE);

        // If child is position absolute alignment is ignored
        if (!position.isAbsolute()) {
          child.setBounds(alignmentFunction.apply(child));
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

  private static GuiElementBounds calculateRowChildAlignmentBounds(GuiElement<?> child, MainAxisChildren mainAxisChildren, CrossAxisChildren crossAxisChildren, int usedWidth) {
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

    int newX = xAlignment > 0 ? child.bounds.x() + xAlignment : child.bounds.x();
    int newY = yAlignment > 0 ? child.bounds.y() + yAlignment : child.bounds.y();

    GuiElementBounds result = child.bounds.setPosition(newX, newY);

    if (crossAxisChildren == CrossAxisChildren.STRETCH) {
      result = result.setDimensions(child.bounds.width(), child.boundConstraints.height());
    }

    return result;
  }

  private static GuiElementBounds calculateColumnChildAlignmentBounds(GuiElement<?> child, MainAxisChildren mainAxisChildren, CrossAxisChildren crossAxisChildren, int usedHeight) {
    int xAlignment = switch (crossAxisChildren) {
      case START, STRETCH -> 0;
      case CENTER -> (child.boundConstraints.width() - child.bounds.width()) / 2;
      case END -> child.boundConstraints.width() - child.bounds.width();
    };
    int yAlignment = switch (mainAxisChildren) {
      case START -> 0;
      case CENTER -> (child.boundConstraints.height() - usedHeight) / 2;
      case END -> child.boundConstraints.height() - usedHeight;
    };

    int newX = xAlignment > 0 ? child.bounds.x() + xAlignment : child.bounds.x();
    int newY = yAlignment > 0 ? child.bounds.y() + yAlignment : child.bounds.y();

    GuiElementBounds result = child.bounds.setPosition(newX, newY);

    if (crossAxisChildren == CrossAxisChildren.STRETCH) {
      result = result.setDimensions(child.boundConstraints.width(), child.bounds.height());
    }

    return result;
  }

  private static GuiElementBounds calculateDefaultLayoutBounds(GuiElement<?> guiElement) {
    var styles = guiElement.getStyles();
    var boundConstraints = guiElement.boundConstraints;

    final int width = styles.getPropertyValue(BaseStyles::width, StyleValue.FULL).getValue(boundConstraints.width());
    final int widthBound = Math.min(width, boundConstraints.width());

    final int height = styles.getPropertyValue(BaseStyles::height, StyleValue.FULL).getValue(boundConstraints.height());
    final int heightBound = Math.min(height, boundConstraints.height());

    return new GuiElementBounds(boundConstraints.x(), boundConstraints.y(), widthBound, heightBound);
  }

  private static void updateContentBounds(GuiElement<?> guiElement) {
    var contentDimensions = guiElement.calculateContentDimensions();

    if (contentDimensions != null) {
      // If a width or height is set those should always "win out"
      contentDimensions = new GuiElementDimensions(
        guiElement.getStyles().getPropertyValue(BaseStyles::width) == null ? contentDimensions.width() : guiElement.contentBounds.width(),
        guiElement.getStyles().getPropertyValue(BaseStyles::height) == null ? contentDimensions.height() : guiElement.contentBounds.height()
      );

      guiElement.setContentBounds(guiElement.getContentBounds().setDimensions(contentDimensions));
    }
  }

  @FunctionalInterface
  interface AlignmentFunction {
    GuiElementBounds apply(GuiElement<?> child);
  }
}
