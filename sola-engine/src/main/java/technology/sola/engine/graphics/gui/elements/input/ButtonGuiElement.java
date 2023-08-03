package technology.sola.engine.graphics.gui.elements.input;

import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.properties.GuiPropertyDefaults;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.MouseEvent;

import java.util.function.Consumer;

/**
 * ButtonGuiElement is a {@link technology.sola.engine.graphics.gui.GuiElement} that provides a button input that can
 * be clicked or interacted with via keyboard.
 */
public class ButtonGuiElement extends BaseInputGuiElement<ButtonGuiElement.Properties> {
  private Runnable onActionConsumer = () -> {
  };

  private boolean wasMouseDownInside = false;

  /**
   * Creates a ButtonGuiElement instance.
   *
   * @param document the {@link SolaGuiDocument}
   */
  public ButtonGuiElement(SolaGuiDocument document) {
    super(document, new Properties(document.propertyDefaults));

    setOnMouseEnterCallback(event -> {
    });
    setOnMouseExitCallback(event -> {
    });
    setOnMouseDownCallback(event -> {
    });
    setOnMouseUpCallback(event -> {
    });
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

  /**
   * Sets the action of this button.
   *
   * @param onActionConsumer the function ran when button is interacted with
   * @return this
   */
  public ButtonGuiElement setOnAction(Runnable onActionConsumer) {
    this.onActionConsumer = () -> {
      if (!properties().isDisabled()) {
        onActionConsumer.run();
      }
    };

    return this;
  }

  /**
   * The properties for a {@link ButtonGuiElement}.
   */
  public static class Properties extends BaseInputGuiElement.Properties {
    /**
     * Creates the properties for a {@link ButtonGuiElement}.
     *
     * @param propertyDefaults the {@link GuiPropertyDefaults}
     */
    public Properties(GuiPropertyDefaults propertyDefaults) {
      super(propertyDefaults);
    }
  }
}
