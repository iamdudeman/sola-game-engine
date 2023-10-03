package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.json.JsonObject;

public class TextStylesJsonDefinition implements StylesJsonDefinition<TextStyles.Builder<?>> {
  private final BaseStylesJsonDefinition baseStylesJsonDefinition = new BaseStylesJsonDefinition();

  @Override
  public TextStyles.Builder<?> populateStylesBuilder(JsonObject stylesJson, TextStyles.Builder<?> stylesBuilder) {
    // todo more to add
    baseStylesJsonDefinition.populateStylesBuilder(stylesJson, stylesBuilder);

    stylesBuilder.setFontAssetId(stylesJson.getString("fontAssetId"));

    return stylesBuilder;
  }
}
