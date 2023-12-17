package technology.sola.engine.graphics.guiv2.elements;

import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.renderer.Renderer;

public class TextGuiElement extends GuiElement<TextStyles> {
  private String text;
  private Font font;

  public TextGuiElement(TextStyles... styles) {
    super(styles);
  }

  @Override
  public void renderContent(Renderer renderer) {
    if (text != null) {
      Color textColor = getStyles().getPropertyValue(TextStyles::textColor, Color.BLACK);

      renderer.setFont(font);
      renderer.drawString(text, contentBounds.x(), contentBounds.y(), textColor);
    }
  }

  @Override
  public void onRecalculateLayout() {
    String fontAssetId = getStyles().getPropertyValue(TextStyles::fontAssetId, DefaultFont.ASSET_ID);

    // todo handle multiple lines and text wrapping

    getAssetLoaderProvider().get(Font.class).get(fontAssetId).executeWhenLoaded(font -> {
      this.font = font;

      if (text == null) {
        // todo this is only true if "align self" was set to "flex-start"
        setContentBounds(contentBounds.setDimensions(0, 0));
      } else {
        var textDimensions = font.getDimensionsForText(text);

        // todo this is only true if "align self" was set to "flex-start"
        setContentBounds(contentBounds.setDimensions(textDimensions.width(), textDimensions.height()));
      }
    });
  }

  public String getText() {
    return text;
  }

  public TextGuiElement setText(String text) {
    this.text = text;
    invalidateLayout();

    return this;
  }
}
