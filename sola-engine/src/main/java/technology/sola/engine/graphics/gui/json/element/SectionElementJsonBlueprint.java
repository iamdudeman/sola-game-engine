package technology.sola.engine.graphics.gui.json.element;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.graphics.gui.json.styles.BaseStylesJsonValueParser;
import technology.sola.engine.graphics.gui.elements.SectionGuiElement;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.json.JsonObject;

/**
 * A {@link GuiElementJsonBlueprint} for {@link SectionGuiElement}.
 */
@NullMarked
public class SectionElementJsonBlueprint extends GuiElementJsonBlueprint<BaseStyles, SectionGuiElement, BaseStyles.Builder<?>> {
  /**
   * Creates an instance of this {@link GuiElementJsonBlueprint}.
   */
  public SectionElementJsonBlueprint() {
    super(new BaseStylesJsonValueParser());
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
    return new BaseStyles.Builder<>();
  }
}
