package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.json.JsonObject;

public class TextStylesJsonBlueprint implements StylesJsonBlueprint<TextStyles.Builder<?>> {
  private final BaseStylesJsonBlueprint baseStylesJsonDefinition = new BaseStylesJsonBlueprint();

  @Override
  public TextStyles.Builder<?> populateStylesBuilderFromJson(TextStyles.Builder<?> stylesBuilder, JsonObject stylesJson) {
    baseStylesJsonDefinition.populateStylesBuilderFromJson(stylesBuilder, stylesJson);

    // todo more to add
    stylesBuilder.setFontAssetId(stylesJson.getString("fontAssetId", null));

    return stylesBuilder;
  }
}
