package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonObject;

public class BaseStylesJsonDefinition implements StylesJsonDefinition<BaseStyles.Builder<?>> {
  @Override
  public BaseStyles.Builder<?> populateStylesBuilder(JsonObject stylesJson, BaseStyles.Builder<?> stylesBuilder) {
    // todo more of these
    Integer gap = stylesJson.getInt("gap", null);

    stylesBuilder.setGap(gap);

    return stylesBuilder;
  }
}
