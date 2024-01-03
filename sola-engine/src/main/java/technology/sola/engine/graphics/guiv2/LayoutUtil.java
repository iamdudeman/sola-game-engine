package technology.sola.engine.graphics.guiv2;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.property.*;

class LayoutUtil {
  static void rebuildLayout(GuiElement<?> guiElement) {
    var guiElementChildren = guiElement.children;
    var styles = guiElement.getStyles();
    var direction = styles.getPropertyValue(BaseStyles::direction, Direction.COLUMN);
    int gap = styles.getPropertyValue(BaseStyles::gap, 0);
    int xOffset = guiElement.boundConstraints.x();
    int yOffset = guiElement.boundConstraints.y();

    final int startIndex = switch (direction) {
      case ROW, COLUMN -> 0;
      case ROW_REVERSE, COLUMN_REVERSE -> guiElementChildren.size() - 1;
    };

    // main layout calculation loop
    for (int i = 0; i < guiElementChildren.size(); i++) {
      int index = startIndex > 0 ? startIndex - i : i;
      GuiElement<?> child = guiElementChildren.get(index);

      Visibility visibility = child.getStyles().getPropertyValue(BaseStyles::visibility, Visibility.VISIBLE);

      // If no visibility then clear out all bounds so no layout space is taken
      if (visibility == Visibility.NONE) {
        child.boundConstraints = new GuiElementBounds(0, 0, 0, 0);
        child.bounds = child.boundConstraints;
        child.contentBounds = child.bounds;
        continue;
      }

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

      if (!position.isAbsolute()) {
        switch (direction) {
          case COLUMN, COLUMN_REVERSE: {
            yOffset += child.getBounds().height() + gap;
            break;
          }
          case ROW, ROW_REVERSE: {
            xOffset += child.getBounds().width() + gap;
            break;
          }
        }
      }

      // child layout bounds have been adjusted
      child.isLayoutChanged = false;
    }
  }

  static void updateAbsolute(GuiElement<?> guiElement) {
    var position = guiElement.getStyles().getPropertyValue(BaseStyles::position, Position.NONE);

    if (position.isAbsolute()) {
      var parentElement = guiElement.getParent();

      int absoluteX = position.x() == null
        ? parentElement.contentBounds.x()
        : parentElement.bounds.x() + position.x().getValue(parentElement.bounds.width());
      int absoluteY = position.y() == null
        ? parentElement.contentBounds.y()
        : parentElement.bounds.y() + position.y().getValue(parentElement.bounds.height());

      guiElement.setPosition(absoluteX, absoluteY);
    }

    guiElement.children.forEach(LayoutUtil::updateAbsolute);
  }

  static void updateAlignment(GuiElement<?> guiElement) {
    var styles = guiElement.getStyles();

    // prepare to calculate alignment adjustments of elements in the flow
    var direction = styles.getPropertyValue(BaseStyles::direction, Direction.COLUMN);
    var mainAxisChildren = styles.getPropertyValue(BaseStyles::mainAxisChildren, MainAxisChildren.START);
    var crossAxisChildren = styles.getPropertyValue(BaseStyles::crossAxisChildren, CrossAxisChildren.START);

    // only need apply alignment if either axis is not START
    if (mainAxisChildren != MainAxisChildren.START || crossAxisChildren != CrossAxisChildren.START) {
      var guiElementChildrenInFlow = guiElement.children.stream()
        .filter(child -> !child.styleContainer.getPropertyValue(BaseStyles::position, Position.NONE).isAbsolute())
        .toList();

      AlignmentFunction alignmentFunction = switch (direction) {
        case ROW, ROW_REVERSE -> child -> {
          final int usedWidth = guiElementChildrenInFlow.stream().mapToInt(childInFlow -> childInFlow.bounds.width()).sum();

          return calculateRowChildAlignmentBounds(child, mainAxisChildren, crossAxisChildren, usedWidth);
        };
        case COLUMN, COLUMN_REVERSE -> child -> {
          final int usedHeight = guiElementChildrenInFlow.stream().mapToInt(childInFlow -> childInFlow.bounds.height()).sum();

          return calculateColumnChildAlignmentBounds(child, mainAxisChildren, crossAxisChildren, usedHeight);
        };
      };

      final int startIndex = switch (direction) {
        case ROW, COLUMN -> 0;
        case ROW_REVERSE, COLUMN_REVERSE -> guiElementChildrenInFlow.size() - 1;
      };

      // alignment adjustment loop
      for (int i = 0; i < guiElementChildrenInFlow.size(); i++) {
        int index = startIndex > 0 ? startIndex - i : i;
        GuiElement<?> child = guiElementChildrenInFlow.get(index);
        var boundsAfterAlignment = alignmentFunction.apply(child);

        child.resizeBounds(boundsAfterAlignment.width(), boundsAfterAlignment.height());
        child.setPosition(boundsAfterAlignment.x(), boundsAfterAlignment.y());
      }
    }

    guiElement.children.forEach(LayoutUtil::updateAlignment);
  }

