package technology.sola.engine.graphics.gui.element;

import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.RenderMode;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.font.Font;

public class TextGuiElement extends GuiElement<TextGuiElement.Properties> {
  private final AssetPoolProvider assetPoolProvider;
  private Font font;

  private int textWidth;
  private int textHeight;

  public TextGuiElement(AssetPoolProvider assetPoolProvider, String text) {
    this.assetPoolProvider = assetPoolProvider;
    properties = new TextGuiElement.Properties();

    properties.setText(text);
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
    renderer.setFont(font);
    renderer.setRenderMode(RenderMode.MASK);
    renderer.drawString(properties.text, x + properties.padding.getLeft(), y + properties.padding.getTop(), properties.colorText);
    renderer.setRenderMode(RenderMode.NORMAL);
  }

  @Override
  public void recalculateChildPositions() {
    font = assetPoolProvider.getAssetPool(Font.class).getAsset(properties().fontName);

    var textDimensions = font.getDimensionsForText(properties().text);

    textWidth = Math.max(textDimensions.width(), 3);
    textHeight = Math.max(textDimensions.height(), 3);
  }

  public static class Properties extends GuiElementBaseProperties {
    private Color colorText = Color.WHITE;
    private String fontName = "monospaced_NORMAL_18";
    private String text = " ";

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
      setLayoutChanged(true);
    }

    public String getFontName() {
      return fontName;
    }

    public void setFontName(String fontName) {
      this.fontName = fontName;
      setLayoutChanged(true);
    }

    public Color getColorText() {
      return colorText;
    }

    public void setColorText(Color colorText) {
      this.colorText = colorText;
    }
  }
}
