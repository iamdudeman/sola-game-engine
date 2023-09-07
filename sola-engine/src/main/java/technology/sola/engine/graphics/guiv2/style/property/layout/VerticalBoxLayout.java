package technology.sola.engine.graphics.guiv2.style.property.layout;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.GuiElementBounds;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.property.StyleValue;

public record VerticalBoxLayout(
  VerticalBoxLayoutInfo info
) implements Layout<VerticalBoxLayout.VerticalBoxLayoutInfo> {
  @Override
  public LayoutType type() {
    return LayoutType.VerticalBox;
  }

  @Override
  public GuiElementBounds calculateBounds(GuiElement<?> guiElement) {
    GuiElement<?> parent = guiElement.getParent();

    int parentWidth = parent.getBounds().width();
    int parentHeight = parent.getBounds().height();
    StyleValue widthStyle = guiElement.getStyles().getPropertyValue(BaseStyles::width, StyleValue.AUTO);
    StyleValue heightStyle = guiElement.getStyles().getPropertyValue(BaseStyles::height, StyleValue.AUTO);

    int width = widthStyle == StyleValue.AUTO ? parentWidth : widthStyle.getValue(parentWidth);
    int height = heightStyle == StyleValue.AUTO ? guiElement.getContentHeight() : heightStyle.getValue(parentHeight);

    return new GuiElementBounds(parent.getBounds().x(), parent.getBounds().y(), width, height);
  }

  public record VerticalBoxLayoutInfo(int gap) {
  }
}
