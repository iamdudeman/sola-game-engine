package technology.sola.engine.graphics.guiv2.style.property.layout;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.GuiElementBounds;

public class InlineLayout implements Layout<InlineLayout.InlineLayoutInfo> {
  private final InlineLayoutInfo info = new InlineLayoutInfo();

  @Override
  public LayoutType type() {
    return LayoutType.Block;
  }

  @Override
  public InlineLayoutInfo info() {
    return info;
  }

  @Override
  public GuiElementBounds calculateBounds(GuiElement<?> guiElement) {
    throw new RuntimeException("not yet implemented");
  }

  public record InlineLayoutInfo() {
  }
}
