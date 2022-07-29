package technology.sola.engine.graphics.gui.elements.control;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.gui.GuiElementGlobalProperties;
import technology.sola.engine.graphics.gui.SolaGui;
import technology.sola.engine.graphics.gui.elements.BaseTextGuiElement;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.MouseEvent;

import java.util.function.Consumer;

public class ButtonGuiElement extends BaseTextGuiElement<ButtonGuiElement.Properties> {
  private Runnable onActionConsumer = () -> {};

  private boolean wasMouseDownInside = false;
  private boolean isHovered = false;

  public ButtonGuiElement(SolaGui solaGui, Properties properties) {
    super(solaGui, properties);

    setOnMouseEnterCallback(event -> {});
    setOnMouseExitCallback(event -> {});
    setOnMouseDownCallback(event -> {});
    setOnMouseUpCallback(event -> {});
    setOnKeyPressCallback(keyEvent -> {
      if (keyEvent.keyCode() == Key.ENTER.getCode()) {
        onActionConsumer.run();
        return false;
      }

      return true;
    });
  }

  @Override
  public void renderSelf(Renderer renderer, int x, int y) {
    Properties properties = properties();
    int contentWidth = getContentWidth();
    int contentHeight = getContentHeight();

    renderer.fillRect(x, y, contentWidth, contentHeight, isHovered ? properties.colorBackgroundHover : properties.colorBackground);
    renderer.drawRect(x, y, contentWidth, contentHeight, isHovered ? properties.colorBorderHover : properties.colorBorder);

    Color baseTextColor = properties.getColorText();

    if (isHovered) {
      properties.setColorText(properties.colorTextHover);
    }

    super.renderSelf(renderer, x, y);

    properties.setColorText(baseTextColor);
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
        requestFocus();
        onActionConsumer.run();
        wasMouseDownInside = false;
      }
    });
  }

  public void setOnAction(Runnable onActionConsumer) {
    this.onActionConsumer = onActionConsumer;
  }

  public static class Properties extends BaseTextGuiElement.Properties {
    private Color colorBorder = new Color(128, 128, 128);
    private Color colorBorderHover = new Color(128, 128, 128);
    private Color colorBackground = new Color(128, 128, 128);
    private Color colorBackgroundHover = Color.WHITE;
    private Color colorTextHover = new Color(128, 128, 128);

    public Properties(GuiElementGlobalProperties globalProperties) {
      super(globalProperties);
    }

    public Color getColorBackground() {
      return colorBackground;
    }

    public Properties setColorBackground(Color colorBackground) {
      this.colorBackground = colorBackground;
      return this;
    }

    public Color getColorBorder() {
      return colorBorder;
    }

    public Properties setColorBorder(Color colorBorder) {
      this.colorBorder = colorBorder;
      return this;
    }

    public Color getColorBorderHover() {
      return colorBorderHover;
    }

    public Properties setColorBorderHover(Color colorBorderHover) {
      this.colorBorderHover = colorBorderHover;
      return this;
    }

    public Color getColorBackgroundHover() {
      return colorBackgroundHover;
    }

    public Properties setColorBackgroundHover(Color colorBackgroundHover) {
      this.colorBackgroundHover = colorBackgroundHover;
      return this;
    }

    public Color getColorTextHover() {
      return colorTextHover;
    }

    public Properties setColorTextHover(Color colorTextHover) {
      this.colorTextHover = colorTextHover;
      return this;
    }
  }
}
