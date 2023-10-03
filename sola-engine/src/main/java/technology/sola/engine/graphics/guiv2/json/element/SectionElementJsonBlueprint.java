package technology.sola.engine.graphics.guiv2.json.element;

import technology.sola.engine.graphics.guiv2.json.styles.BaseStylesJsonBlueprint;
import technology.sola.engine.graphics.guiv2.elements.SectionGuiElement;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonObject;

/**
 * A {@link GuiElementJsonBlueprint} for {@link SectionGuiElement}.
 */
public class SectionElementJsonBlueprint extends GuiElementJsonBlueprint<BaseStyles, SectionGuiElement, BaseStyles.Builder<?>> {
  /**
   * Creates an instance of this {@link GuiElementJsonBlueprint}.
   */
  public SectionElementJsonBlueprint() {
    super(new BaseStylesJsonBlueprint());
  }

  @Override
  public String getTag() {
    return "Section";
  }

  @Override
  public SectionGuiElement createElementFromJson(JsonObject propsJson) {
    return new SectionGuiElement();
  }

  @Override
  protected BaseStyles.Builder<?> createStylesBuilder() {
    return BaseStyles.create();
  }
}
