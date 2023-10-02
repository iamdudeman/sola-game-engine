package technology.sola.engine.assets.gui.element;

import technology.sola.engine.assets.gui.styles.TextStylesJsonParser;
import technology.sola.engine.graphics.guiv2.elements.TextGuiElement;
import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.json.JsonObject;

public class TextElementJsonDefinition extends GuiElementJsonDefinition<TextStyles, TextGuiElement, TextStyles.Builder<?>> {
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

    // todo should be nullable
    textGuiElement.setText(propsJson.getString("text"));

    return textGuiElement;
  }

  @Override
  protected TextStyles.Builder<?> getBuilder() {
    return TextStyles.create();
  }
}
