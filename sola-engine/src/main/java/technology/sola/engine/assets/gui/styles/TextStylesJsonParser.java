package technology.sola.engine.assets.gui.styles;

import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.json.JsonObject;

public class TextStylesJsonParser implements StylesJsonParser<TextStyles.Builder<?>> {
  private final BaseStylesJsonParser baseStylesJsonBuilder = new BaseStylesJsonParser();

  @Override
  public TextStyles.Builder<?> populateStyles(JsonObject stylesJson, TextStyles.Builder<?> styleBuilder) {
    // todo more to add
    baseStylesJsonBuilder.populateStyles(stylesJson, styleBuilder);

    styleBuilder.setFontAssetId(stylesJson.getString("fontAssetId"));

    return styleBuilder;
  }
}
