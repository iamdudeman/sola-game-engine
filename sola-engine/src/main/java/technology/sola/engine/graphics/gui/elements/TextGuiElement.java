package technology.sola.engine.graphics.gui.elements;

import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementDimensions;
import technology.sola.engine.graphics.gui.util.TextRenderDetails;
import technology.sola.engine.graphics.gui.util.TextRenderUtils;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.List;

/**
 * TextGuiElement is a {@link GuiElement} that renders text for a GUI. It does not render child elements.
 */
public class TextGuiElement extends GuiElement<TextStyles> {
  // properties
  private String text;

  // internals
  private Font font = DefaultFont.get();
  private String currentFontId = DefaultFont.ASSET_ID;
  private TextRenderDetails textRenderDetails;

  @Override
  public void renderContent(Renderer renderer) {
    Color textColor = getStyles().getPropertyValue(TextStyles::textColor, Color.BLACK);
    renderer.setFont(font);

    var textAlignment = styleContainer.getPropertyValue(TextStyles::textAlignment, TextStyles.TextAlignment.START);

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
  public GuiElement<TextStyles> appendChildren(GuiElement<?>... children) {
    return this;
  }

  /**
   * @return the text that will be rendered
   */
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
