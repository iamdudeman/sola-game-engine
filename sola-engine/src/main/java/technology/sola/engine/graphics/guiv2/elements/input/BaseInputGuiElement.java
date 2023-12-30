package technology.sola.engine.graphics.guiv2.elements.input;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;

public abstract class BaseInputGuiElement<Style extends BaseStyles> extends GuiElement<Style> {
  // props
  private boolean isDisabled;

  public BaseInputGuiElement() {
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
    if (this.isDisabled != disabled) {
      boolean shouldRefocusParent = disabled && getParent() != null && isFocussed();

      isDisabled = disabled;
      styleContainer.invalidate();

      if (shouldRefocusParent) {
        getParent().requestFocus();
      }
    }
  }

  @Override
  public boolean isFocusable() {
    return super.isFocusable() && !isDisabled;
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
