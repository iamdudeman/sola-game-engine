package technology.sola.engine.graphics.gui.util;

import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElementBounds;
import technology.sola.engine.graphics.gui.GuiElementDimensions;
import technology.sola.engine.graphics.gui.elements.TextStyles;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

/**
 * TextRenderUtils contains methods useful for rendering text for a GUI.
 */
public class TextRenderUtils {
  /**
   * Calculates {@link TextRenderDetails} for the text based on the content bounds available on current {@link Font}.
   *
   * @param font          the font to render with
   * @param text          the text to render
   * @param contentBounds the bounds to render the text within
   * @return the details needed to render the text
   */
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

  /**
   * Handles rendering lines of GUI text to a {@link Renderer}.
   *
   * @param renderer          the {@code Renderer} instance
   * @param textRenderDetails the {@link TextRenderDetails}
   * @param textAlignment     the {@link TextStyles.TextAlignment} for rendering
   * @param contentBounds     the {@link technology.sola.engine.graphics.gui.GuiElement}'s content {@link GuiElementBounds}
   * @param textColor         the {@link Color} of the lines of text
   */
  public static void renderLines(Renderer renderer, TextRenderDetails textRenderDetails, TextStyles.TextAlignment textAlignment, GuiElementBounds contentBounds, Color textColor) {
    var font = renderer.getFont();
    var lines = textRenderDetails.lines();

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

      renderer.drawString(lines.get(i), contentBounds.x() + xAdjustment, contentBounds.y() + leadingSpace + textRenderDetails.lineHeight() * i, textColor);
    }
  }

  private TextRenderUtils() {
  }
}
