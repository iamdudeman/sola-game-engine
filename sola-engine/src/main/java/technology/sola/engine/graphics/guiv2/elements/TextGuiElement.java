package technology.sola.engine.graphics.guiv2.elements;

import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.GuiElementDimensions;
import technology.sola.engine.graphics.guiv2.util.TextRenderUtils;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public class TextGuiElement extends GuiElement<TextStyles> {
  // properties
  private String text;

  // internals
  private Font font = DefaultFont.get();
  private String currentFontId = DefaultFont.ASSET_ID;
  private int lineHeight = 1;
  private List<String> lines = new ArrayList<>();

  public TextGuiElement(TextStyles... styles) {
    super(styles);
  }

  @Override
  public void renderContent(Renderer renderer) {
    Color textColor = getStyles().getPropertyValue(TextStyles::textColor, Color.BLACK);
    renderer.setFont(font);

    var textAlignment = styleContainer.getPropertyValue(TextStyles::getTextAlignment, TextStyles.TextAlignment.START);

    TextRenderUtils.renderLines(renderer, lines, textAlignment, contentBounds, lineHeight, textColor);
  }

  @Override
  public GuiElementDimensions calculateContentDimensions() {
    checkAndHandleAssetIdChange();

    // If text is null then no reason to take up layout space
    if (text == null || text.isEmpty()) {
      return new GuiElementDimensions(0, 0);
    }

    var renderDetails = TextRenderUtils.calculateRenderDetails(font, text, contentBounds);

    lineHeight = renderDetails.lineHeight();
    lines = renderDetails.lines();

    return renderDetails.dimensions();
  }

  @Override
  public boolean isFocusable() {
    return false;
  }

  @Override
  public GuiElement<TextStyles> appendChildren(GuiElement<?>... children) {
    return this;
  }

  public String getText() {
    return text;
  }

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
