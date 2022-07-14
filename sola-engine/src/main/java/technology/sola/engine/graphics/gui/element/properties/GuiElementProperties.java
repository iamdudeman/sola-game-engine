package technology.sola.engine.graphics.gui.element.properties;

public class GuiElementProperties {
  public final GuiElementBounds padding;
  public final GuiElementBounds margin;
  private boolean isHidden;

  public GuiElementProperties() {
    padding = new GuiElementBounds();
    margin = new GuiElementBounds();
  }

  public boolean isHidden() {
    return isHidden;
  }

  public void setHidden(boolean hidden) {
    isHidden = hidden;
  }
}
