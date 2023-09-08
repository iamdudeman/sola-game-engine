package technology.sola.engine.graphics.guiv2.style.property.layout;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.GuiElementBounds;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.property.Spacing;
import technology.sola.engine.graphics.guiv2.style.property.StyleValue;

import java.util.List;

public record VerticalBoxLayout(VerticalBoxLayoutInfo info) implements Layout<VerticalBoxLayout.VerticalBoxLayoutInfo> {
  @Override
  public GuiElementBounds updateChildBounds(GuiElement<?> guiElement, List<GuiElement<?>> children, UpdateGuiElementPosition updateGuiElementPosition) {
    int x = guiElement.getContentBounds().x();
    int y = guiElement.getContentBounds().y();
    int autoHeight = 0;

    int xOffset = x;
    int yOffset = y;

    for (GuiElement<?> child : children) {
      Spacing margin = child.getStyles().getPropertyValue(BaseStyles::margin, Spacing.NONE);
      yOffset += margin.top().getValue(guiElement.getContentBounds().height());
      updateGuiElementPosition.update(child, xOffset + margin.left().getValue(guiElement.getContentBounds().width()), yOffset);

      int childHeight = child.getHeight();
      yOffset += childHeight;
      yOffset += margin.bottom().getValue(guiElement.getContentBounds().height());
      autoHeight = yOffset;
    }

    int newWidth = guiElement.getBounds().width();
    int newHeight = guiElement.getBounds().height();

    if (guiElement.getStyles().getPropertyValue(BaseStyles::height, StyleValue.AUTO) == StyleValue.AUTO) {
      newHeight = autoHeight;
    }
    if (guiElement.getStyles().getPropertyValue(BaseStyles::width, StyleValue.AUTO) == StyleValue.AUTO) {
      Spacing margin = guiElement.getStyles().getPropertyValue(BaseStyles::margin, Spacing.NONE);
      int offset = margin.left().getValue(guiElement.getParent().getContentBounds().width()) + margin.right().getValue(guiElement.getParent().getContentBounds().width());

      newWidth = newWidth - offset;
    }

    return new GuiElementBounds(guiElement.getBounds().x(), guiElement.getBounds().y(), newWidth, newHeight);
  }

  @Override
  public Layout<VerticalBoxLayoutInfo> mergeWith(Layout<VerticalBoxLayoutInfo> otherProperty) {
    return otherProperty == null ? this : otherProperty;
  }

  public record VerticalBoxLayoutInfo(int gap) {
  }
}
