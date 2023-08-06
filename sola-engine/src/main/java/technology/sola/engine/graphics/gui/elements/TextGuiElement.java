package technology.sola.engine.graphics.gui.elements;

import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.properties.GuiPropertyDefaults;

/**
 * TextGuiElement is a {@link technology.sola.engine.graphics.gui.GuiElement} for displaying text in a gui.
 */
public class TextGuiElement extends BaseTextGuiElement<TextGuiElement.Properties> {
  /**
   * Creates a TextGuiElement.
   *
   * @param document the {@link SolaGuiDocument}
   */
  public TextGuiElement(SolaGuiDocument document) {
    super(document, new Properties(document.propertyDefaults));
  }

  /**
   * Properties for {@link TextGuiElement}
   */
  public static class Properties extends BaseTextGuiElement.Properties {
    /**
     * Creates a {@link TextGuiElement} properties instance.
     *
     * @param propertyDefaults the {@link GuiPropertyDefaults}
     */
    public Properties(GuiPropertyDefaults propertyDefaults) {
      super(propertyDefaults);
    }
  }
}
