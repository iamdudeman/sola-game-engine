package technology.sola.engine.graphics.gui.json;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.graphics.gui.json.element.GuiElementJsonBlueprint;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.json.exception.MissingGuiElementJsonBlueprintException;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.engine.graphics.gui.style.theme.GuiTheme;
import technology.sola.json.JsonArray;
import technology.sola.json.JsonObject;

import java.util.List;
import java.util.stream.Stream;

/**
 * GuiJsonDocumentBuilder handles building {@link GuiJsonDocument} instances from a {@link JsonObject}.
 */
@NullMarked
public class GuiJsonDocumentBuilder {
  private static final String ATTR_TAG = "tag";
  private static final String ATTR_ID = "id";
  private static final String ATTR_CHILDREN = "children";
  private static final String ATTR_PROPS = "props";
  private static final String ATTR_STYLES = "styles";
  private final GuiTheme guiTheme;
  private final List<GuiElementJsonBlueprint<?, ?, ?>> guiElementJsonBlueprints;

  /**
   * Creates an instance of GuiJsonDocumentBuilder with desired {@link GuiElementJsonBlueprint}s.
   *
   * @param guiTheme                 the {@link GuiTheme} to apply to elements parsed from JSON
   * @param guiElementJsonBlueprints the list of definitions to build from
   */
  public GuiJsonDocumentBuilder(GuiTheme guiTheme, List<GuiElementJsonBlueprint<?, ?, ?>> guiElementJsonBlueprints) {
    this.guiTheme = guiTheme;
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

  @SuppressWarnings({"unchecked", "rawtypes"})
  private GuiElement<?, ?> buildElement(JsonObject elementJson) {
    String elementTag = elementJson.getString(ATTR_TAG);

    GuiElementJsonBlueprint<?, ?, ?> guiElementJsonBlueprint = guiElementJsonBlueprints.stream()
      .filter(definition -> definition.getTag().equals(elementTag))
      .findFirst()
      .orElseThrow(() -> new MissingGuiElementJsonBlueprintException(elementTag));

    // build element with props
    GuiElement<?, ?> element = guiElementJsonBlueprint.createElementFromJson(elementJson.getObject(ATTR_PROPS, new JsonObject()));

    element.setId(elementJson.getString(ATTR_ID, null));

    // build and set element styles
    var stylesJsonArray = elementJson.getArray(ATTR_STYLES, new JsonArray());
    var themeStyles = guiTheme.getForElement(element);
    var styles = stylesJsonArray.stream()
      .map(styleObject -> (ConditionalStyle<@NonNull BaseStyles>) guiElementJsonBlueprint.createStylesFromJson(styleObject.asObject()))
      .toList();

    ((GuiElement) element).styles().setStyles(
      Stream.concat(themeStyles.stream(), styles.stream()).toList()
    );

    // build all children as well
    elementJson.getArray(ATTR_CHILDREN, new JsonArray()).stream().map(childJson -> buildElement(childJson.asObject())).forEach(element::appendChildren);

    return element;
  }
}
