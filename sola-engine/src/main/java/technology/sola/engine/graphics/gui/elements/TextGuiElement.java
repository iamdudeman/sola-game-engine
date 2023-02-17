package technology.sola.engine.graphics.gui.elements;

import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.properties.GuiElementGlobalProperties;

public class TextGuiElement extends BaseTextGuiElement<TextGuiElement.Properties> {
  public TextGuiElement(SolaGuiDocument document) {
    super(document, new Properties(document.globalProperties));
  }

  public static class Properties extends BaseTextGuiElement.Properties {
    public Properties(GuiElementGlobalProperties globalProperties) {
      super(globalProperties);
    }
  }
}