  static void clearChildLayoutChanged(GuiElement<?> guiElement) {
    guiElement.isLayoutChanged = false;

    guiElement.children.forEach(LayoutUtil::clearChildLayoutChanged);
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
      case CENTER -> (child.getParent().contentBounds.width() - usedWidth) / 2;
      case END -> child.getParent().contentBounds.width() - usedWidth;
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
      case CENTER -> (child.getParent().contentBounds.height() - usedHeight) / 2;
      case END -> child.getParent().contentBounds.height() - usedHeight;
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
    var styles = guiElement.getStyles();
    boolean isAutoWidth = styles.getPropertyValue(BaseStyles::width) == null;
    boolean isAutoHeight = styles.getPropertyValue(BaseStyles::height) == null;

    // If no custom content dimensions then size based on children
    if (contentDimensions == null) {
      int gap = styles.getPropertyValue(BaseStyles::gap, 0);
      var direction = styles.getPropertyValue(BaseStyles::direction, Direction.COLUMN);
      var childrenInLayout = guiElement.children.stream()
        .filter(child -> !child.getStyles().getPropertyValue(BaseStyles::position, Position.NONE).isAbsolute())
        .toList();

      if (direction == Direction.ROW || direction == Direction.ROW_REVERSE) {
        contentDimensions = new GuiElementDimensions(
          isAutoWidth
            ? childrenInLayout.stream().mapToInt(child -> child.bounds.width()).sum() + gap * childrenInLayout.size()
            : guiElement.contentBounds.width(),
          isAutoHeight
            ? childrenInLayout.stream().mapToInt(child -> child.bounds.height()).max().orElse(0)
            : guiElement.contentBounds.height()
        );
      } else {
        contentDimensions = new GuiElementDimensions(
          isAutoWidth
            ? childrenInLayout.stream().mapToInt(child -> child.bounds.width()).max().orElse(0)
            : guiElement.contentBounds.width(),
          isAutoHeight
            ? childrenInLayout.stream().mapToInt(child -> child.bounds.height()).sum() + gap * childrenInLayout.size()
            : guiElement.contentBounds.height()
        );
      }

      // fill available space if there are no children
      if (contentDimensions.width() == 0) {
        contentDimensions = new GuiElementDimensions(
          guiElement.contentBounds.width(), contentDimensions.height()
        );
      }
      if (contentDimensions.height() == 0) {
        contentDimensions = new GuiElementDimensions(
          contentDimensions.width(), guiElement.contentBounds.height()
        );
      }
    } else {
      contentDimensions = new GuiElementDimensions(
        isAutoWidth ? contentDimensions.width() : guiElement.contentBounds.width(),
        isAutoHeight ? contentDimensions.height() : guiElement.contentBounds.height()
      );
    }

    guiElement.resizeContent(contentDimensions.width(), contentDimensions.height());
  }

  @FunctionalInterface
  interface AlignmentFunction {
    GuiElementBounds apply(GuiElement<?> child);
  }
}
