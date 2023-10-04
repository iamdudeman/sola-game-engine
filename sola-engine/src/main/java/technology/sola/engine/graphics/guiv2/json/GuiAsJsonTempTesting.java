package technology.sola.engine.graphics.guiv2.json;

import technology.sola.engine.graphics.guiv2.json.element.SectionElementJsonBlueprint;
import technology.sola.engine.graphics.guiv2.json.element.TextElementJsonBlueprint;
import technology.sola.json.JsonObject;
import technology.sola.json.SolaJson;

import java.util.List;

// todo delete this file after testing
@Deprecated
public class GuiAsJsonTempTesting {
  public static void main(String[] args) {
    String exampleJson = """
    {
      "tag": "Section",
      "styles": {
        "backgroundColor": "rgb(250, 240, 100)",
        "visibility": "visible"
      },
      "children": [
        {
          "tag": "Text",
          "props": {
            "text": "Hello world"
          },
          "styles": {
            "textColor": "#aabbccdd"
          }
        }
      ]
    }
    """;
    JsonObject jsonObject = new SolaJson().parse(exampleJson).asObject();

    GuiJsonDocumentBuilder guiJsonDocumentBuilder = new GuiJsonDocumentBuilder(List.of(
      new SectionElementJsonBlueprint(),
      new TextElementJsonBlueprint()
    ));

    var result = guiJsonDocumentBuilder.build(jsonObject);

    System.out.println(result);
  }
}
