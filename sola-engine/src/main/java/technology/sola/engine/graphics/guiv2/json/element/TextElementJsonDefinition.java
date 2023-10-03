package technology.sola.engine.graphics.guiv2.json.element;

import technology.sola.engine.graphics.guiv2.json.styles.TextStylesJsonParser;
import technology.sola.engine.graphics.guiv2.elements.TextGuiElement;
import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.json.JsonObject;

/**
 * A {@link GuiElementJsonDefinition} for {@link TextGuiElement}.
 */
public class TextElementJsonDefinition extends GuiElementJsonDefinition<TextStyles, TextGuiElement, TextStyles.Builder<?>> {
  /**
   * Creates an instance of this {@link GuiElementJsonDefinition}.
   */
  public TextElementJsonDefinition() {
    super(new TextStylesJsonParser());
  }

  @Override
  public String getTag() {
    return "Text";
  }

  @Override
  public TextGuiElement createElement(JsonObject propsJson) {
    TextGuiElement textGuiElement = new TextGuiElement();

    textGuiElement.setText(propsJson.getString("text", null));

    return textGuiElement;
  }

  @Override
  protected TextStyles.Builder<?> createStylesBuilder() {
    return TextStyles.create();
  }
}
