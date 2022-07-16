package technology.sola.engine.graphics.gui.element.control;

import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.RenderMode;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.graphics.gui.element.ChildlessGuiElement;
import technology.sola.engine.graphics.gui.element.GuiElementBaseProperties;
import technology.sola.engine.input.MouseEvent;

import java.util.function.Consumer;

public class ButtonGuiElement extends ChildlessGuiElement<ButtonGuiElement.Properties> {
  private final AssetPoolProvider assetPoolProvider;
  private Font font;

  private Consumer<MouseEvent> onClickConsumer = event -> {};

  private boolean wasMouseDownInside = false;
  private boolean isHovered = false;
  private int textWidth;
  private int textHeight;

  public ButtonGuiElement(AssetPoolProvider assetPoolProvider, String text) {
    this.assetPoolProvider = assetPoolProvider;
    properties = new Properties();

    properties.setText(text);
    setOnMouseEnterCallback((event) -> {});
    setOnMouseExitCallback((event) -> {});
    setOnMouseDownCallback(event -> {});
    setOnMouseUpCallback(event -> {});
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
    renderer.fillRect(x, y, getContentWidth(), getContentHeight(), isHovered ? properties.colorBackgroundHover : properties.colorBackground);

    renderer.drawRect(x, y, getContentWidth(), getContentHeight(), isHovered ? properties.colorBorderHover : properties.colorBorder);

    renderer.setFont(font);
    renderer.setRenderMode(RenderMode.MASK);
    renderer.drawString(properties.text, x + properties.padding.getLeft(), y + properties.padding.getTop(), isHovered ? properties.colorTextHover : properties.colorText);
    renderer.setRenderMode(RenderMode.NORMAL);
  }

  @Override
  public void setOnMouseEnterCallback(Consumer<MouseEvent> callback) {
    super.setOnMouseEnterCallback(event -> {
      isHovered = true;
      callback.accept(event);
    });
  }

  @Override
  public void setOnMouseExitCallback(Consumer<MouseEvent> callback) {
    super.setOnMouseExitCallback(event -> {
      isHovered = false;
      wasMouseDownInside = false;
      callback.accept(event);
    });
  }

  @Override
  public void setOnMouseDownCallback(Consumer<MouseEvent> callback) {
    super.setOnMouseDownCallback(event -> {
      wasMouseDownInside = true;
      callback.accept(event);
    });
  }

  @Override
  public void setOnMouseUpCallback(Consumer<MouseEvent> callback) {
    super.setOnMouseUpCallback(event -> {
      callback.accept(event);
      if (wasMouseDownInside) {
        onClickConsumer.accept(event);
        wasMouseDownInside = false;
      }
    });
  }

  @Override
  public void recalculateChildPositions() {
    font = assetPoolProvider.getAssetPool(Font.class).getAsset(properties().fontName);

    var textDimensions = font.getDimensionsForText(properties().text);

    textWidth = Math.max(textDimensions.width(), 3);
    textHeight = Math.max(textDimensions.height(), 3);
  }

  public void setOnClick(Consumer<MouseEvent> onClickConsumer) {
    this.onClickConsumer = onClickConsumer;
  }

  public static class Properties extends GuiElementBaseProperties {
    private Color colorBorder = new Color(128, 128, 128);
    private Color colorBorderHover = new Color(128, 128, 128);
    private Color colorBackground = new Color(128, 128, 128);
    private Color colorBackgroundHover = Color.WHITE;
    private Color colorText = Color.WHITE;
    private Color colorTextHover = new Color(128, 128, 128);
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

    public Color getColorBackground() {
      return colorBackground;
    }

    public void setColorBackground(Color colorBackground) {
      this.colorBackground = colorBackground;
    }

    public Color getColorBorder() {
      return colorBorder;
    }

    public void setColorBorder(Color colorBorder) {
      this.colorBorder = colorBorder;
    }

    public Color getColorBorderHover() {
      return colorBorderHover;
    }

    public void setColorBorderHover(Color colorBorderHover) {
      this.colorBorderHover = colorBorderHover;
    }

    public Color getColorBackgroundHover() {
      return colorBackgroundHover;
    }

    public void setColorBackgroundHover(Color colorBackgroundHover) {
      this.colorBackgroundHover = colorBackgroundHover;
    }

    public Color getColorText() {
      return colorText;
    }

    public void setColorText(Color colorText) {
      this.colorText = colorText;
    }

    public Color getColorTextHover() {
      return colorTextHover;
    }

    public void setColorTextHover(Color colorTextHover) {
      this.colorTextHover = colorTextHover;
    }
  }
}
