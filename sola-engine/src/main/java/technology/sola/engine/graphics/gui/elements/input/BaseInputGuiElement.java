package technology.sola.engine.graphics.gui.elements.input;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.BaseTextGuiElement;
import technology.sola.engine.graphics.gui.properties.GuiPropertyDefaults;

public abstract class BaseInputGuiElement<T extends BaseInputGuiElement.Properties> extends BaseTextGuiElement<T> {
  public BaseInputGuiElement(SolaGuiDocument document, T properties) {
    super(document, properties);
  }

  @Override
  public boolean isHovered() {
    return super.isHovered() && !properties.isDisabled();
  }

  public static class Properties extends BaseTextGuiElement.Properties {
    private boolean isDisabled = false;
    private Color disabledBackgroundColor;

    public Properties(GuiPropertyDefaults propertyDefaults) {
      super(propertyDefaults);
      setFocusable(true);

      // property defaults
      setFocusOutlineColor(propertyDefaults.colorFocusOutline());
      setBorderColor(propertyDefaults.colorInputBorder());
      setDisabledBackgroundColor(propertyDefaults.colorInputDisabledBackground());
      hover.setBackgroundColor(propertyDefaults.colorInputHoverBackgroundColor());
    }

    @Override
    public boolean isFocusable() {
      return super.isFocusable() && !isDisabled();
    }

    @Override
    public Color getBackgroundColor() {
      return isDisabled() ? getDisabledBackgroundColor() : super.getBackgroundColor();
    }

    public boolean isDisabled() {
      return isDisabled;
    }

    public BaseInputGuiElement.Properties setDisabled(boolean disabled) {
      isDisabled = disabled;
      return this;
    }

    public Color getDisabledBackgroundColor() {
      return disabledBackgroundColor;
    }

    public BaseInputGuiElement.Properties setDisabledBackgroundColor(Color disabledBackgroundColor) {
      this.disabledBackgroundColor = disabledBackgroundColor;
      return this;
    }
  }
}
