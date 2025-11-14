package technology.sola.engine.graphics.gui.json.element;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.graphics.gui.elements.SpriteGuiElement;
import technology.sola.engine.graphics.gui.json.styles.BaseStylesJsonValueParser;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.json.JsonObject;

/**
 * A {@link GuiElementJsonBlueprint} for {@link SpriteGuiElement}.
 */
@NullMarked
public class SpriteElementJsonBlueprint extends GuiElementJsonBlueprint<BaseStyles, SpriteGuiElement, BaseStyles.Builder<?>> {
  /**
   * Creates an instance of this {@link GuiElementJsonBlueprint}.
   */
  public SpriteElementJsonBlueprint() {
    super(new BaseStylesJsonValueParser());
  }

  @Override
  public String getTag() {
    return "Sprite";
  }

  @Override
  public SpriteGuiElement createElementFromJson(JsonObject propsJson) {
    return new SpriteGuiElement()
      .setAssetId(propsJson.getString("assetId", null))
      .setSpriteId(propsJson.getString("spriteId", null));
  }

  @Override
  protected BaseStyles.Builder<?> createStylesBuilder() {
    return new BaseStyles.Builder<>();
  }
}
