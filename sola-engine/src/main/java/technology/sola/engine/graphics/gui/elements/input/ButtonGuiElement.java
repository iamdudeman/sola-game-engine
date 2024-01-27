package technology.sola.engine.graphics.gui.elements.input;

import technology.sola.engine.graphics.gui.GuiElementDimensions;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;

/**
 * ButtonGuiElement is a {@link BaseInputGuiElement} that allows users to interact via clicking the button or using
 * keyboard shortcuts.
 */
public class ButtonGuiElement extends BaseInputGuiElement<BaseStyles> {
  // props
  private Runnable onAction = () -> {
  };

  /**
   * Creates a ButtonGuiElement and adds default event listeners for handling disabled states and when the {@link Key#SPACE}
   * key is pressed.
   */
  public ButtonGuiElement() {
    super();

    events().mousePressed().on(mouseEvent -> {
      if (!isDisabled()) {
        requestFocus();
      }
    });

    events().mouseReleased().on(mouseEvent -> {
      if (isHovered() && isActive()) {
        onAction.run();
      }
    });

    events().keyPressed().on(keyEvent -> {
      if (!isDisabled()) {
        int keyCode = keyEvent.getKeyEvent().keyCode();

        if (keyCode == Key.SPACE.getCode()) {
          onAction.run();
        }
      }
    });
  }

  @Override
  public void renderContent(Renderer renderer) {
    renderChildren(renderer);
  }

  @Override
  public GuiElementDimensions calculateContentDimensions() {
    return null;
  }

  /**
   * Sets the action that fires when the button is interacted with.
   *
   * @param onAction the action to execute on button interaction
   * @return this
   */
  public ButtonGuiElement setOnAction(Runnable onAction) {
    this.onAction = onAction;

    return this;
  }
}
