package technology.sola.engine.graphics.guiv2.elements;

import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.GuiElementBounds;
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

    for (int i = 0; i < lines.size(); i++) {
      int leadingSpace = font.getFontInfo().leading() * i;

      renderer.drawString(lines.get(i), contentBounds.x(), contentBounds.y() + leadingSpace + lineHeight * i, textColor);
    }
  }

  @Override
  public GuiElementBounds calculateBounds(GuiElementBounds boundConstraints) {
    // todo cleanup this bad code
    //   maybe find a way to not need to setContentBounds directly!

    String fontAssetId = getStyles().getPropertyValue(TextStyles::fontAssetId, DefaultFont.ASSET_ID);

    System.out.println(text + " " + fontAssetId);

    if (!fontAssetId.equals(currentFontId)) {
      var fontAssetHandle = getAssetLoaderProvider().get(Font.class).get(fontAssetId);

      if (fontAssetHandle.isLoading()) {
        fontAssetHandle.executeWhenLoaded(font -> {
          this.font = font;
          this.currentFontId = fontAssetId;
          System.out.println("loaded " + font.getFontInfo().fontFamily());
          getParent().invalidateLayout();
        });
      } else {
        font = fontAssetHandle.getAsset();
      }
    }

    if (text == null) {
      setContentBounds(contentBounds.setDimensions(0, 0));
    } else {
      setBounds(super.calculateBounds(boundConstraints));

      var textDimensions = font.getDimensionsForText(text);
      lineHeight = Math.max(textDimensions.height(), 1);
      int textHeight = lineHeight;
      int textWidth = Math.max(textDimensions.width(), 1);

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

      setContentBounds(contentBounds.setDimensions(textWidth, textHeight));

    }
      return bounds;
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
