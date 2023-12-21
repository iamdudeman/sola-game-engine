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
  private Font font = DefaultFont.get();
  private int lineHeight = 1;
  private List<String> lines = new ArrayList<>();
  // properties
  private String text;

  public TextGuiElement(TextStyles... styles) {
    super(styles);
  }

  @Override
  public void renderContent(Renderer renderer) {
//    if (text != null) {
//      Color textColor = getStyles().getPropertyValue(TextStyles::textColor, Color.BLACK);
//
//      renderer.setFont(font);
//      renderer.drawString(text, contentBounds.x(), contentBounds.x(), textColor);
//    }

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
    // todo support multiple line wrapping
//    String fontAssetId = getStyles().getPropertyValue(TextStyles::fontAssetId, DefaultFont.ASSET_ID);
//
//    var fontAssetHandle = getAssetLoaderProvider().get(Font.class).get(fontAssetId);
//
//    if (fontAssetHandle.isLoading() || font == null) {
//      fontAssetHandle.executeWhenLoaded(font -> {
//        this.font = font;
//        System.out.println("loaded " + text);
//        invalidateLayout();
//      });
//
//      return super.calculateBounds(boundConstraints);
//    } else {
      if (text == null) {
        setContentBounds(contentBounds.setDimensions(0, 0));

        return bounds;
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

        return bounds;
      }
//    }

    // todo handle multiple lines and text wrapping

//    getAssetLoaderProvider().get(Font.class).get(fontAssetId).executeWhenLoaded(font -> {
//      this.font = font;
//
//      if (text == null) {
//        // todo this is only true if "align self" was set to "flex-start"
//        setContentBounds(contentBounds.setDimensions(0, 0));
//      } else {
//        var textDimensions = font.getDimensionsForText(text);
//
//        // todo this is only true if "align self" was set to "flex-start"
//        setContentBounds(contentBounds.setDimensions(textDimensions.width(), textDimensions.height()));
//      }
//    });
//
//    return new GuiElementBounds(0, 0 ,10, 10); // todo real value
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
