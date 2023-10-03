package technology.sola.engine.graphics.guiv2.json.element;

import technology.sola.engine.graphics.guiv2.json.styles.StylesJsonDefinition;
import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonObject;

public abstract class GuiElementJsonDefinition<Styles extends BaseStyles, Element extends GuiElement<Styles>, StylesBuilder extends BaseStyles.Builder<?>> {
  protected StylesJsonDefinition<StylesBuilder> stylesJsonDefinition;

  public GuiElementJsonDefinition(StylesJsonDefinition<StylesBuilder> stylesJsonDefinition) {
    this.stylesJsonDefinition = stylesJsonDefinition;
  }

  public abstract String getTag();

  public Styles createStyles(JsonObject stylesJson) {
    return (Styles) stylesJsonDefinition.populateStylesBuilder(stylesJson, createStylesBuilder()).build();
  }

  public abstract Element createElement(JsonObject propsJson);

  protected abstract StylesBuilder createStylesBuilder();
}
