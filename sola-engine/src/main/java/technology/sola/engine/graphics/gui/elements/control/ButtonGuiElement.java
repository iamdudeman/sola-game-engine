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

  public ButtonGuiElement(SolaGui solaGui) {
    super(solaGui, new Properties(solaGui.globalProperties));

    setOnMouseEnterCallback(event -> {});
    setOnMouseExitCallback(event -> {});
    setOnMouseDownCallback(event -> {});
    setOnMouseUpCallback(event -> {});
    setOnKeyPressCallback(keyEvent -> {
      int keyCode = keyEvent.getKeyCode();

      if (keyCode == Key.ENTER.getCode() || keyCode == Key.CARRIAGE_RETURN.getCode() || keyCode == Key.SPACE.getCode()) {
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

  public ButtonGuiElement setOnAction(Runnable onActionConsumer) {
    this.onActionConsumer = onActionConsumer;

    return this;
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
