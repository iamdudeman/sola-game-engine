package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.json.JsonElement;

/**
 * A {@link StylesJsonValueParser} implementation for {@link TextStyles.Builder}.
 */
public class TextStylesJsonValueParser implements StylesJsonValueParser<TextStyles.Builder<?>> {
  private final BaseStylesJsonValueParser baseStylesJsonDefinition = new BaseStylesJsonValueParser();

  @Override
  public void setPropertyFromJson(TextStyles.Builder<?> stylesBuilder, String propertyKey, JsonElement value) {
    switch (propertyKey) {
      case "fontAssetId" -> stylesBuilder.setFontAssetId(StylesJsonBlueprintUtils.parseString(value));
      case "textColor" -> stylesBuilder.setTextColor(StylesJsonBlueprintUtils.parseColor(value));
      default -> baseStylesJsonDefinition.setPropertyFromJson(stylesBuilder, propertyKey, value);
    }
  }
}
