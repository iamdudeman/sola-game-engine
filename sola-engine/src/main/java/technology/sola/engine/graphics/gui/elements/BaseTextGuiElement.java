package technology.sola.engine.graphics.gui.elements;

import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.RenderMode;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.graphics.gui.GuiElementProperties;

public abstract class BaseTextGuiElement<T extends BaseTextGuiElement.Properties> extends ChildlessGuiElement<T> {
  private final AssetPoolProvider assetPoolProvider;
  private Font font;

  private int textWidth;
  private int textHeight;

  public BaseTextGuiElement(AssetPoolProvider assetPoolProvider, T properties) {
    super(properties);
    this.assetPoolProvider = assetPoolProvider;
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
    renderer.drawString(properties.getText(), x + properties.padding.getLeft(), y + properties.padding.getTop(), properties.getColorText());
    renderer.setRenderMode(RenderMode.NORMAL);
  }

  @Override
  public void recalculateChildPositions() {
    font = assetPoolProvider.getAssetPool(Font.class).getAsset(properties().getFontName());

    var textDimensions = font.getDimensionsForText(properties().getText());

    textWidth = Math.max(textDimensions.width(), 3);
    textHeight = Math.max(textDimensions.height(), 3);
  }

  public static class Properties extends GuiElementProperties {
    private Color colorText = Color.WHITE; // todo default should maybe come from a central place
    private String fontName = "monospaced_NORMAL_18"; // todo default should maybe come from a central place
    private String text = "";

    public String getText() {
      return text;
    }

    public Properties setText(String text) {
      this.text = text;
      setLayoutChanged(true);
      return this;
    }

    public String getFontName() {
      return fontName;
    }

    public Properties setFontName(String fontName) {
      this.fontName = fontName;
      setLayoutChanged(true);
      return this;
    }

    public Color getColorText() {
      return colorText;
    }

    public Properties setColorText(Color colorText) {
      this.colorText = colorText;
      return this;
    }
  }
}
