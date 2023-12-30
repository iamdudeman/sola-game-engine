package technology.sola.engine.graphics.guiv2.json.element.input;

import technology.sola.engine.graphics.guiv2.elements.input.TextInputGuiElement;
import technology.sola.engine.graphics.guiv2.elements.input.TextInputStyles;
import technology.sola.engine.graphics.guiv2.json.element.GuiElementJsonBlueprint;
import technology.sola.engine.graphics.guiv2.json.styles.input.TextInputStylesJsonValueParser;
import technology.sola.json.JsonObject;

/**
 * A {@link GuiElementJsonBlueprint} for {@link TextInputGuiElement}.
 */
public class TextInputElementJsonBlueprint extends GuiElementJsonBlueprint<TextInputStyles, TextInputGuiElement, TextInputStyles.Builder<?>> {
  /**
   * Creates an instance of this {@link GuiElementJsonBlueprint}.
   */
  public TextInputElementJsonBlueprint() {
    super(new TextInputStylesJsonValueParser());
  }

  @Override
  public String getTag() {
    return "TextInput";
  }

  @Override
  public TextInputGuiElement createElementFromJson(JsonObject propsJson) {
    TextInputGuiElement textInputGuiElement = new TextInputGuiElement();

    textInputGuiElement.setValue(propsJson.getString("value", ""));
    textInputGuiElement.setMaxLength(propsJson.getInt("maxLength", null));
    textInputGuiElement.setPlaceholder(propsJson.getString("placeholder", ""));
    textInputGuiElement.setDisabled(propsJson.getBoolean("disabled", false));

    return textInputGuiElement;
  }

  @Override
  protected TextInputStyles.Builder<?> createStylesBuilder() {
    return TextInputStyles.create();
  }
}
