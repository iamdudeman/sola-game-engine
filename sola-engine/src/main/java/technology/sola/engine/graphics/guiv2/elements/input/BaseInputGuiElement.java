package technology.sola.engine.graphics.guiv2.elements.input;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;

public abstract class BaseInputGuiElement<Style extends BaseStyles> extends GuiElement<Style> {
  // props
  private boolean isDisabled;

  @SafeVarargs
  public BaseInputGuiElement(Style... styles) {
    super(styles);

    events().mousePressed().on(mouseEvent -> {
      if (!isDisabled) {
        requestFocus();
      }
    });
  }

  public boolean isDisabled() {
    return isDisabled;
  }

  public void setDisabled(boolean disabled) {
    isDisabled = disabled;
  }

  @Override
  public boolean isHovered() {
    return super.isHovered() && !isDisabled;
  }

  @Override
  public boolean isActive() {
    return super.isActive() && !isDisabled;
  }
}
