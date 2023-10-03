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

  // todo better name
  public Element doTheThing(JsonObject elementJson) {
    // todo props shouldn't be required
    Element element = createElement(elementJson.getObject("props"));

    // todo styles shouldn't be required
    var builder = stylesJsonParser.populateStyles(elementJson.getObject("styles"), createStylesBuilder());
    element.setStyle((Styles) builder.build());

    // todo need to handle children as well

    return element;
  }

  protected abstract Element createElement(JsonObject propsJson);

  protected abstract StylesBuilder createStylesBuilder();
}
