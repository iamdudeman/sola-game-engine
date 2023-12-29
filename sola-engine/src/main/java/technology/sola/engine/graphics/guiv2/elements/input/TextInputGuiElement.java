package technology.sola.engine.graphics.guiv2.elements.input;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.GuiElementDimensions;
import technology.sola.engine.graphics.guiv2.elements.TextGuiElement;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.KeyboardLayout;

// todo placeholder text styling isn't hooked up
//   and composed TextGuiElement might not be proper implementation

public class TextInputGuiElement extends BaseInputGuiElement<TextInputStyles> {
  // props
  private String placeholder = "";
  private Integer maxLength;

  // internals
  private String value = "";
  private final StringBuilder valueBuilder = new StringBuilder();
  private boolean isShiftDown = false;
  private TextGuiElement textGuiElement;

  public TextInputGuiElement(TextInputStyles... styles) {
    textGuiElement = new TextGuiElement(styles);
    textGuiElement.setText(value.isEmpty() ? " " : value);

    super.appendChildren(textGuiElement);

    events().keyReleased().on(keyEvent -> {
      if (keyEvent.getKeyEvent().keyCode() == Key.SHIFT.getCode()) {
        isShiftDown = false;
      }
    });

    events().keyPressed().on(keyEvent -> {
      int keyCode = keyEvent.getKeyEvent().keyCode();
      int length = valueBuilder.length();
      boolean isLessThanMaxLength = maxLength == null || length < maxLength;

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
        } else if (KeyboardLayout.hasShift(keyCode)) {
          if (isShiftDown) {
            valueBuilder.append(KeyboardLayout.shift(character));
          } else {
            valueBuilder.append(character);
          }
        } else if (keyCode == Key.SPACE.getCode()) {
          valueBuilder.append(character);
        }
      }

      setValue(valueBuilder.toString());
    });
  }

  @Override
  public void renderContent(Renderer renderer) {
    renderChildren(renderer);
  }

  @Override
  public GuiElementDimensions calculateContentDimensions() {
    return null;
  }

  @Override
  public boolean isFocusable() {
    return true;
  }

  @Override
  public GuiElement<TextInputStyles> appendChildren(GuiElement<?>... children) {
    return this;
  }

  public String getPlaceholder() {
    return placeholder;
  }

  public void setPlaceholder(String placeholder) {
    this.placeholder = placeholder;

    if (value.isEmpty()) {
      textGuiElement.setText(placeholder);
    }
  }

  public Integer getMaxLength() {
    return maxLength;
  }

  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;

    if (maxLength != null && valueBuilder.length() > maxLength) {
      valueBuilder.delete(maxLength, valueBuilder.length());
      value = valueBuilder.toString();
      updateText();
    }
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    valueBuilder.delete(0, valueBuilder.length());

    if (maxLength != null && maxLength < value.length()) {
      valueBuilder.append(value, 0, maxLength);
    } else {
      valueBuilder.append(value);
    }

    this.value = valueBuilder.toString();
    updateText();
  }

  private void updateText() {
    if (value.isEmpty()) {
      textGuiElement.setText(placeholder.isEmpty() ? " " : placeholder);
    } else {
      textGuiElement.setText(value);
    }
  }
}
