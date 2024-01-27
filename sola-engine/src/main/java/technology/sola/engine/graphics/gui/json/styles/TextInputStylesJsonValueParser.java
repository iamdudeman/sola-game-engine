package technology.sola.engine.graphics.gui.json.styles;

import technology.sola.engine.graphics.gui.elements.TextStyles;
import technology.sola.engine.graphics.gui.elements.input.TextInputStyles;
import technology.sola.json.JsonElement;

/**
 * A {@link StylesJsonValueParser} implementation for {@link TextStyles.Builder}.
 */
public class TextInputStylesJsonValueParser implements StylesJsonValueParser<TextInputStyles.Builder<?>> {
  private final TextStylesJsonValueParser textStylesJsonValueParser = new TextStylesJsonValueParser();

  @Override
  public void setPropertyFromJson(TextInputStyles.Builder<?> stylesBuilder, String propertyKey, JsonElement value) {
    switch (propertyKey) {
      case "placeholderColor" -> stylesBuilder.setPlaceholderColor(StylesJsonBlueprintUtils.parseColor(value));
      default -> textStylesJsonValueParser.setPropertyFromJson(stylesBuilder, propertyKey, value);
    }
  }
}
