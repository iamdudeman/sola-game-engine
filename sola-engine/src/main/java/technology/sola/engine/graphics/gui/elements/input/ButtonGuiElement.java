package technology.sola.engine.graphics.gui.elements.input;

import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.properties.GuiElementGlobalProperties;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.MouseEvent;

import java.util.function.Consumer;

public class ButtonGuiElement extends BaseInputGuiElement<ButtonGuiElement.Properties> {
  private Runnable onActionConsumer = () -> {};

  private boolean wasMouseDownInside = false;

  public ButtonGuiElement(SolaGuiDocument document) {
    super(document, new Properties(document.globalProperties));

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
    this.onActionConsumer = () -> {
      if (!properties().isDisabled()) {
        onActionConsumer.run();
      }
    };

    return this;
  }

  public static class Properties extends BaseInputGuiElement.Properties {
    public Properties(GuiElementGlobalProperties globalProperties) {
      super(globalProperties);
    }
  }
}
