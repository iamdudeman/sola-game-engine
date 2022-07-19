package technology.sola.engine.graphics.gui.elements;

import technology.sola.engine.graphics.gui.GuiElementGlobalProperties;

public class TextGuiElement extends BaseTextGuiElement<TextGuiElement.Properties> {
  public TextGuiElement(Properties properties) {
    super(properties);
  }

  public static class Properties extends BaseTextGuiElement.Properties {
    public Properties(GuiElementGlobalProperties globalProperties) {
      super(globalProperties);
    }
  }
}
