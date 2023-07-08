package technology.sola.engine.graphics.gui.elements.input;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.properties.GuiPropertyDefaults;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.KeyboardLayout;

public class TextInputGuiElement extends BaseInputGuiElement<TextInputGuiElement.Properties> {
  private final StringBuilder valueBuilder = new StringBuilder();
  private boolean isShiftDown = false;

  public TextInputGuiElement(SolaGuiDocument solaGuiDocument) {
    super(solaGuiDocument, new Properties(solaGuiDocument.propertyDefaults));

    setOnMouseUpCallback(mouseEvent -> requestFocus());

    setOnKeyReleaseCallback(guiKeyEvent -> {
      int keyCode = guiKeyEvent.getKeyCode();

      if (keyCode == Key.SHIFT.getCode()) {
        isShiftDown = false;
      }
    });

    setOnKeyPressCallback(guiKeyEvent -> {
      int keyCode = guiKeyEvent.getKeyCode();
      int length = valueBuilder.length();
      boolean isLessThanMaxLength = properties().maxLength == null || length < properties().maxLength;

      if (keyCode == Key.SHIFT.getCode()) {
        isShiftDown = true;
      }

      if (keyCode == Key.BACKSPACE.getCode() && length > 0) {
        valueBuilder.deleteCharAt(length - 1);
      } else if (isLessThanMaxLength) {
        char character = (char) keyCode;

        if (keyCode >= Key.A.getCode() && keyCode <= Key.Z.getCode()) {
          if (isShiftDown) {
            valueBuilder.append(character);
          } else {
            valueBuilder.append((char) (keyCode + 32));
          }
        } else if (hasShift(keyCode)) {
          if (isShiftDown) {
            valueBuilder.append(KeyboardLayout.shift(character));
          } else {
            valueBuilder.append(character);
          }
        } else if (keyCode == Key.SPACE.getCode()) {
          valueBuilder.append(character);
        }
      }

      properties.setValue(valueBuilder.toString());
    });
  }

  private boolean hasShift(int keyCode) {
    return (keyCode >= Key.COMMA.getCode() && keyCode <= Key.NINE.getCode())
      || keyCode == Key.SEMI_COLON.getCode()
      || keyCode == Key.EQUALS.getCode()
      || (keyCode >= Key.LEFT_BRACKET.getCode() && keyCode <= Key.RIGHT_BRACKET.getCode());
  }

  @Override
  public void renderSelf(Renderer renderer, int x, int y) {
    Color originalColorText = properties.getColorText();

    if (properties.value.isEmpty()) {
      properties.setColorText(properties.colorPlaceholderText);
    }

    super.renderSelf(renderer, x, y);

    properties.setColorText(originalColorText);
  }

  public static class Properties extends BaseInputGuiElement.Properties {
    private String placeholder = "";
    private String value = "";
    private Integer maxLength;
    private Color colorPlaceholderText;

    public Properties(GuiPropertyDefaults propertyDefaults) {
      super(propertyDefaults);

      // property defaults
      setColorPlaceholderText(propertyDefaults.colorPlaceholderText());
    }

    public Properties setColorPlaceholderText(Color colorPlaceholderText) {
      this.colorPlaceholderText = colorPlaceholderText;

      return this;
    }

    public Properties setPlaceholder(String placeholder) {
      this.placeholder = placeholder;

      if (value.isEmpty()) {
        setText(placeholder);
      }

      return this;
    }

    public Properties setValue(String value) {
      this.value = value;

      setText(value.isEmpty() ? placeholder : value);

      return this;
    }

    public String getValue() {
      return value;
    }

    public Integer getMaxLength() {
      return maxLength;
    }

    public Properties setMaxLength(Integer maxLength) {
      this.maxLength = maxLength;

      return this;
    }
  }
}

