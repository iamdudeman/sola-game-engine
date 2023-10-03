package technology.sola.engine.assets.graphics.gui;

import technology.sola.engine.graphics.guiv2.json.GuiDocumentJsonParser;
import technology.sola.engine.graphics.guiv2.json.element.SectionElementJsonDefinition;
import technology.sola.engine.graphics.guiv2.json.element.TextElementJsonDefinition;
import technology.sola.json.JsonObject;
import technology.sola.json.SolaJson;

import java.util.List;

// todo SolaWithDefaults could set initial GuiDocumentJsonParser GuiElementJsonDefinitions and then allow adding new ones
// todo need to figure out how to link GuiDocumentJsonParser and AssetLoader stuff for this
public class GuiAsJsonTempTesting {
  public static void main(String[] args) {
    String exampleJson = """
    {
      "tag": "Section",
      "props": {

      },
      "styles": {
        "gap": 5
      },
      "children": [
        {
          "tag": "Text",
          "props": {
            "text": "Hello world"
          },
          "styles": {
            "fontAssetId": "default"
          },
          "children": []
        }
      ]
    }
    """;
    JsonObject jsonObject = new SolaJson().parse(exampleJson).asObject();

    GuiDocumentJsonParser guiDocumentJsonParser = new GuiDocumentJsonParser(List.of(
      new SectionElementJsonDefinition(),
      new TextElementJsonDefinition()
    ));

//    var result = new SectionElementJsonDefinition().doTheThing(jsonObject);
    var result = guiDocumentJsonParser.parse(jsonObject);

    System.out.println(result);
  }
}
