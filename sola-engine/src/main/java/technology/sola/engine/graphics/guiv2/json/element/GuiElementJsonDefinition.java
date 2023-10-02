package technology.sola.engine.graphics.guiv2.json.element;

import technology.sola.engine.graphics.guiv2.json.styles.StylesJsonParser;
import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonObject;

public abstract class GuiElementJsonDefinition<Style extends BaseStyles, Element extends GuiElement<Style>, Builder extends BaseStyles.Builder<?>> {
  protected StylesJsonParser<Builder> stylesJsonParser;

  public GuiElementJsonDefinition(StylesJsonParser<Builder> stylesJsonParser) {
    this.stylesJsonParser = stylesJsonParser;
  }

  public abstract String getElementName();

  // todo better name
  public Element doTheThing(JsonObject elementJson) {
    // todo props shouldn't be required
    Element element = buildElement(elementJson.getObject("props"));

    // todo styles shouldn't be required
    var builder = stylesJsonParser.populateStyles(elementJson.getObject("styles"), getBuilder());
    element.setStyle((Style) builder.build());

    // todo need to handle children as well

    return element;
  }

  protected abstract Element buildElement(JsonObject propsJson);

  protected abstract Builder getBuilder();
}
