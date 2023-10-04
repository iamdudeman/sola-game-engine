package technology.sola.engine.graphics.guiv2.json.element;

import technology.sola.engine.graphics.guiv2.json.styles.StylesJsonBlueprint;
import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonObject;

public abstract class GuiElementJsonBlueprint<Styles extends BaseStyles, Element extends GuiElement<Styles>, StylesBuilder extends BaseStyles.Builder<?>> {
  protected StylesJsonBlueprint<StylesBuilder> stylesJsonBlueprint;

  public GuiElementJsonBlueprint(StylesJsonBlueprint<StylesBuilder> stylesJsonBlueprint) {
    this.stylesJsonBlueprint = stylesJsonBlueprint;
  }

  public abstract String getTag();

  public Styles createStylesFromJson(JsonObject stylesJson) {
    StylesBuilder stylesBuilder = createStylesBuilder();

    stylesJson.forEach((key, value) -> stylesJsonBlueprint.parseStylesJsonValue(stylesBuilder, key, value));

    return (Styles) stylesBuilder.build();
  }

  public abstract Element createElementFromJson(JsonObject propsJson);

  protected abstract StylesBuilder createStylesBuilder();
}
