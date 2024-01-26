package technology.sola.engine.graphics.guiv2.elements.input;

import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.GuiElementDimensions;
import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.engine.graphics.guiv2.util.TextRenderDetails;
import technology.sola.engine.graphics.guiv2.util.TextRenderUtils;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.KeyboardLayout;

import java.util.List;

/**
 * TextInputGuiElement is a {@link BaseInputGuiElement} that allows users to interact via typing text into the field.
 */
public class TextInputGuiElement extends BaseInputGuiElement<TextInputStyles> {
  // props
  private String placeholder = "";
  private Integer maxLength;

  // internals
  private String value = "";
  private final StringBuilder valueBuilder = new StringBuilder();
  private boolean isShiftDown = false;
  private Font font = DefaultFont.get();
  private String currentFontId = DefaultFont.ASSET_ID;
  private TextRenderDetails textRenderDetails;
  private String text;

  /**
   * Creates an instance of TextInputGuiElement registering default event listeners for typing characters into the field.
   */
  public TextInputGuiElement() {
    super();

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
    Color textColor = getStyles().getPropertyValue(TextStyles::textColor, Color.BLACK);
    renderer.setFont(font);

    // Use placeholder color if present
    if (value.isEmpty() && !placeholder.isEmpty()) {
      textColor = getStyles().getPropertyValue(TextInputStyles::placeholderColor, textColor);
    }

    var textAlignment = styleContainer.getPropertyValue(TextStyles::getTextAlignment, TextStyles.TextAlignment.START);

    TextRenderUtils.renderLines(renderer, textRenderDetails, textAlignment, contentBounds, textColor);
  }

  @Override
  public GuiElementDimensions calculateContentDimensions() {
    checkAndHandleAssetIdChange();

    // If text is null then no reason to take up layout space
    if (text == null || text.isEmpty()) {
      textRenderDetails = new TextRenderDetails(0, List.of(), new GuiElementDimensions(0, 0));
      return textRenderDetails.dimensions();
    }

    textRenderDetails = TextRenderUtils.calculateRenderDetails(font, text, contentBounds);

    return textRenderDetails.dimensions();
  }

  /**
   * TextInputGuiElement does not render children so this method will do nothing.
   *
   * @param children the child elements to add
   * @return this
   */
  @Override
  public GuiElement<TextInputStyles> appendChildren(GuiElement<?>... children) {
    return this;
  }

  /**
   * @return the placeholder when no user value is entered
   */
  public String getPlaceholder() {
    return placeholder;
  }

  /**
   * Sets the placeholder text for when no user value is entered.
   *
   * @param placeholder the new placeholder text
   */
  public void setPlaceholder(String placeholder) {
    this.placeholder = placeholder;

    if (value.isEmpty()) {
      setText(placeholder);
    }
  }

  /**
   * @return the maximum number of characters allowed for the input
   */
  public Integer getMaxLength() {
    return maxLength;
  }

  /**
   * Sets the maximum number of characters allowed for the input. If the new length is smaller than the entered value
   * it will be truncated down to the new max length.
   *
   * @param maxLength the new max length
   */
  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;

    if (maxLength != null && valueBuilder.length() > maxLength) {
      valueBuilder.delete(maxLength, valueBuilder.length());
      value = valueBuilder.toString();
      updateText();
    }
  }

  /**
   * @return the user entered value in the input
   */
  public String getValue() {
    return value;
  }

  /**
   * Programmatically sets the user entered value of the input.
   *
   * @param value the new value of the input
   */
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
      setText(placeholder.isEmpty() ? " " : placeholder);
    } else {
      setText(value);
    }
  }

  private void setText(String text) {
    this.text = text;
    invalidateLayout();
  }

  private void checkAndHandleAssetIdChange() {
    String fontAssetId = getStyles().getPropertyValue(TextStyles::fontAssetId, DefaultFont.ASSET_ID);

    if (!fontAssetId.equals(currentFontId)) {
      var fontAssetHandle = getAssetLoaderProvider().get(Font.class).get(fontAssetId);

      if (fontAssetHandle.isLoading()) {
        fontAssetHandle.executeWhenLoaded(font -> {
          this.font = font;
          this.currentFontId = fontAssetId;

          invalidateLayout();
        });
      } else {
        font = fontAssetHandle.getAsset();
      }
    }
  }
}
