package technology.sola.engine.graphics.gui.element.properties;

public class GuiElementProperties {
  private GuiElementBounds padding;
  private GuiElementBounds margin;
  private GuiElementBounds absolutePosition;
  private int zIndex;
  private boolean isHidden;

  public GuiElementProperties() {
    isHidden = false;
    zIndex = 0;
    padding = new GuiElementBounds();
    margin = new GuiElementBounds();
  }
}
