package technology.sola.engine.assets.gui.element;

import technology.sola.engine.assets.gui.GuiAsJsonTempTesting;
import technology.sola.engine.assets.gui.styles.BaseStylesJsonParser;
import technology.sola.engine.graphics.guiv2.elements.SectionElement;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonObject;

public class SectionElementJsonDefinition extends GuiElementJsonDefinition<BaseStyles, SectionElement, BaseStyles.Builder<?>> {
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
