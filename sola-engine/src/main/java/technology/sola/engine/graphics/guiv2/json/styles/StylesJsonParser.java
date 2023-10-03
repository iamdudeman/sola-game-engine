package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonObject;

// todo better name
public interface StylesJsonParser<Builder extends BaseStyles.Builder<?>> {
  // todo better name
  Builder populateStyles(JsonObject stylesJson, Builder styleBuilder);
}
