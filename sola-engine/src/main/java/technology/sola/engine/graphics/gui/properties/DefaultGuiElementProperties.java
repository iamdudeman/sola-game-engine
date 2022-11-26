package technology.sola.engine.graphics.gui.properties;

public class DefaultGuiElementProperties extends GuiElementBaseProperties<GuiElementBaseHoverProperties> {
  public DefaultGuiElementProperties(GuiElementGlobalProperties globalProperties) {
    super(globalProperties, new GuiElementBaseHoverProperties());
  }
}
