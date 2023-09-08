package technology.sola.engine.graphics.guiv2.style.property.layout;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.GuiElementBounds;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.property.Border;
import technology.sola.engine.graphics.guiv2.style.property.StyleValue;

import java.util.List;

// todo needs to respect margin

public record VerticalBoxLayout(VerticalBoxLayoutInfo info) implements Layout<VerticalBoxLayout.VerticalBoxLayoutInfo> {
  @Override
  public GuiElementBounds updateChildBounds(GuiElement<?> guiElement, List<GuiElement<?>> children, UpdateGuiElementPosition updateGuiElementPosition) {
    int x = guiElement.getContentBounds().x();
    int y = guiElement.getContentBounds().y();
    int autoHeight = 0;

    int xOffset = x;
    int yOffset = y;

    for (GuiElement<?> child : children) {
      updateGuiElementPosition.update(child, xOffset, yOffset);

      int childHeight = child.getHeight();
      yOffset += childHeight;
      autoHeight = yOffset;
    }


    if (guiElement.getStyles().getPropertyValue(BaseStyles::height, StyleValue.AUTO) == StyleValue.AUTO) {
      return new GuiElementBounds(x, y, guiElement.getWidth(), autoHeight);
    } else {
      return guiElement.getBounds();
    }
  }

  @Override
  public Layout<VerticalBoxLayoutInfo> mergeWith(Layout<VerticalBoxLayoutInfo> otherProperty) {
    return otherProperty == null ? this : otherProperty;
  }

  public record VerticalBoxLayoutInfo(int gap) {
  }
}
