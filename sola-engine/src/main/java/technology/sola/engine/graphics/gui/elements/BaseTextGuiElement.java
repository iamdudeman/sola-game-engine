package technology.sola.engine.graphics.gui.elements;

import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.core.module.graphics.gui.SolaGui;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementGlobalProperties;
import technology.sola.engine.graphics.gui.GuiElementProperties;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;

public abstract class BaseTextGuiElement<T extends BaseTextGuiElement.Properties> extends GuiElement<T> {
  private Font font;

  private int textWidth = 1;
  private int textHeight = 1;

  public BaseTextGuiElement(SolaGui solaGui, T properties) {
    super(solaGui, properties);
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
    renderer.drawWithBlendMode(BlendMode.MASK, r -> renderer.drawString(properties.getText(), x + properties.padding.getLeft(), y + properties.padding.getTop(), properties.getColorText()));
  }

  @Override
  public void recalculateLayout() {
    solaGui.getAssetPoolProvider().getAssetPool(Font.class).getAsset(properties().getFontAssetId())
      .executeIfLoaded(font -> {
        this.font = font;
        var textDimensions = font.getDimensionsForText(properties().getText());

        textWidth = Math.max(textDimensions.width(), 1);
        textHeight = Math.max(textDimensions.height(), 1);
      });
  }

  public static class Properties extends GuiElementProperties {
    private Color colorText = null;
    private String fontAssetId = null;
    private String text = "";

    public Properties(GuiElementGlobalProperties globalProperties) {
      super(globalProperties);
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
  }
}
