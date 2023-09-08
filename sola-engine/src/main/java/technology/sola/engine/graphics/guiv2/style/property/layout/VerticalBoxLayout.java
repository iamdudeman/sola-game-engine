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
    int x = guiElement.getBounds().x();
    int y = guiElement.getBounds().y();
    int autoHeight = 0;

    int xOffset = x + guiElement.getStyles().getPropertyValue(BaseStyles::border, Border.NONE).left();
    int yOffset = y + guiElement.getStyles().getPropertyValue(BaseStyles::border, Border.NONE).top();

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

//    throw new RuntimeException("Not yet implemented");


//    GuiElement<?> parent = guiElement.getParent();
//
//    int parentWidth = parent.getBounds().width();
//    int parentHeight = parent.getBounds().height();
//    StyleValue widthStyle = guiElement.getStyles().getPropertyValue(BaseStyles::width, StyleValue.AUTO);
//    StyleValue heightStyle = guiElement.getStyles().getPropertyValue(BaseStyles::height, StyleValue.AUTO);
//
//    int width = widthStyle == StyleValue.AUTO ? parentWidth : widthStyle.getValue(parentWidth);
//    int height = heightStyle == StyleValue.AUTO ? guiElement.getContentHeight() : heightStyle.getValue(parentHeight);
//
//    return new GuiElementBounds(parent.getBounds().x(), parent.getBounds().y(), width, height);
  }

  public record VerticalBoxLayoutInfo(int gap) {
  }
}
