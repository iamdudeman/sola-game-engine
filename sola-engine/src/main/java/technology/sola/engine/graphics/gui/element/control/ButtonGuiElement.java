package technology.sola.engine.graphics.gui.element.control;

import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.RenderMode;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.graphics.gui.element.ChildlessGuiElement;
import technology.sola.engine.graphics.gui.element.GuiElementBaseProperties;

// todo add onClick method?
// todo change default border color and background color stuff
// todo ability to change font

public class ButtonGuiElement extends ChildlessGuiElement<ButtonGuiElement.Properties> {
  private final AssetPoolProvider assetPoolProvider;
  private String text;
  private Font font;

  private int textWidth;
  private int textHeight;

  public ButtonGuiElement(AssetPoolProvider assetPoolProvider, String text) {
    this.assetPoolProvider = assetPoolProvider;
    properties = new Properties();

    setText(text);

    setOnMouseEnterCallback((event) -> properties.borderColor = Color.GREEN);

    setOnMouseExitCallback((event) -> properties.borderColor = Color.BLUE);
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
    renderer.fillRect(x, y, getContentWidth(), getContentHeight(), properties.backgroundColor);

    renderer.drawRect(x, y, getContentWidth(), getContentHeight(), properties.borderColor);

    renderer.setFont(font);
    renderer.setRenderMode(RenderMode.MASK);
    renderer.drawString(text, x + properties.padding.getLeft(), y + properties.padding.getTop(), properties.borderColor);
    renderer.setRenderMode(RenderMode.NORMAL);
  }

  private void setText(String text) {
    font = assetPoolProvider.getAssetPool(Font.class).getAsset(properties().fontName);

    this.text = text;

    var textDimensions = font.getDimensionsForText(text);

    textWidth = Math.max(textDimensions.width(), 3);
    textHeight = Math.max(textDimensions.height(), 3);

    properties.setMaxDimensions(textWidth, textHeight);
  }

  public static class Properties extends GuiElementBaseProperties {
    private Color borderColor = Color.BLUE;
    private Color backgroundColor = Color.WHITE;
    private String fontName = "default";

    public Color getBackgroundColor() {
      return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
      this.backgroundColor = backgroundColor;
    }
  }
}
