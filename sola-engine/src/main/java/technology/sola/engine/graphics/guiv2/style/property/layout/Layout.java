package technology.sola.engine.graphics.guiv2.style.property.layout;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.GuiElementBounds;
import technology.sola.engine.graphics.guiv2.style.property.MergeableProperty;

import java.util.List;

// TODO consider VerticalLayout
// todo consider HorizontalLayout

public interface Layout<Info> extends MergeableProperty<Layout<Info>> {
  Info info();

  // todo rename this
  GuiElementBounds updateChildBounds(GuiElement<?> guiElement, List<GuiElement<?>> children, UpdateGuiElementPosition updateGuiElementPosition);

  // todo rename this
  @FunctionalInterface
  interface UpdateGuiElementPosition {
    void update(GuiElement<?> guiElement, int x, int y);
  }
}
