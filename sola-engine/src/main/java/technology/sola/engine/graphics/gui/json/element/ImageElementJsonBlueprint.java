package technology.sola.engine.graphics.gui.json.element;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.graphics.gui.elements.ImageGuiElement;
import technology.sola.engine.graphics.gui.json.styles.BaseStylesJsonValueParser;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.json.JsonObject;

/**
 * A {@link GuiElementJsonBlueprint} for {@link ImageGuiElement}.
 */
@NullMarked
public class ImageElementJsonBlueprint extends GuiElementJsonBlueprint<BaseStyles, ImageGuiElement, BaseStyles.Builder<?>> {
  /**
   * Creates an instance of this {@link GuiElementJsonBlueprint}.
   */
  public ImageElementJsonBlueprint() {
    super(new BaseStylesJsonValueParser());
  }

  @Override
  public String getTag() {
    return "Image";
  }

  @Override
  public ImageGuiElement createElementFromJson(JsonObject propsJson) {
    return new ImageGuiElement()
      .setAssetId(propsJson.getString("assetId", null));
  }

  @Override
  protected BaseStyles.Builder<?> createStylesBuilder() {
    return BaseStyles.create();
  }
}
