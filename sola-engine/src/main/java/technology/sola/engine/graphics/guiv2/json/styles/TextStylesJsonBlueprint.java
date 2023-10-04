package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.json.JsonObject;

/**
 * {@link StylesJsonBlueprint} for populating a {@link TextStyles.Builder} from a styles {@link JsonObject}.
 */
public class TextStylesJsonBlueprint implements StylesJsonBlueprint<TextStyles.Builder<?>> {
  private final BaseStylesJsonBlueprint baseStylesJsonDefinition = new BaseStylesJsonBlueprint();

  @Override
  public TextStyles.Builder<?> populateStylesBuilderFromJson(TextStyles.Builder<?> stylesBuilder, JsonObject stylesJson) {
    baseStylesJsonDefinition.populateStylesBuilderFromJson(stylesBuilder, stylesJson);

    stylesJson.forEach((key, value) -> {
      switch (key) {
        case "fontAssetId" -> stylesBuilder.setFontAssetId(StylesJsonBlueprintUtils.parseString(value));
        case "textColor" -> stylesBuilder.setTextColor(StylesJsonBlueprintUtils.parseColor(value));
      }
    });

    return stylesBuilder;
  }
}
