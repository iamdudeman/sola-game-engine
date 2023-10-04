package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.property.Visibility;
import technology.sola.json.JsonElement;
import technology.sola.json.JsonObject;

/**
 * {@link StylesJsonBlueprint} for populating a {@link BaseStyles.Builder} from a styles {@link JsonObject}.
 */
public class BaseStylesJsonBlueprint implements StylesJsonBlueprint<BaseStyles.Builder<?>> {
  @Override
  public BaseStyles.Builder<?> populateStylesBuilderFromJson(BaseStyles.Builder<?> stylesBuilder, JsonObject stylesJson) {
    stylesJson.forEach((key, value) -> {
      switch (key) {
        case "backgroundColor" -> stylesBuilder.setBackgroundColor(StylesJsonBlueprintUtils.parseColor(value));
        case "visibility" -> stylesBuilder.setVisibility(parseVisibility(value));
      }
    });

    return stylesBuilder;
  }

  private Visibility parseVisibility(JsonElement value) {
    if (value.isNull()) {
      return null;
    }

    String visibilityString = value.asString();

    return switch (visibilityString) {
      case "hidden" -> Visibility.HIDDEN;
      case "none" -> Visibility.NONE;
      case "visible" -> Visibility.VISIBLE;
      default -> throw new IllegalArgumentException("Unrecognized visibility [" + visibilityString + "]");
    };
  }

  /*
  //    todo border;
  //    todo outline;
  //    todo padding;
  //    todo width;
  //    todo height;
  //
  //    todo gap;
  //    todo direction;
  //    todo mainAxisChildren;
  //    todo crossAxisChildren;
   */
}
