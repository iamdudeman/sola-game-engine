package technology.sola.engine.graphics.guiv2.json;

import technology.sola.engine.graphics.guiv2.json.element.GuiElementJsonDefinition;
import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonObject;

import java.util.List;

// todo better name
public class GuiDocumentJsonParser {
  private static final String ATTR_TAG = "tag";
  private static final String ATTR_CHILDREN = "children";
  private static final String ATTR_PROPS = "props";
  private static final String ATTR_STYLES = "styles";
  private final List<GuiElementJsonDefinition<?, ?, ?>> jsonDefinitionList;

  public GuiDocumentJsonParser(List<GuiElementJsonDefinition<?, ?, ?>> jsonDefinitionList) {
    this.jsonDefinitionList = jsonDefinitionList;
  }

  // todo better name
  public GuiElement<?> parse(JsonObject documentJson) {
    String elementTag = documentJson.getString(ATTR_TAG);

    GuiElementJsonDefinition<?, ?, ?> guiElementJsonDefinition = jsonDefinitionList.stream()
      .filter(definition -> definition.getTag().equals(elementTag))
      .findFirst()
      .orElseThrow();

    // todo props should be nullable
    GuiElement<?> element = guiElementJsonDefinition.createElement(documentJson.getObject(ATTR_PROPS));
    // todo styles should be nullable
    BaseStyles styles = guiElementJsonDefinition.createStyles(documentJson.getObject(ATTR_STYLES));

    ((GuiElement<BaseStyles>) element).setStyle(styles);

    // todo children should be nullable
    documentJson.getArray(ATTR_CHILDREN).stream().map(childJson -> parse(childJson.asObject())).forEach(element::appendChildren);

    return element;
  }
}
