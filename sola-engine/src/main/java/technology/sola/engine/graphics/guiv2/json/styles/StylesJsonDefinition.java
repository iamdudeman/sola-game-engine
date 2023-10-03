package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonObject;

public interface StylesJsonDefinition<Builder extends BaseStyles.Builder<?>> {
  Builder populateStylesBuilder(JsonObject stylesJson, Builder stylesBuilder);
}
