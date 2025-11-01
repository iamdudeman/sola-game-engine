package technology.sola.engine.graphics.gui.elements.input;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementDimensions;
import technology.sola.engine.graphics.gui.elements.TextStyles;
import technology.sola.engine.graphics.gui.style.DefaultStyleValues;
import technology.sola.engine.graphics.gui.util.TextRenderDetails;
import technology.sola.engine.graphics.gui.util.TextRenderUtils;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.KeyboardLayout;

import java.util.List;

/**
 * TextInputGuiElement is a {@link BaseInputGuiElement} that allows users to interact via typing text into the field.
 */
@NullMarked
public class TextInputGuiElement extends BaseInputGuiElement<TextInputStyles, TextInputGuiElement> {
  // props
  private String placeholder = "";
  @Nullable
  private Integer maxLength;

  // internals
  private String value = "";
  private final StringBuilder valueBuilder = new StringBuilder();
  private boolean isShiftDown = false;
  private Font font = DefaultFont.get();
  private String currentFontId = DefaultFont.ASSET_ID;
  @Nullable
  private TextRenderDetails textRenderDetails;
  @Nullable
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

    events().keyPressed().on(guiKeyEvent -> {
      var keyEvent = guiKeyEvent.getKeyEvent();
      int keyCode = keyEvent.keyCode();
      int length = valueBuilder.length();
      boolean isLessThanMaxLength = maxLength == null || length < maxLength;

      if (keyCode == Key.SHIFT.getCode()) {
        isShiftDown = true;
      } else if (keyCode == Key.BACKSPACE.getCode()) {
        if (length > 0) {
          valueBuilder.deleteCharAt(length - 1);
        }
      } else if (isLessThanMaxLength) {
        char character = keyEvent.keyChar();

        if (keyCode >= Key.A.getCode() && keyCode <= Key.Z.getCode()) {
          if (isShiftDown) {
            valueBuilder.append(character);
          } else {
            valueBuilder.append((char) (keyCode + 32));
          }
        } else if (keyCode == Key.SPACE.getCode()) {
          valueBuilder.append(character);
        } else if (KeyboardLayout.hasShift(keyCode)) {
          if (isShiftDown) {
            valueBuilder.append(KeyboardLayout.shift(character));
          } else {
            valueBuilder.append(character);
          }
        }
      }

      setValue(valueBuilder.toString());
    });

    events().mousePressed().on(mouseEvent -> {
      if (!isDisabled()) {
        setVirtualKeyboardVisible(true);
        mouseEvent.stopPropagation();
      }
    });

    events().touchEnd().on(touchEvent -> {
      if (!isDisabled()) {
        setVirtualKeyboardVisible(true);
        touchEvent.stopPropagation();
      }
    });
  }

  @Override
  public void renderContent(Renderer renderer) {
    Color textColor = styles().getPropertyValue(TextStyles::textColor, DefaultStyleValues.TEXT_COLOR);
    renderer.setFont(font);

    // Use placeholder color if present
    if (value.isEmpty() && !placeholder.isEmpty()) {
      textColor = styles().getPropertyValue(TextInputStyles::placeholderColor, textColor);
    }

    var textAlignment = styleContainer.getPropertyValue(TextStyles::textAlignment, DefaultStyleValues.TEXT_ALIGNMENT);

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

  @Override
  public TextInputGuiElement self() {
    return this;
  }

  /**
   * TextInputGuiElement does not render children so this method will do nothing.
   *
   * @param children the child elements to add
   * @return this
   */
  @Override
  public TextInputGuiElement appendChildren(GuiElement<?, ?>... children) {
    throw new UnsupportedOperationException("TextInputGuiElement does not render children");
  }

  /**
   * TextInputGuiElement does not render children so this method will do nothing.
   *
   * @param child the child element that will not be removed
   * @return this
   */
  @Override
  public TextInputGuiElement removeChild(GuiElement<?, ?> child) {
    throw new UnsupportedOperationException("TextInputGuiElement does not render children");
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
   * @return this
   */
  public TextInputGuiElement setPlaceholder(String placeholder) {
    this.placeholder = placeholder;

    if (value.isEmpty()) {
      setText(placeholder);
    }

    return this;
  }

  /**
   * @return the maximum number of characters allowed for the input
   */
  @Nullable
  public Integer getMaxLength() {
    return maxLength;
  }

  /**
   * Sets the maximum number of characters allowed for the input. If the new length is smaller than the entered value
   * it will be truncated down to the new max length.
   *
   * @param maxLength the new max length
   * @return this
   */
  public TextInputGuiElement setMaxLength(@Nullable Integer maxLength) {
    this.maxLength = maxLength;

    if (maxLength != null && valueBuilder.length() > maxLength) {
      valueBuilder.delete(maxLength, valueBuilder.length());
      value = valueBuilder.toString();
      updateText();
    }

    return this;
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
   * @return this
   */
  public TextInputGuiElement setValue(String value) {
    valueBuilder.delete(0, valueBuilder.length());

    if (maxLength != null && maxLength < value.length()) {
      valueBuilder.append(value, 0, maxLength);
    } else {
      valueBuilder.append(value);
    }

    this.value = valueBuilder.toString();
    updateText();

    return this;
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
    String fontAssetId = styles().getPropertyValue(TextStyles::fontAssetId, DefaultStyleValues.FONT_ASSET_ID);

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
