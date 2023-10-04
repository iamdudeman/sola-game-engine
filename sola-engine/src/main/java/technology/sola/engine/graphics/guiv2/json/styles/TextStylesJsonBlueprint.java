package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.engine.graphics.guiv2.json.exception.UnsupportedStylesPropertyException;
import technology.sola.json.JsonObject;

public class TextStylesJsonBlueprint implements StylesJsonBlueprint<TextStyles.Builder<?>> {
  private final BaseStylesJsonBlueprint baseStylesJsonDefinition = new BaseStylesJsonBlueprint();

  @Override
  public TextStyles.Builder<?> populateStylesBuilderFromJson(TextStyles.Builder<?> stylesBuilder, JsonObject stylesJson) {
    baseStylesJsonDefinition.populateStylesBuilderFromJson(stylesBuilder, stylesJson);

    stylesJson.forEach((key, value) -> {
        switch (key) {
            case "fontAssetId" -> stylesBuilder.setFontAssetId(StylesJsonBlueprintUtils.parseString(value));
            case "textColor" -> stylesBuilder.setTextColor(StylesJsonBlueprintUtils.parseColor(value));
            default -> throw new UnsupportedStylesPropertyException(key, TextStyles.class);
        }
    });

    return stylesBuilder;
  }
}
