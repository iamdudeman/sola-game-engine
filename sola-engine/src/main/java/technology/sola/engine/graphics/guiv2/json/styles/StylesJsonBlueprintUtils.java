package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.Color;
import technology.sola.json.JsonElement;

public class StylesJsonBlueprintUtils {
  public static String parseString(JsonElement value) {
    return value.isNull() ? null : value.asString();
  }

  public static Color parseColor(JsonElement value) {
    // todo null
    // todo rgb
    // todo argb
    // todo #00ff00

    // todo implement
    return null;
  }
}
