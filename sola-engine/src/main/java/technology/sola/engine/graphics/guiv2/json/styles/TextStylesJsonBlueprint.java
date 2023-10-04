package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.json.JsonElement;

public class TextStylesJsonBlueprint implements StylesJsonBlueprint<TextStyles.Builder<?>> {
  private final BaseStylesJsonBlueprint baseStylesJsonDefinition = new BaseStylesJsonBlueprint();

  @Override
  public void parseStylesJsonValue(TextStyles.Builder<?> stylesBuilder, String propertyKey, JsonElement value) {
    switch (propertyKey) {
      case "fontAssetId" -> stylesBuilder.setFontAssetId(StylesJsonBlueprintUtils.parseString(value));
      case "textColor" -> stylesBuilder.setTextColor(StylesJsonBlueprintUtils.parseColor(value));
      default -> baseStylesJsonDefinition.parseStylesJsonValue(stylesBuilder, propertyKey, value);
    }
  }
}
