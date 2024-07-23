package technology.sola.engine.graphics.gui.json.element;

import technology.sola.engine.graphics.gui.json.styles.StylesJsonValueParser;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.json.JsonObject;

/**
 * GuiElementJsonBlueprint provides functionality for building a {@link GuiElement} with its styles from JSON. Each
 * blueprint should have a unique tag ({@link GuiElementJsonBlueprint#getTag()}).
 *
 * @param <Styles>        the styles type
 * @param <Element>       the gui element type
 * @param <StylesBuilder> the styles builder type
 */
public abstract class GuiElementJsonBlueprint<Styles extends BaseStyles, Element extends GuiElement<Styles, ?>, StylesBuilder extends BaseStyles.Builder<?>> {
  private final StylesJsonValueParser<StylesBuilder> stylesJsonValueParser;

  /**
   * Creates an instance of this blueprint.
   *
   * @param stylesJsonValueParser the {@link StylesJsonValueParser} used to parse styles values
   */
  public GuiElementJsonBlueprint(StylesJsonValueParser<StylesBuilder> stylesJsonValueParser) {
    this.stylesJsonValueParser = stylesJsonValueParser;
  }

  /**
   * @return the tag identifier of this blueprint
   */
  public abstract String getTag();

  /**
   * Creates an instance of the Element's Styles.
   *
   * @param stylesJson the {@link JsonObject} to build the Styles from
   * @return the Styles instance with properties set
   */
  @SuppressWarnings("unchecked")
  public ConditionalStyle<Styles> createStylesFromJson(JsonObject stylesJson) {
    StylesBuilder stylesBuilder = createStylesBuilder();

    String condition = stylesJson.getString("@condition", "");

    stylesJson.forEach((key, value) -> stylesJsonValueParser.setPropertyFromJson(stylesBuilder, key, value));

    var style = (Styles) stylesBuilder.build();

    return switch (condition) {
      case ":active" -> ConditionalStyle.active(style);
      case ":disabled" -> ConditionalStyle.disabled(style);
      case ":focus" -> ConditionalStyle.focus(style);
      case ":hover" -> ConditionalStyle.hover(style);
      default -> ConditionalStyle.always(style);
    };
  }

  /**
   * Creates an instance of the Element of this blueprint.
   *
   * @param propsJson the {@link JsonObject} to build from
   * @return the Element with props set
   */
  public abstract Element createElementFromJson(JsonObject propsJson);

  /**
   * @return a new StylesBuilder for this blueprint to use
   */
  protected abstract StylesBuilder createStylesBuilder();
}
