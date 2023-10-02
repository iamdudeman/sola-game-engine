package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonObject;

public class BaseStylesJsonParser implements StylesJsonParser<BaseStyles.Builder<?>> {
  @Override
  public BaseStyles.Builder<?> populateStyles(JsonObject stylesJson, BaseStyles.Builder<?> styleBuilder) {
    // todo more of these
    Integer gap = stylesJson.getInt("gap", null);

    styleBuilder.setGap(gap);

    return styleBuilder;
  }
}
