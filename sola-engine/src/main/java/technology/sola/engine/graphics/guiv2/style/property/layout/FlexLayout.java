package technology.sola.engine.graphics.guiv2.style.property.layout;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.GuiElementBounds;

// todo
//   horizontalAlignment? (layout)
//   verticalAlignment? (layout)

public record FlexLayout(
  FlexLayout.FlexLayoutInfo info
) implements Layout<FlexLayout.FlexLayoutInfo> {
  @Override
  public LayoutType type() {
    return LayoutType.Flex;
  }

  @Override
  public GuiElementBounds calculateBounds(GuiElement<?> guiElement) {
    throw new RuntimeException("not yet implemented");
  }

  public record FlexLayoutInfo(Direction direction, int gap) {
  }

  public enum Direction {
    Column,
    Row,
  }
}
