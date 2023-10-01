package technology.sola.engine.graphics.guiv2.json;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.elements.SectionElement;
import technology.sola.engine.graphics.guiv2.elements.TextGuiElement;
import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonObject;
import technology.sola.json.SolaJson;

import java.util.ArrayList;
import java.util.List;

// todo need to figure out how to link GuiDocumentJsonParser and AssetLoader stuff for this
public class GuiAsJsonTempTesting {
  public static void main(String[] args) {
    String exampleJson = """
    {
      "ele": "Section",
      "props": {

      },
      "styles": {
        "gap": 5
      },
      "children": [
        {
          "ele": "Text",
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

    GuiDocumentJsonParser guiDocumentJsonParser = new GuiDocumentJsonParser();

    guiDocumentJsonParser.jsonDefinitionList.add(new SectionElementJsonDefinition());
    guiDocumentJsonParser.jsonDefinitionList.add(new TextElementJsonDefinition());

//    var result = new SectionElementJsonDefinition().doTheThing(jsonObject);
    var result = guiDocumentJsonParser.parse(jsonObject);

    System.out.println(result);
  }


  public static class SectionElementJsonDefinition extends GuiElementJsonDefinition<BaseStyles, SectionElement, BaseStyles.Builder<?>> {
    public SectionElementJsonDefinition() {
      super(new BaseStylesJsonParser());
    }

    @Override
    public String getElementName() {
      return "Section";
    }

    @Override
    protected SectionElement buildElement(JsonObject propsJson) {
      return new SectionElement();
    }

    @Override
    protected BaseStyles.Builder<?> getBuilder() {
      return BaseStyles.create();
    }
  }

  public static class TextElementJsonDefinition extends GuiElementJsonDefinition<TextStyles, TextGuiElement, TextStyles.Builder<?>> {
    public TextElementJsonDefinition() {
      super(new TextStylesJsonParser());
    }

    @Override
    public String getElementName() {
      return "Text";
    }

    @Override
    protected TextGuiElement buildElement(JsonObject propsJson) {
      TextGuiElement textGuiElement = new TextGuiElement();

      textGuiElement.setText(propsJson.getString("text"));

      return textGuiElement;
    }

    @Override
    protected TextStyles.Builder<?> getBuilder() {
      return TextStyles.create();
    }
  }

  public static class TextStylesJsonParser implements StylesJsonParser<TextStyles.Builder<?>> {
    private final BaseStylesJsonParser baseStylesJsonBuilder = new BaseStylesJsonParser();

    @Override
    public TextStyles.Builder<?> populateStyleBuilder(JsonObject stylesJson, TextStyles.Builder<?> styleBuilder) {
      baseStylesJsonBuilder.populateStyleBuilder(stylesJson, styleBuilder);

      styleBuilder.setFontAssetId(stylesJson.getString("fontAssetId"));

      return styleBuilder;
    }
  }

  public static class BaseStylesJsonParser implements StylesJsonParser<BaseStyles.Builder<?>> {
    @Override
    public BaseStyles.Builder<?> populateStyleBuilder(JsonObject stylesJson, BaseStyles.Builder<?> styleBuilder) {
      // todo more of these
      Integer gap = stylesJson.getInt("gap", null);

      styleBuilder.setGap(gap);

      return styleBuilder;
    }
  }

  public interface StylesJsonParser<Builder extends BaseStyles.Builder<?>> {
    Builder populateStyleBuilder(JsonObject stylesJson, Builder styleBuilder);
  }

  public abstract static class GuiElementJsonDefinition<Style extends BaseStyles, Element extends GuiElement<Style>, Builder extends BaseStyles.Builder<?>> {
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
      var builder = stylesJsonParser.populateStyleBuilder(elementJson.getObject("styles"), getBuilder());
      element.setStyle((Style) builder.build());

      // todo need to handle children as well

      return element;
    }

    protected abstract Element buildElement(JsonObject propsJson);

    protected abstract Builder getBuilder();
  }

  public static class GuiDocumentJsonParser {
    private List<GuiElementJsonDefinition<?, ?, ?>> jsonDefinitionList = new ArrayList<>();

    public GuiElement<?> parse(JsonObject documentJson) {
      String elementId = documentJson.getString("ele");

      var theDefinition = jsonDefinitionList.stream().filter(definition -> definition.getElementName().equals(elementId)).findFirst().orElseThrow();

      var element = theDefinition.doTheThing(documentJson);

      // todo should be nullable
      documentJson.getArray("children").stream().map(childJson -> {
        return parse(childJson.asObject());
      }).forEach(element::appendChildren);

      return element;
    }
  }
}
