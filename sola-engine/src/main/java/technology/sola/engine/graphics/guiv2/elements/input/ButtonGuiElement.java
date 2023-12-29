package technology.sola.engine.graphics.guiv2.elements.input;

import technology.sola.engine.graphics.guiv2.GuiElementDimensions;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;

public class ButtonGuiElement extends BaseInputGuiElement<BaseStyles> {
  // props
  private Runnable onAction = () -> {
  };

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

  public void setOnAction(Runnable onAction) {
    this.onAction = onAction;
  }
}
