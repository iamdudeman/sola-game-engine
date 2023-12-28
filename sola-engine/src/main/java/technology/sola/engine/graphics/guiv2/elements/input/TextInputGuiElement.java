package technology.sola.engine.graphics.guiv2.elements.input;

import technology.sola.engine.graphics.guiv2.elements.TextGuiElement;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.KeyboardLayout;

// todo placeholder text styling

public class TextInputGuiElement extends TextGuiElement {
  // props
  private String placeHolderText = "";
  private Integer maxLength;

  // internals
  private String value = "";
  private final StringBuilder valueBuilder = new StringBuilder();
  private boolean isShiftDown = false;

  public TextInputGuiElement() {
    setText(value.isEmpty() ? " " : value);

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

  public String getPlaceHolderText() {
    return placeHolderText;
  }

  public void setPlaceHolderText(String placeHolderText) {
    this.placeHolderText = placeHolderText;
  }

  public Integer getMaxLength() {
    return maxLength;
  }

  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
    updateText();
  }

  private void updateText() {
    if (value.isEmpty()) {
      setText(placeHolderText.isEmpty() ? " " : placeHolderText);
    } else {
      setText(value);
    }
  }
}
