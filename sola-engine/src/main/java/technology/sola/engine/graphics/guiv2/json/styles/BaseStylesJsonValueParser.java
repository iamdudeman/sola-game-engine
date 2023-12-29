package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.guiv2.json.exception.UnsupportedStylesPropertyException;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.property.*;
import technology.sola.json.JsonElement;

/**
 * A {@link StylesJsonValueParser} implementation for {@link BaseStyles.Builder}.
 */
public class BaseStylesJsonValueParser implements StylesJsonValueParser<BaseStyles.Builder<?>> {
  @Override
  public void setPropertyFromJson(BaseStyles.Builder<?> stylesBuilder, String propertyKey, JsonElement value) {
    switch (propertyKey) {
      case "@condition" -> {
        // @condition is a valid property but does not apply to stylesBuilder
      }
      case "backgroundColor" -> stylesBuilder.setBackgroundColor(StylesJsonBlueprintUtils.parseColor(value));
      case "borderColor" -> stylesBuilder.setBorderColor(StylesJsonBlueprintUtils.parseColor(value));
      case "crossAxisChildren" -> stylesBuilder.setCrossAxisChildren(parseCrossAxisChildren(value));
      case "direction" -> stylesBuilder.setDirection(parseDirection(value));
      case "gap" -> stylesBuilder.setGap(StylesJsonBlueprintUtils.parseInteger(value));
      case "height" -> stylesBuilder.setHeight(StylesJsonBlueprintUtils.parseStyleValueAsString(value));
      case "mainAxisChildren" -> stylesBuilder.setMainAxisChildren(parseMainAxisChildren(value));
      case "padding" -> stylesBuilder.setPadding(StylesJsonBlueprintUtils.parseInteger(value));
      case "paddingBottom" -> stylesBuilder.setPaddingBottom(StylesJsonBlueprintUtils.parseInteger(value));
      case "paddingHorizontal" -> stylesBuilder.setPaddingHorizontal(StylesJsonBlueprintUtils.parseInteger(value));
      case "paddingLeft" -> stylesBuilder.setPaddingLeft(StylesJsonBlueprintUtils.parseInteger(value));
      case "paddingRight" -> stylesBuilder.setPaddingRight(StylesJsonBlueprintUtils.parseInteger(value));
      case "paddingTop" -> stylesBuilder.setPaddingTop(StylesJsonBlueprintUtils.parseInteger(value));
      case "paddingVertical" -> stylesBuilder.setPaddingVertical(StylesJsonBlueprintUtils.parseInteger(value));
      case "positionX" -> stylesBuilder.setPositionX(StylesJsonBlueprintUtils.parseStyleValueAsString(value));
      case "positionY" -> stylesBuilder.setPositionY(StylesJsonBlueprintUtils.parseStyleValueAsString(value));
      case "visibility" -> stylesBuilder.setVisibility(parseVisibility(value));
      case "width" -> stylesBuilder.setWidth(StylesJsonBlueprintUtils.parseStyleValueAsString(value));
      default -> throw new UnsupportedStylesPropertyException(propertyKey);
    }
  }

  private MainAxisChildren parseMainAxisChildren(JsonElement value) {
    if (value.isNull()) {
      return null;
    }

    String mainAxisChildrenString = value.asString().toLowerCase();

    return switch (mainAxisChildrenString) {
      case "start" -> MainAxisChildren.START;
      case "center" -> MainAxisChildren.CENTER;
      case "end" -> MainAxisChildren.END;
      default -> throw new IllegalArgumentException("Unrecognized mainAxisChildren [" + mainAxisChildrenString + "]");
    };
  }

  private CrossAxisChildren parseCrossAxisChildren(JsonElement value) {
    if (value.isNull()) {
      return null;
    }

    String crossAxisChildrenString = value.asString().toLowerCase();

    return switch (crossAxisChildrenString) {
      case "stretch" -> CrossAxisChildren.STRETCH;
      case "start" -> CrossAxisChildren.START;
      case "center" -> CrossAxisChildren.CENTER;
      case "end" -> CrossAxisChildren.END;
      default -> throw new IllegalArgumentException("Unrecognized crossAxisChildren [" + crossAxisChildrenString + "]");
    };
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
