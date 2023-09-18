package technology.sola.engine.graphics.guiv2.style.property.layout;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.GuiElementBounds;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.property.StyleValue;

import java.util.List;

public record BlockLayout(BlockLayoutInfo info) implements Layout<BlockLayout.BlockLayoutInfo> {
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
      yOffset += childHeight + info().gap;
      autoHeight = yOffset - info().gap;
    }

    int newWidth = guiElement.getBounds().width();
    int newHeight = guiElement.getBounds().height();

    if (guiElement.getStyles().getPropertyValue(BaseStyles::height, StyleValue.AUTO) == StyleValue.AUTO) {
      newHeight = autoHeight;
    }

    return new GuiElementBounds(guiElement.getBounds().x(), guiElement.getBounds().y(), newWidth, newHeight);
  }

  @Override
  public Layout<BlockLayoutInfo> mergeWith(Layout<BlockLayoutInfo> otherProperty) {
    return otherProperty == null ? this : otherProperty;
  }

  public record BlockLayoutInfo(int gap) {
  }
}
