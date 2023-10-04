package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.json.JsonElement;

public class TextStylesJsonValueParser implements StylesJsonValueParser<TextStyles.Builder<?>> {
  private final BaseStylesJsonValueParser baseStylesJsonDefinition = new BaseStylesJsonValueParser();

  @Override
  public void parse(TextStyles.Builder<?> stylesBuilder, String propertyKey, JsonElement value) {
    switch (propertyKey) {
      case "fontAssetId" -> stylesBuilder.setFontAssetId(StylesJsonBlueprintUtils.parseString(value));
      case "textColor" -> stylesBuilder.setTextColor(StylesJsonBlueprintUtils.parseColor(value));
      default -> baseStylesJsonDefinition.parse(stylesBuilder, propertyKey, value);
    }
  }
}
