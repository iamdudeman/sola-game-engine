package technology.sola.engine.graphics.guiv2.json.element;

import technology.sola.engine.graphics.guiv2.json.styles.BaseStylesJsonDefinition;
import technology.sola.engine.graphics.guiv2.elements.SectionElement;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonObject;

/**
 * A {@link GuiElementJsonDefinition} for {@link SectionElement}.
 */
public class SectionElementJsonDefinition extends GuiElementJsonDefinition<BaseStyles, SectionElement, BaseStyles.Builder<?>> {
  /**
   * Creates an instance of this {@link GuiElementJsonDefinition}.
   */
  public SectionElementJsonDefinition() {
    super(new BaseStylesJsonDefinition());
  }

  @Override
  public String getTag() {
    return "Section";
  }

  @Override
  public SectionElement createElement(JsonObject propsJson) {
    return new SectionElement();
  }

  @Override
  protected BaseStyles.Builder<?> createStylesBuilder() {
    return BaseStyles.create();
  }
}
