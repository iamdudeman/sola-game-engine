package technology.sola.engine.graphics.gui.elements;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.RenderMode;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementGlobalProperties;
import technology.sola.engine.graphics.gui.GuiElementProperties;

public abstract class BaseTextGuiElement<T extends BaseTextGuiElement.Properties> extends GuiElement<T> {
  private Font font;

  private int textWidth;
  private int textHeight;

  public BaseTextGuiElement(T properties) {
    super(properties);
  }

  @Override
  public int getContentWidth() {
    return textWidth + properties.padding.getLeft() + properties.padding.getRight();
  }

  @Override
  public int getContentHeight() {
    return textHeight + properties.padding.getTop() + properties.padding.getBottom();
  }

  @Override
  public void renderSelf(Renderer renderer, int x, int y) {
    Properties properties = properties();

    renderer.setFont(font);
    renderer.setRenderMode(RenderMode.MASK);
    renderer.drawString(properties.getText(), x + properties.padding.getLeft(), y + properties.padding.getTop(), properties.getColorText());
    renderer.setRenderMode(RenderMode.NORMAL);
  }

  @Override
  public void recalculateLayout() {
    font = properties.getAssetPoolProvider().getAssetPool(Font.class).getAsset(properties().getFontAssetId());

    var textDimensions = font.getDimensionsForText(properties().getText());

    textWidth = Math.max(textDimensions.width(), 3);
    textHeight = Math.max(textDimensions.height(), 3);
  }

  public static class Properties extends GuiElementProperties {
    private Color colorText = null;
    private String fontAssetId = null;
    private String text = "";

    public Properties(GuiElementGlobalProperties globalProperties) {
      super(globalProperties);
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
  }
}
