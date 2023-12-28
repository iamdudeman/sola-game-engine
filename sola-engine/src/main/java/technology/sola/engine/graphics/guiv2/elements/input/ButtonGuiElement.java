package technology.sola.engine.graphics.guiv2.elements.input;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.GuiElementDimensions;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;

public class ButtonGuiElement extends GuiElement<BaseStyles> {
  // props
  private Runnable onAction = () -> {
  };
  private boolean isDisabled = false;

  // internal
  private boolean isHovered = false;
  private boolean isClicking = false;

  public ButtonGuiElement(BaseStyles... styles) {
    super(styles);

    events().mouseEntered().on(mouseEvent -> {
      isHovered = true;
    });

    events().mouseExited().on(mouseEvent -> {
      isHovered = false;
    });

    events().mousePressed().on(mouseEvent -> {
      if (!isDisabled) {
        isClicking = true;
      }
    });

    events().mouseReleased().on(mouseEvent -> {
      if (isClicking && isHovered) {
        requestFocus();
        onAction.run();
      }

      isClicking = false;
    });

    events().keyPressed().on(keyEvent -> {
      if (!isDisabled) {
        int keyCode = keyEvent.getKeyEvent().keyCode();

        if (keyCode == Key.ENTER.getCode() || keyCode == Key.CARRIAGE_RETURN.getCode() || keyCode == Key.SPACE.getCode()) {
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

  public void setOnAction(Runnable onAction) {
    this.onAction = onAction;
  }

  public boolean isDisabled() {
    return isDisabled;
  }

  public void setDisabled(boolean disabled) {
    isDisabled = disabled;
  }
}
