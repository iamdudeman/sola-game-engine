package technology.sola.engine.graphics.gui.json.styles;

import technology.sola.engine.graphics.gui.elements.TextStyles;
import technology.sola.json.JsonElement;

/**
 * A {@link StylesJsonValueParser} implementation for {@link TextStyles.Builder}.
 */
public class TextStylesJsonValueParser implements StylesJsonValueParser<TextStyles.Builder<?>> {
  private final BaseStylesJsonValueParser baseStylesJsonDefinition = new BaseStylesJsonValueParser();

  @Override
  public void setPropertyFromJson(TextStyles.Builder<?> stylesBuilder, String propertyKey, JsonElement value) {
    switch (propertyKey) {
      case "fontAssetId" -> stylesBuilder.setFontAssetId(StylesJsonBlueprintUtils.parseString(value));
      case "textColor" -> stylesBuilder.setTextColor(StylesJsonBlueprintUtils.parseColor(value));
      case "textAlignment" -> stylesBuilder.setTextAlignment(parseTextAlignment(value));
      default -> baseStylesJsonDefinition.setPropertyFromJson(stylesBuilder, propertyKey, value);
    }
  }

  private TextStyles.TextAlignment parseTextAlignment(JsonElement value) {
    if (value.isNull()) {
      return null;
    }

    String textAlignment = value.asString().toLowerCase();

    return switch (textAlignment) {
      case "start" -> TextStyles.TextAlignment.START;
      case "center" -> TextStyles.TextAlignment.CENTER;
      case "end" -> TextStyles.TextAlignment.END;
      default -> throw new IllegalArgumentException("Unrecognized textAlignment [" + textAlignment + "]");
    };
  }
}
