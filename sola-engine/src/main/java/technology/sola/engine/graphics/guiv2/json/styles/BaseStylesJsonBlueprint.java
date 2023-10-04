package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonObject;

/**
 * {@link StylesJsonBlueprint} for populating a {@link BaseStyles.Builder} from a styles {@link JsonObject}.
 */
public class BaseStylesJsonBlueprint implements StylesJsonBlueprint<BaseStyles.Builder<?>> {
  @Override
  public BaseStyles.Builder<?> populateStylesBuilderFromJson(BaseStyles.Builder<?> stylesBuilder, JsonObject stylesJson) {
    // todo more of these
    Integer gap = stylesJson.getInt("gap", null);

    stylesBuilder.setGap(gap);

    return stylesBuilder;
  }

  /*
  //    todo background;
  //    todo border;
  //    todo outline;
  //    todo padding;
  //    todo width;
  //    todo height;
  //    todo visibility;
  //
  //    todo gap;
  //    todo direction;
  //    todo mainAxisChildren;
  //    todo crossAxisChildren;
   */
}
