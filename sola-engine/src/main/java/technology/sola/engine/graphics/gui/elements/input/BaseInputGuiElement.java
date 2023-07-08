package technology.sola.engine.graphics.gui.elements.input;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.elements.BaseTextGuiElement;
import technology.sola.engine.graphics.gui.properties.GuiPropertyDefaults;
import technology.sola.engine.graphics.renderer.Renderer;

public abstract class BaseInputGuiElement<T extends BaseInputGuiElement.Properties> extends BaseTextGuiElement<T> {
  public BaseInputGuiElement(SolaGuiDocument document, T properties) {
    super(document, properties);
  }

  @Override
  public boolean isHovered() {
    return super.isHovered() && !properties.isDisabled();
  }

  @Override
  public void render(Renderer renderer) {
    Color backgroundColor = properties.getBackgroundColor();
    if (properties().isDisabled()) {
      properties.setBackgroundColor(properties.getDisabledBackgroundColor());
    }
    super.render(renderer);
    properties.setBackgroundColor(backgroundColor);
  }

  public static class Properties extends BaseTextGuiElement.Properties {
    private boolean isDisabled = false;
    private Color disabledBackgroundColor;

    public Properties(GuiPropertyDefaults propertyDefaults) {
      super(propertyDefaults);
      setFocusable(true);

      setFocusOutlineColor(Color.BLACK);
      setBorderColor(Color.BLACK);
      setDisabledBackgroundColor(getBackgroundColor().shade(0.18f));

      hover.setBackgroundColor(getBackgroundColor().shade(0.1f));
    }

    @Override
    public boolean isFocusable() {
      return super.isFocusable() && !isDisabled();
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
