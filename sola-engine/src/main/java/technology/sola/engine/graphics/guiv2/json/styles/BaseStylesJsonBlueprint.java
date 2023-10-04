package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.guiv2.json.exception.UnsupportedStylesPropertyException;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.property.Visibility;
import technology.sola.json.JsonElement;

public class BaseStylesJsonBlueprint implements StylesJsonBlueprint<BaseStyles.Builder<?>> {
  @Override
  public void parseStylesJsonValue(BaseStyles.Builder<?> stylesBuilder, String propertyKey, JsonElement value) {
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
    switch (propertyKey) {
      case "backgroundColor" -> stylesBuilder.setBackgroundColor(StylesJsonBlueprintUtils.parseColor(value));
      case "visibility" -> stylesBuilder.setVisibility(parseVisibility(value));
      default -> throw new UnsupportedStylesPropertyException(propertyKey);
    }
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
}
