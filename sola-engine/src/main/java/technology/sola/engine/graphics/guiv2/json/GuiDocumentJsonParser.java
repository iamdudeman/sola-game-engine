package technology.sola.engine.graphics.guiv2.json;

import technology.sola.engine.graphics.guiv2.json.element.GuiElementJsonDefinition;
import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.json.JsonObject;

import java.util.List;

public class GuiDocumentJsonParser {
  public static final String ATTR_TAG = "tag";
  public static final String ATTR_CHILDREN = "children";
  public static final String ATTR_PROPS = "props";
  public static final String ATTR_STYLES = "styles";
  private final List<GuiElementJsonDefinition<?, ?, ?>> jsonDefinitionList;

  public GuiDocumentJsonParser(List<GuiElementJsonDefinition<?, ?, ?>> jsonDefinitionList) {
    this.jsonDefinitionList = jsonDefinitionList;
  }

  public GuiElement<?> parse(JsonObject documentJson) {
    String elementId = documentJson.getString(ATTR_TAG);

    var theDefinition = jsonDefinitionList.stream().filter(definition -> definition.getTag().equals(elementId)).findFirst().orElseThrow();

    var element = theDefinition.doTheThing(documentJson);

    // todo should be nullable
    documentJson.getArray(ATTR_CHILDREN).stream().map(childJson -> parse(childJson.asObject())).forEach(element::appendChildren);

    return element;
  }
}
