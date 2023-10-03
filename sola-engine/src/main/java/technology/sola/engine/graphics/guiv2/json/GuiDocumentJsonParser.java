package technology.sola.engine.graphics.guiv2.json;

import technology.sola.engine.graphics.guiv2.json.element.GuiElementJsonDefinition;
import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.json.JsonObject;

import java.util.List;

public class GuiDocumentJsonParser {
  private final List<GuiElementJsonDefinition<?, ?, ?>> jsonDefinitionList;

  public GuiDocumentJsonParser(List<GuiElementJsonDefinition<?, ?, ?>> jsonDefinitionList) {
    this.jsonDefinitionList = jsonDefinitionList;
  }

  public GuiElement<?> parse(JsonObject documentJson) {
    String elementId = documentJson.getString("ele");

    var theDefinition = jsonDefinitionList.stream().filter(definition -> definition.getElementName().equals(elementId)).findFirst().orElseThrow();

    var element = theDefinition.doTheThing(documentJson);

    // todo should be nullable
    documentJson.getArray("children").stream().map(childJson -> parse(childJson.asObject())).forEach(element::appendChildren);

    return element;
  }
}
