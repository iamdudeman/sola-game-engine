package technology.sola.engine.graphics.guiv2.json;

import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.graphics.guiv2.json.element.GuiElementJsonBlueprint;
import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.json.exception.MissingGuiElementJsonBlueprintException;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonArray;
import technology.sola.json.JsonObject;

import java.util.List;

/**
 * GuiJsonDocumentBuilder handles building {@link GuiJsonDocument} instances from a {@link JsonObject}.
 */
public class GuiJsonDocumentBuilder {
  private static final String ATTR_TAG = "tag";
  private static final String ATTR_CHILDREN = "children";
  private static final String ATTR_PROPS = "props";
  private static final String ATTR_STYLES = "styles";
  private final List<GuiElementJsonBlueprint<?, ?, ?>> guiElementJsonBlueprints;

  /**
   * Creates an instance of GuiJsonDocumentBuilder with desired {@link GuiElementJsonBlueprint}s.
   *
   * @param guiElementJsonBlueprints the list of definitions to build from
   */
  public GuiJsonDocumentBuilder(List<GuiElementJsonBlueprint<?, ?, ?>> guiElementJsonBlueprints) {
    this.guiElementJsonBlueprints = guiElementJsonBlueprints;
  }

  /**
   * Builds a {@link GuiJsonDocument} from {@link JsonObject}.
   *
   * @param elementJson the JSON to build a {@code GuiJsonDocument} from
   * @return the {@code GuiJsonDocument}
   */
  public GuiJsonDocument build(JsonObject elementJson) {
    return new GuiJsonDocument(buildElement(elementJson));
  }

  @SuppressWarnings("unchecked")
  private GuiElement<?> buildElement(JsonObject elementJson) {
    String elementTag = elementJson.getString(ATTR_TAG);

    GuiElementJsonBlueprint<?, ?, ?> guiElementJsonBlueprint = guiElementJsonBlueprints.stream()
      .filter(definition -> definition.getTag().equals(elementTag))
      .findFirst()
      .orElseThrow(() -> new MissingGuiElementJsonBlueprintException(elementTag));

    GuiElement<?> element = guiElementJsonBlueprint.createElementFromJson(elementJson.getObject(ATTR_PROPS, new JsonObject()));
    BaseStyles styles = guiElementJsonBlueprint.createStylesFromJson(elementJson.getObject(ATTR_STYLES, new JsonObject()));

    element.setId(elementJson.getString("id", null));

    ((GuiElement<BaseStyles>) element).setStyle(styles);

    elementJson.getArray(ATTR_CHILDREN, new JsonArray()).stream().map(childJson -> buildElement(childJson.asObject())).forEach(element::appendChildren);

    return element;
  }
}
