package technology.sola.engine.graphics.guiv2.elements;

import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.GuiElementDimensions;
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

    for (int i = 0; i < lines.size(); i++) {
      int leadingSpace = font.getFontInfo().leading() * i;
      String line = lines.get(i);

      int xAdjustment = 0;

      if (textAlignment != TextStyles.TextAlignment.START) {
        var textDimensions = font.getDimensionsForText(line);
        int availableSpace = contentBounds.width() - textDimensions.width();

        if (availableSpace > 0) {
          if (textAlignment == TextStyles.TextAlignment.CENTER) {
            xAdjustment = availableSpace / 2;
          } else if (textAlignment == TextStyles.TextAlignment.END) {
            xAdjustment = availableSpace;
          }
        }
      }

      renderer.drawString(lines.get(i), contentBounds.x() + xAdjustment, contentBounds.y() + leadingSpace + lineHeight * i, textColor);
    }
  }

  @Override
  public GuiElementDimensions calculateContentDimensions() {
    checkAndHandleAssetIdChange();

    // If text is null then no reason to take up layout space
    if (text == null || text.isEmpty()) {
      return new GuiElementDimensions(0, 0);
    }

    var textDimensions = font.getDimensionsForText(text);
    lineHeight = textDimensions.height();
    int textHeight = lineHeight;
    int textWidth = textDimensions.width();

    if (contentBounds.width() < textWidth) {
      textWidth = contentBounds.width();
      lines = new ArrayList<>();
      String[] words = text.split(" ");
      String sentence = words.length > 0 ? words[0] : " ";

      for (int i = 1; i < words.length; i++) {
        String word = words[i];
        String tempSentence = sentence + " " + word;
        Font.TextDimensions sentenceDimensions = font.getDimensionsForText(tempSentence);

        if (sentenceDimensions.width() < contentBounds.width()) {
          sentence = tempSentence;
        } else {
          // Start next line with current word
          lines.add(sentence);
          sentence = word;
        }
      }

      lines.add(sentence);
      textHeight = lineHeight * lines.size();
    } else {
      lines = new ArrayList<>(1);
      lines.add(text);
    }

    return new GuiElementDimensions(textWidth, textHeight);
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

          getParent().invalidateLayout();
        });
      } else {
        font = fontAssetHandle.getAsset();
      }
    }
  }
}
