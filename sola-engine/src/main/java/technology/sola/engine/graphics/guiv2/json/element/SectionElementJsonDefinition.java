package technology.sola.engine.graphics.guiv2.json.element;

import technology.sola.engine.graphics.guiv2.json.styles.BaseStylesJsonParser;
import technology.sola.engine.graphics.guiv2.elements.SectionElement;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonObject;

public class SectionElementJsonDefinition extends GuiElementJsonDefinition<BaseStyles, SectionElement, BaseStyles.Builder<?>> {
  public SectionElementJsonDefinition() {
    super(new BaseStylesJsonParser());
  }

  @Override
  public String getTag() {
    return "Section";
  }

  @Override
  protected SectionElement createElement(JsonObject propsJson) {
    return new SectionElement();
  }

  @Override
  protected BaseStyles.Builder<?> createStylesBuilder() {
    return BaseStyles.create();
  }
}
