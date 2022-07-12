package technology.sola.engine.graphics.gui.element.properties;

public class GuiElementProperties {
  public final GuiElementBounds padding;
  public final GuiElementBounds margin;
  public int layer;
  public boolean isHidden;

  public GuiElementProperties() {
    padding = new GuiElementBounds();
    margin = new GuiElementBounds();
  }

  public GuiElementProperties(GuiElementBounds padding, GuiElementBounds margin, int layer, boolean isHidden) {
    this.padding = padding;
    this.margin = margin;
    this.layer = layer;
    this.isHidden = isHidden;
  }
}
