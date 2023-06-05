package technology.sola.engine.graphics.gui.elements.form;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.BaseTextGuiElement;
import technology.sola.engine.graphics.gui.properties.GuiElementGlobalProperties;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;

public class TextInputGuiElement extends BaseTextGuiElement<TextInputGuiElement.Properties> {
  private final StringBuilder valueBuilder = new StringBuilder();

  public TextInputGuiElement(SolaGuiDocument solaGuiDocument) {
    super(solaGuiDocument, new Properties(solaGuiDocument.globalProperties));

    setOnMouseUpCallback(mouseEvent -> requestFocus());

    setOnKeyPressCallback(guiKeyEvent -> {
      int keyCode = guiKeyEvent.getKeyCode();
      int length = valueBuilder.length();
      boolean isLessThanMaxLength = properties().maxLength == null || length < properties().maxLength;

      // todo handle space, dash, underscore
      // todo handle lowercase + shift

      if (keyCode == Key.BACKSPACE.getCode() && length > 0) {
        valueBuilder.deleteCharAt(length - 1);
      } else if (keyCode >= 65 && keyCode <= 90 && isLessThanMaxLength) {
        char character = (char) keyCode;

        valueBuilder.append(character);
      }

      properties.setValue(valueBuilder.toString());
    });
  }

  @Override
  public void renderSelf(Renderer renderer, int x, int y) {
    if (isFocussed()) {
      properties.setBorderColor(Color.LIGHT_BLUE);
    } else {
      properties.setBorderColor(Color.WHITE);
    }

    super.renderSelf(renderer, x, y);
  }

  public static class Properties extends BaseTextGuiElement.Properties {
    private String label = "";
    private String value = "";
    private Integer maxLength;

    public Properties(GuiElementGlobalProperties globalProperties) {
      super(globalProperties);
      setFocusable(true);
      setBorderColor(Color.WHITE);
      setBackgroundColor(Color.BLACK);
      hover.setBackgroundColor(new Color(150, 40, 40, 40));
      padding.set(4);
    }

    public Properties setLabel(String label) {
      this.label = label;
      setText(label + ": " + value);

      return this;
    }

    public Properties setValue(String value) {
      this.value = value;
      setText(label + ": " + value);

      return this;
    }

    public String getValue() {
      return value;
    }

    public Integer getMaxLength() {
      return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
      this.maxLength = maxLength;
    }
  }
}

