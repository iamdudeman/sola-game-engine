package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.Color;
import technology.sola.json.JsonElement;

public class StylesJsonBlueprintUtils {
  public static String parseString(JsonElement value) {
    return value.isNull() ? null : value.asString();
  }

  public static Integer parseInteger(JsonElement value) {
    return value.isNull() ? null : value.asInt();
  }

  public static String parseStyleValueAsString(JsonElement value) {
    return switch (value.getType()) {
      case NULL -> null;
      case LONG -> "" + value.asInt();
      case STRING -> value.asString();
      default -> throw new IllegalArgumentException("Unrecognized StyleValue format [" + value + "]");
    };
  }

  public static Color parseColor(JsonElement value) {
    if (value.isNull()) {
      return null;
    }

    String colorString = value.asString().toLowerCase();

    if (colorString.startsWith("rgb")) {
      String[] parts = colorString.replace("rgb(", "").replace(")", "").split(",");

      return new Color(
        Integer.parseInt(parts[0].trim()),
        Integer.parseInt(parts[1].trim()),
        Integer.parseInt(parts[2].trim())
      );
    } else if (colorString.startsWith("argb")) {
      String[] parts = colorString.replace("argb(", "").replace(")", "").split(",");

      return new Color(
        Integer.parseInt(parts[0].trim()),
        Integer.parseInt(parts[1].trim()),
        Integer.parseInt(parts[2].trim()),
        Integer.parseInt(parts[3].trim())
      );
    } else if (colorString.startsWith("#")) {
      return new Color(Long.decode(colorString.replace("#", "0x")).intValue());
    }

    throw new IllegalArgumentException("Unrecognized color format [" + colorString + "]");
  }
}
