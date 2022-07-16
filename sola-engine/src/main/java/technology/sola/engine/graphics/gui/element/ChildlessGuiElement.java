package technology.sola.engine.graphics.gui.element;

public abstract class ChildlessGuiElement<T extends GuiElementBaseProperties> extends GuiElement<T> {
  @Override
  public void addChild(GuiElement<?> guiElement) {
    // Nothing to do here
  }

  @Override
  public void recalculateChildPositions() {
    // Nothing to do here
  }
}
