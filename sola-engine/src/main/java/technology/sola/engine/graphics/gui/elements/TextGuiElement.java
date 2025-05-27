package technology.sola.engine.graphics.gui.elements;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementDimensions;
import technology.sola.engine.graphics.gui.style.DefaultStyleValues;
import technology.sola.engine.graphics.gui.util.TextRenderDetails;
import technology.sola.engine.graphics.gui.util.TextRenderUtils;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.List;

/**
 * TextGuiElement is a {@link GuiElement} that renders text for a GUI. It does not render child elements.
 */
@NullMarked
public class TextGuiElement extends GuiElement<TextStyles, TextGuiElement> {
  // properties
  @Nullable
  private String text;

  // internals
  private Font font = DefaultFont.get();
  private String currentFontId = DefaultFont.ASSET_ID;
  @Nullable
  private TextRenderDetails textRenderDetails;

  @Override
  public void renderContent(Renderer renderer) {
    Color textColor = styles().getPropertyValue(TextStyles::textColor, DefaultStyleValues.TEXT_COLOR);
    renderer.setFont(font);

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

  /**
   * TextGuiElement is not focusable so this will return false.
   *
   * @return false
   */
  @Override
  public boolean isFocusable() {
    return false;
  }

  /**
   * TextGuiElement does not render children so this method will do nothing.
   *
   * @param children the child elements to add
   * @return this
   */
  @Override
  public TextGuiElement appendChildren(GuiElement<?, ?>... children) {
    return this;
  }

  /**
   * TextGuiElement does not render children so this method will do nothing.
   *
   * @param child the child element that will not be removed
   * @return this
   */
  @Override
  public TextGuiElement removeChild(GuiElement<?, ?> child) {
    return this;
  }

  /**
   * TextGuiElement does not render children so this method will return an empty List.
   *
   * @return empty List
   */
  @Override
  public List<GuiElement<?, ?>> getChildren() {
    return List.of();
  }

  /**
   * @return the text that will be rendered
   */
  @Nullable
  public String getText() {
    return text;
  }

  /**
   * Sets the text that will be rendered.
   *
   * @param text the new text to be rendered
   * @return this
   */
  public TextGuiElement setText(String text) {
    this.text = text;
    invalidateLayout();

    return this;
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
