package technology.sola.engine.assets.gui.styles;

import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonObject;

public interface StylesJsonParser<Builder extends BaseStyles.Builder<?>> {
  Builder populateStyles(JsonObject stylesJson, Builder styleBuilder);
}
