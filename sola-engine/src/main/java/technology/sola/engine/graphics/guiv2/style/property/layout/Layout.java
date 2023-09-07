package technology.sola.engine.graphics.guiv2.style.property.layout;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.GuiElementBounds;

// TODO consider VerticalLayout
// todo consider HorizontalLayout

public interface Layout<Info> {
  LayoutType type();

  Info info();

  GuiElementBounds calculateBounds(GuiElement<?> guiElement);
}
