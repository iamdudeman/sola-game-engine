package technology.sola.engine.graphics.gui.elements.control;

import technology.sola.engine.core.module.graphics.gui.SolaGui;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.properties.GuiElementGlobalProperties;
import technology.sola.engine.graphics.gui.elements.BaseTextGuiElement;
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
    public Properties(GuiElementGlobalProperties globalProperties) {
      super(globalProperties);
      setFocusable(true);
      setFocusOutlineColor(Color.LIGHT_BLUE);

      setBackgroundColor(Color.DARK_GRAY);
      setBorderColor(Color.WHITE);

      hover.setBackgroundColor(Color.WHITE);
      hover.setBorderColor(Color.DARK_GRAY);
      hover.setColorText(Color.DARK_GRAY);
    }
  }
}
