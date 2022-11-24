package technology.sola.engine.graphics.gui.elements.control;

import technology.sola.engine.core.module.graphics.gui.SolaGui;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.properties.GuiElementGlobalProperties;
import technology.sola.engine.graphics.gui.elements.BaseTextGuiElement;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.MouseEvent;

import java.util.function.Consumer;

public class ButtonGuiElement extends BaseTextGuiElement<ButtonGuiElement.Properties> {
  private Runnable onActionConsumer = () -> {};

  private boolean wasMouseDownInside = false;

  public ButtonGuiElement(SolaGui solaGui, Properties properties) {
    super(solaGui, properties);

    setOnMouseEnterCallback(event -> {});
    setOnMouseExitCallback(event -> {});
    setOnMouseDownCallback(event -> {});
    setOnMouseUpCallback(event -> {});
    setOnKeyPressCallback(keyEvent -> {
      if (keyEvent.getKeyCode() == Key.ENTER.getCode() || keyEvent.getKeyCode() == Key.CARRIAGE_RETURN.getCode()) {
        onActionConsumer.run();
        keyEvent.stopPropagation();
      }
    });
  }

  @Override
  public void renderSelf(Renderer renderer, int x, int y) {
    Properties properties = properties();
    Color baseTextColor = properties.getColorText();

    if (isHovered()) {
      properties.setColorText(properties.colorTextHover);
    }

    super.renderSelf(renderer, x, y);

    properties.setColorText(baseTextColor);
  }

  @Override
  public void setOnMouseExitCallback(Consumer<MouseEvent> callback) {
    super.setOnMouseExitCallback(event -> {
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
    private Color colorTextHover = Color.DARK_GRAY;

    public Properties(GuiElementGlobalProperties globalProperties) {
      super(globalProperties);
      setBackgroundColor(Color.DARK_GRAY);
      setHoverBackgroundColor(Color.WHITE);

      setFocusable(true);
      setBorderColor(Color.WHITE);
      setHoverBorderColor(Color.DARK_GRAY);
      setFocusOutlineColor(Color.LIGHT_BLUE);
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
