package technology.sola.engine.graphics.gui.json.element;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.graphics.gui.json.styles.TextStylesJsonValueParser;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.TextStyles;
import technology.sola.json.JsonObject;

/**
 * A {@link GuiElementJsonBlueprint} for {@link TextGuiElement}.
 */
@NullMarked
public class TextElementJsonBlueprint extends GuiElementJsonBlueprint<TextStyles, TextGuiElement, TextStyles.Builder<?>> {
  /**
   * Creates an instance of this {@link GuiElementJsonBlueprint}.
   */
  public TextElementJsonBlueprint() {
    super(new TextStylesJsonValueParser());
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
