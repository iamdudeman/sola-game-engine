package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonObject;

@FunctionalInterface
public interface StylesJsonBlueprint<Builder extends BaseStyles.Builder<?>> {
  Builder populateStylesBuilderFromJson(Builder stylesBuilder, JsonObject stylesJson);
}
