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

      // todo handle space, dash, underscore
      // todo handle lowercase + shift

      if (keyCode == Key.SHIFT.getCode()) {
        isShiftDown = true;
      }

      if (keyCode == Key.BACKSPACE.getCode() && length > 0) {
        valueBuilder.deleteCharAt(length - 1);
      } else if (isLessThanMaxLength) {
        if (keyCode >= 65 && keyCode <= 90) {
          char character = (char) keyCode;

          if (isShiftDown) {
            valueBuilder.append(character);
          } else {
            valueBuilder.append(Character.toLowerCase(character));
          }
        } else if (keyCode >= 44 && keyCode <= 57) {
          if (isShiftDown) {
            valueBuilder.append(KeyboardLayout.shift((char) keyCode));
          } else {
            valueBuilder.append((char) keyCode);
          }
        } else if (Character.isWhitespace(keyCode)) {
          valueBuilder.append((char) keyCode);
        }
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

