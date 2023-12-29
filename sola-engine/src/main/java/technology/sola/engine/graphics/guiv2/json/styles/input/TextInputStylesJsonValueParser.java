package technology.sola.engine.graphics.guiv2.json.styles.input;

import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.engine.graphics.guiv2.elements.input.TextInputStyles;
import technology.sola.engine.graphics.guiv2.json.styles.StylesJsonBlueprintUtils;
import technology.sola.engine.graphics.guiv2.json.styles.StylesJsonValueParser;
import technology.sola.engine.graphics.guiv2.json.styles.TextStylesJsonValueParser;
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
