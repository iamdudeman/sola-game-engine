package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonElement;

@FunctionalInterface
public interface StylesJsonBlueprint<Builder extends BaseStyles.Builder<?>> {
  void parseStylesJsonValue(Builder stylesBuilder, String propertyKey, JsonElement value);
}
