package technology.sola.engine.graphics.gui.elements;

import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.properties.GuiElementBaseHoverProperties;
import technology.sola.engine.graphics.gui.properties.GuiElementBaseProperties;
import technology.sola.engine.graphics.gui.properties.GuiElementGlobalProperties;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTextGuiElement<T extends BaseTextGuiElement.Properties> extends GuiElement<T> {
  private Font font = DefaultFont.get();
  private String currentFontAssetId;

  private int textWidth = 1;
  private int textHeight = 1;
  private int lineHeight = 1;
  private List<String> lines = new ArrayList<>();

  public BaseTextGuiElement(SolaGuiDocument document, T properties) {
    super(document, properties);
  }

  @Override
  public int getContentWidth() {
    return textWidth;
  }

  @Override
  public int getContentHeight() {
    return textHeight;
  }

  @Override
  public void renderSelf(Renderer renderer, int x, int y) {
    Properties properties = properties();

    if (font != null) {
      Integer width = properties.getWidth();
      int alignOffsetX = 0;

      if (width != null) {
        alignOffsetX = switch (properties.textAlign) {
          case LEFT -> 0;
          case CENTER -> width / 2 - (getContentWidth() + properties.padding.getLeft() + properties.padding.getRight()) / 2;
          case RIGHT -> (width - ((properties.padding.getLeft() + properties.padding.getRight() + getContentWidth())));
        };
      }

      Color colorToRender = properties.getColorText();

      if (properties.hover.colorText != null && isHovered()) {
        colorToRender = properties.hover.colorText;
      }

      renderer.setFont(font);

      for (int i = 0; i < lines.size(); i++) {
        renderer.drawString(lines.get(i), x + alignOffsetX, y + lineHeight * i, colorToRender);
      }
    }
  }

  @Override
  public void recalculateLayout() {
    String propertiesAssetId = properties().getFontAssetId();

    if (!propertiesAssetId.equals(currentFontAssetId)) {
      document.getAssetLoaderProvider()
        .get(Font.class)
        .get(propertiesAssetId)
        .executeWhenLoaded(font -> {
          this.font = font;
          this.currentFontAssetId = propertiesAssetId;
          properties().setLayoutChanged(true);
        });
    }

    Font.TextDimensions textDimensions = font.getDimensionsForText(properties().getText());

    lineHeight = Math.max(textDimensions.height(), 1);
    textHeight = lineHeight;
    textWidth = Math.max(textDimensions.width(), 1);
    int availableWidth = properties.getWidth() == null ? textWidth : properties.getWidth() - getNonContentWidth();

    if (availableWidth < textWidth) {
      textWidth = availableWidth;
      lines = new ArrayList<>();
      String[] words = properties.getText().split(" ");
      String sentence = words.length > 0 ? words[0] : " ";

      for (int i = 1; i < words.length; i++) {
        String word = words[i];
        String tempSentence = sentence + " " + word;
        Font.TextDimensions sentenceDimensions = font.getDimensionsForText(tempSentence);

        if (sentenceDimensions.width() < availableWidth) {
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
      lines.add(properties().getText());
    }
  }

  public static class HoverProperties extends GuiElementBaseHoverProperties {
    private Color colorText = null;

    public Color getColorText() {
      return colorText;
    }

    public HoverProperties setColorText(Color colorText) {
      this.colorText = colorText;

      return this;
    }
  }

  public static class Properties extends GuiElementBaseProperties<HoverProperties> {
    private Color colorText = null;
    private String fontAssetId = null;
    private String text = "";
    private TextAlign textAlign = TextAlign.LEFT;

    public Properties(GuiElementGlobalProperties globalProperties) {
      super(globalProperties, new HoverProperties());
      setFocusable(false);
    }

    public String getText() {
      return text;
    }

    public Properties setText(String text) {
      this.text = text;
      setLayoutChanged(true);
      return this;
    }

    public String getFontAssetId() {
      return fontAssetId == null ? globalProperties.getDefaultFontAssetId() : fontAssetId;
    }

    public Properties setFontAssetId(String fontAssetId) {
      this.fontAssetId = fontAssetId;
      setLayoutChanged(true);
      return this;
    }

    public Color getColorText() {
      return colorText == null ? globalProperties.getDefaultTextColor() : colorText;
    }

    public Properties setColorText(Color colorText) {
      this.colorText = colorText;
      return this;
    }

    public TextAlign getTextAlign() {
      return textAlign;
    }

    public Properties setTextAlign(TextAlign textAlign) {
      this.textAlign = textAlign;
      return this;
    }
  }

  public enum TextAlign {
    LEFT,
    CENTER,
    RIGHT
  }
}
