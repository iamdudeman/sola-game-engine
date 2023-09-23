package technology.sola.engine.graphics.guiv2.elements;

import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.GuiElementBounds;
import technology.sola.engine.graphics.guiv2.GuiElementWithoutChildren;
import technology.sola.engine.graphics.guiv2.style.property.Border;
import technology.sola.engine.graphics.guiv2.style.property.Padding;
import technology.sola.engine.graphics.renderer.Renderer;

public class TextGuiElement extends GuiElementWithoutChildren<TextStyles> {
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

    getAssetLoaderProvider().get(Font.class).get(fontAssetId).executeWhenLoaded(font -> {
      this.font = font;

      Border border = getStyles().getPropertyValue(TextStyles::border, Border.NONE);
      Padding padding = getStyles().getPropertyValue(TextStyles::padding, Padding.NONE);

      if (text == null) {
        // todo this is only true if "align self" was set to "flex-start"
        contentBounds = contentBounds.setDimensions(0, 0);
      } else {
        var textDimensions = font.getDimensionsForText(text);

        // todo this is only true if "align self" was set to "flex-start"
        contentBounds = contentBounds.setDimensions(textDimensions.width(), textDimensions.height());
      }

      bounds = new GuiElementBounds(
        contentBounds.x() - border.left() - padding.left().getValue(getParent().getContentBounds().width()),
        contentBounds.y() - border.top() - padding.top().getValue(getParent().getContentBounds().height()),
        contentBounds.width() + border.left() + border.right() + padding.left().getValue(getParent().getContentBounds().width()) + padding.right().getValue(getParent().getContentBounds().width()),
        contentBounds.height() + border.top() + border.bottom() + padding.top().getValue(getParent().getContentBounds().height()) + padding.bottom().getValue(getParent().getContentBounds().height())
      );
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
