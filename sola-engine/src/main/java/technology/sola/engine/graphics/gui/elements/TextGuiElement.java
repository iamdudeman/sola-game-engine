package technology.sola.engine.graphics.gui.elements;

import technology.sola.engine.core.module.graphics.gui.SolaGui;
import technology.sola.engine.graphics.gui.properties.GuiElementGlobalProperties;

public class TextGuiElement extends BaseTextGuiElement<TextGuiElement.Properties> {
  public TextGuiElement(SolaGui solaGui, Properties properties) {
    super(solaGui, properties);
  }

  public static class Properties extends BaseTextGuiElement.Properties {
    public Properties(GuiElementGlobalProperties globalProperties) {
      super(globalProperties);
    }
  }
}
