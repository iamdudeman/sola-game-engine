package technology.sola.engine.graphics.gui.elements.form;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.BaseTextGuiElement;
import technology.sola.engine.graphics.gui.properties.GuiElementGlobalProperties;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.KeyboardLayout;

public class TextInputGuiElement extends BaseTextGuiElement<TextInputGuiElement.Properties> {
  private final StringBuilder valueBuilder = new StringBuilder();
  private boolean isShiftDown = false;

  public TextInputGuiElement(SolaGuiDocument solaGuiDocument) {
    super(solaGuiDocument, new Properties(solaGuiDocument.globalProperties));

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

  public static class Properties extends BaseTextGuiElement.Properties {
    private String placeholder = "";
    private String value = "";
    private Integer maxLength;
    private Color colorPlaceholderText = Color.LIGHT_GRAY;

    public Properties(GuiElementGlobalProperties globalProperties) {
      super(globalProperties);
      setFocusable(true);
      setFocusOutlineColor(Color.LIGHT_BLUE);

      setBorderColor(Color.WHITE);
      setBackgroundColor(Color.BLACK);
      hover.setBackgroundColor(new Color(150, 40, 40, 40));
      padding.set(4);
    }

    public void setColorPlaceholderText(Color colorPlaceholderText) {
      this.colorPlaceholderText = colorPlaceholderText;
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

    public void setMaxLength(Integer maxLength) {
      this.maxLength = maxLength;
    }
  }
}

