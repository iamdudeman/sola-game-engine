package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.guiv2.json.exception.UnsupportedStylesPropertyException;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.property.Direction;
import technology.sola.engine.graphics.guiv2.style.property.Visibility;
import technology.sola.json.JsonElement;

public class BaseStylesJsonBlueprint implements StylesJsonBlueprint<BaseStyles.Builder<?>> {
  @Override
  public void parseStylesJsonValue(BaseStyles.Builder<?> stylesBuilder, String propertyKey, JsonElement value) {
    /*
    //    todo border;
    //    todo outline;
    //    todo padding;
    //
    //    todo mainAxisChildren;
    //    todo crossAxisChildren;
     */
    switch (propertyKey) {
      case "backgroundColor" -> stylesBuilder.setBackgroundColor(StylesJsonBlueprintUtils.parseColor(value));
      case "direction" -> stylesBuilder.setDirection(parseDirection(value));
      case "gap" -> stylesBuilder.setGap(StylesJsonBlueprintUtils.parseInteger(value));
      case "height" -> stylesBuilder.setHeight(StylesJsonBlueprintUtils.parseStyleValue(value));
      case "visibility" -> stylesBuilder.setVisibility(parseVisibility(value));
      case "width" -> stylesBuilder.setWidth(StylesJsonBlueprintUtils.parseStyleValue(value));
      default -> throw new UnsupportedStylesPropertyException(propertyKey);
    }
  }

  private Direction parseDirection(JsonElement value) {
    if (value.isNull()) {
      return null;
    }

    String directionString = value.asString().toLowerCase();

    return switch (directionString) {
      case "column" -> Direction.COLUMN;
      case "column-reverse" -> Direction.COLUMN_REVERSE;
      case "row" -> Direction.ROW;
      case "row-reverse" -> Direction.ROW_REVERSE;
      default -> throw new IllegalArgumentException("Unrecognized direction [" + directionString + "]");
    };
  }

  private Visibility parseVisibility(JsonElement value) {
    if (value.isNull()) {
      return null;
    }

    String visibilityString = value.asString().toLowerCase();

    return switch (visibilityString) {
      case "hidden" -> Visibility.HIDDEN;
      case "none" -> Visibility.NONE;
      case "visible" -> Visibility.VISIBLE;
      default -> throw new IllegalArgumentException("Unrecognized visibility [" + visibilityString + "]");
    };
  }
}
