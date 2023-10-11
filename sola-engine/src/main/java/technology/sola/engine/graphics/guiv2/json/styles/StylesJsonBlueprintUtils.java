package technology.sola.engine.graphics.guiv2.json.styles;

import technology.sola.engine.graphics.Color;
import technology.sola.json.JsonElement;

/**
 * A collection of common {@link JsonElement }parsing methods for use by {@link StylesJsonValueParser}s.
 */
public class StylesJsonBlueprintUtils {
  /**
   * Parses a String or null value from a {@link JsonElement}.
   *
   * @param value the {@code JsonElement} to parse
   * @return the parsed String or null
   */
  public static String parseString(JsonElement value) {
    return value.isNull() ? null : value.asString();
  }

  /**
   * Parses an Integer or null value from a {@link JsonElement}.
   *
   * @param value the {@code JsonElement} to parse
   * @return the parsed Integer or null
   */
  public static Integer parseInteger(JsonElement value) {
    return value.isNull() ? null : value.asInt();
  }

  /**
   * Parses a valid {@link technology.sola.engine.graphics.guiv2.style.property.StyleValue} value as a string or null
   * from a {@link JsonElement}.
   *
   * @param value the {@code JsonElement} to parse
   * @return a valid {@link technology.sola.engine.graphics.guiv2.style.property.StyleValue} as a string or null
   */
  public static String parseStyleValueAsString(JsonElement value) {
    return switch (value.getType()) {
      case NULL -> null;
      case LONG -> "" + value.asInt();
      case STRING -> value.asString();
      default -> throw new IllegalArgumentException("Unrecognized StyleValue format [" + value + "]");
    };
  }

  /**
   * Parses a {@link Color} from a {@link JsonElement}.
   * <p>
   * Valid formats:
   * <ul>
   *   <li><pre>rgb(int, int, int)</pre></li>
   *   <li><pre>argb(int, int, int, int)</pre></li>
   *   <li><pre>#aarrggbb</pre></li>
   * </ul>
   *
   * @param value the {@code JsonElement} to parse
   * @return the parsed Color or null
   */
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
      // rgb hash
      if (colorString.length() == 7) {
        return new Color(Long.decode(colorString.replace("#", "0xff")).intValue());
      }

      return new Color(Long.decode(colorString.replace("#", "0x")).intValue());
    }

    throw new IllegalArgumentException("Unrecognized color format [" + colorString + "]");
  }

  private StylesJsonBlueprintUtils() {
  }
}
