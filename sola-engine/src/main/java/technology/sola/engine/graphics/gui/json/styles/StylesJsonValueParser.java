package technology.sola.engine.graphics.gui.json.styles;

import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.json.JsonElement;

/**
 * StylesJsonValueParser handles parsing a style value from a {@link JsonElement} and setting it into a StylesBuilder.
 *
 * @param <Builder> the builder type extending {@link BaseStyles}
 */
@FunctionalInterface
public interface StylesJsonValueParser<Builder extends BaseStyles.Builder<?>> {
  /**
   * Sets the desired property in a StylesBuilder based on the {@link JsonElement} value
   *
   * @param stylesBuilder the StylesBuilder to set a property of
   * @param propertyKey   the property of the styles builder to set
   * @param value         the value to set into the builder
   */
  void setPropertyFromJson(Builder stylesBuilder, String propertyKey, JsonElement value);
}
