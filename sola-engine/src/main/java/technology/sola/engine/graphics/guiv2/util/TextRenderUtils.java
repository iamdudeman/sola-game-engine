package technology.sola.engine.graphics.guiv2.util;

import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.GuiElementBounds;
import technology.sola.engine.graphics.guiv2.GuiElementDimensions;
import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public class TextRenderUtils {
  public static TextRenderDetails calculateRenderDetails(Font font, String text, GuiElementBounds contentBounds) {
    List<String> lines = new ArrayList<>();
    int lineHeight = 0;
    int textWidth = 0;
    String[] newLines = text.split("\n");

    for (String newLine : newLines) {
      var textDimensions = font.getDimensionsForText(newLine);
      lineHeight = textDimensions.height();
      textWidth = Math.max(textWidth, textDimensions.width());

      if (contentBounds.width() < textWidth) {
        textWidth = contentBounds.width();
        String[] words = newLine.split(" ");
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
      } else {
        lines.add(newLine);
      }
    }

    int textHeight = lineHeight * lines.size();

    return new TextRenderDetails(
      lineHeight, lines, new GuiElementDimensions(textWidth, textHeight)
    );
  }

  public static void renderLines(Renderer renderer, List<String> lines, TextStyles.TextAlignment textAlignment, GuiElementBounds contentBounds, int lineHeight, Color textColor) {
    var font = renderer.getFont();

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

  public record TextRenderDetails(int lineHeight, List<String> lines, GuiElementDimensions dimensions) {
  }

  private TextRenderUtils() {
  }
}
