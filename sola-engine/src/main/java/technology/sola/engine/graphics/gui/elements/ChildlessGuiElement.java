package technology.sola.engine.graphics.gui.elements;

import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementProperties;

public abstract class ChildlessGuiElement<T extends GuiElementProperties> extends GuiElement<T> {
  public ChildlessGuiElement(T properties) {
    super(properties);
  }

  @Override
  public void addChild(GuiElement<?> guiElement) {
    // Nothing to do here
  }

  @Override
  public void recalculateChildPositions() {
    // Nothing to do here
  }
}
