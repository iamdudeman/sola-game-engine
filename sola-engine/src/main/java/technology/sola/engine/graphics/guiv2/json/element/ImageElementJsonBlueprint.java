package technology.sola.engine.graphics.guiv2.json.element;

import technology.sola.engine.graphics.guiv2.elements.ImageGuiElement;
import technology.sola.engine.graphics.guiv2.json.styles.BaseStylesJsonValueParser;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.json.JsonObject;

/**
 * A {@link GuiElementJsonBlueprint} for {@link ImageGuiElement}.
 */
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
    ImageGuiElement imageGuiElement = new ImageGuiElement();

    imageGuiElement.setAssetId(propsJson.getString("assetId", null));

    return imageGuiElement;
  }

  @Override
  protected BaseStyles.Builder<?> createStylesBuilder() {
    return BaseStyles.create();
  }
}
