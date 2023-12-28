package technology.sola.engine.graphics.guiv2.json.element.input;

import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.engine.graphics.guiv2.elements.input.TextInputGuiElement;
import technology.sola.engine.graphics.guiv2.json.element.GuiElementJsonBlueprint;
import technology.sola.engine.graphics.guiv2.json.styles.TextStylesJsonValueParser;
import technology.sola.json.JsonObject;

// todo probably needs its own styles and props instead of reusing Text

/**
 * A {@link GuiElementJsonBlueprint} for {@link TextInputGuiElement}.
 */
public class TextInputElementJsonBlueprint extends GuiElementJsonBlueprint<TextStyles, TextInputGuiElement, TextStyles.Builder<?>> {
  /**
   * Creates an instance of this {@link GuiElementJsonBlueprint}.
   */
  public TextInputElementJsonBlueprint() {
    super(new TextStylesJsonValueParser());
  }

  @Override
  public String getTag() {
    return "TextInput";
  }

  @Override
  public TextInputGuiElement createElementFromJson(JsonObject propsJson) {
    TextInputGuiElement textGuiElement = new TextInputGuiElement();

    textGuiElement.setText(propsJson.getString("text", null));

    return textGuiElement;
  }

  @Override
  protected TextStyles.Builder<?> createStylesBuilder() {
    return TextStyles.create();
  }
}
