package technology.sola.engine.graphics.guiv2.json.element;

import technology.sola.engine.graphics.guiv2.json.styles.StylesJsonParser;
import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonObject;

public abstract class GuiElementJsonDefinition<Styles extends BaseStyles, Element extends GuiElement<Styles>, StylesBuilder extends BaseStyles.Builder<?>> {
  protected StylesJsonParser<StylesBuilder> stylesJsonParser;

  public GuiElementJsonDefinition(StylesJsonParser<StylesBuilder> stylesJsonParser) {
    this.stylesJsonParser = stylesJsonParser;
  }

  public abstract String getTag();

  public Styles createStyles(JsonObject stylesJson) {
    return (Styles) stylesJsonParser.populateStyles(stylesJson, createStylesBuilder()).build();
  }

  public abstract Element createElement(JsonObject propsJson);

  protected abstract StylesBuilder createStylesBuilder();
}
