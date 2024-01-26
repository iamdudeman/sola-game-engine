package technology.sola.engine.graphics.guiv2.elements.input;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;

/**
 * BaseInputGuiElement provides basic functionality for {@link GuiElement}s that allow users to interact with them via
 * keyboard or mouse events.
 *
 * @param <Style> the style type
 */
public abstract class BaseInputGuiElement<Style extends BaseStyles> extends GuiElement<Style> {
  // props
  private boolean isDisabled;

  /**
   * Creates a new BaseInputGuiElement instance registering a mouse event for focussing the element when pressed.
   */
  public BaseInputGuiElement() {
    events().mousePressed().on(mouseEvent -> {
      if (!isDisabled) {
        requestFocus();
      }
    });
  }

  /**
   * A disabled input cannot be focussed, hovered or active.
   *
   * @return true if the input is disabled
   */
  public boolean isDisabled() {
    return isDisabled;
  }

  /**
   * Updates the disabled state of the input. If the input is currently focussed then focus will return to its parent.
   *
   * @param disabled the new disabled state
   */
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
