package technology.sola.engine.graphics.gui.json.element;

import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.json.styles.BaseStylesJsonValueParser;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.json.JsonObject;

/**
 * A {@link GuiElementJsonBlueprint} for {@link ButtonGuiElement}.
 */
public class ButtonElementJsonBlueprint extends GuiElementJsonBlueprint<BaseStyles, ButtonGuiElement, BaseStyles.Builder<?>> {
  /**
   * Creates an instance of this {@link GuiElementJsonBlueprint}.
   */
  public ButtonElementJsonBlueprint() {
    super(new BaseStylesJsonValueParser());
  }

  @Override
  public String getTag() {
    return "Button";
  }

  @Override
  public ButtonGuiElement createElementFromJson(JsonObject propsJson) {
    ButtonGuiElement buttonGuiElement = new ButtonGuiElement();

    buttonGuiElement.setDisabled(propsJson.getBoolean("disabled", false));

    return buttonGuiElement;
  }

  @Override
  protected BaseStyles.Builder<?> createStylesBuilder() {
    return BaseStyles.create();
  }
}
