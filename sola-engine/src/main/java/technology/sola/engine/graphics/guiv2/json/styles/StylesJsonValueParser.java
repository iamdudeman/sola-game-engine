package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonElement;

// todo consider builder pattern here instead maybe? Then "getBuilder" from element blueprint could be removed I think
//    setValue(String propertyKey, JsonElement value)
//    build() -> Styles instance

@FunctionalInterface
public interface StylesJsonValueParser<Builder extends BaseStyles.Builder<?>> {
  void parse(Builder stylesBuilder, String propertyKey, JsonElement value);
}
