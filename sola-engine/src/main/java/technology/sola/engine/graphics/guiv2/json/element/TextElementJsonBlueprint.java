package technology.sola.engine.graphics.guiv2.json.element;

import technology.sola.engine.graphics.guiv2.json.styles.TextStylesJsonBlueprint;
import technology.sola.engine.graphics.guiv2.elements.TextGuiElement;
import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.json.JsonObject;

/**
 * A {@link GuiElementJsonBlueprint} for {@link TextGuiElement}.
 */
public class TextElementJsonBlueprint extends GuiElementJsonBlueprint<TextStyles, TextGuiElement, TextStyles.Builder<?>> {
  /**
   * Creates an instance of this {@link GuiElementJsonBlueprint}.
   */
  public TextElementJsonBlueprint() {
    super(new TextStylesJsonBlueprint());
  }

  @Override
  public String getTag() {
    return "Text";
  }

  @Override
  public TextGuiElement createElementFromJson(JsonObject propsJson) {
    TextGuiElement textGuiElement = new TextGuiElement();

    textGuiElement.setText(propsJson.getString("text", null));

    return textGuiElement;
  }

  @Override
  protected TextStyles.Builder<?> createStylesBuilder() {
    return TextStyles.create();
  }
}
