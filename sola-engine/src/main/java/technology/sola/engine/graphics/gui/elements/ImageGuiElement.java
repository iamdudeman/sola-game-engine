package technology.sola.engine.graphics.gui.elements;

import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementDimensions;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;

/**
 * ImageGuiElement is a {@link GuiElement} that renders a {@link SolaImage} in a GUI. It does not render child elements.
 */
public class ImageGuiElement extends GuiElement<BaseStyles> {
  // props
  private String assetId;

  // internals
  private SolaImage solaImage;
  private String currentAssetId;

  @Override
  public void renderContent(Renderer renderer) {
    if (solaImage != null) {
      SolaImage transformedImage = solaImage.resize(contentBounds.width(), contentBounds.height());

      var currentBlendFunction = renderer.getBlendFunction();
      renderer.setBlendFunction(BlendMode.MASK);
      renderer.drawImage(transformedImage, contentBounds.x(), contentBounds.y());
      renderer.setBlendFunction(currentBlendFunction);
    }
  }

  @Override
  public GuiElementDimensions calculateContentDimensions() {
    if (assetId != null && !assetId.equals(currentAssetId)) {
      getAssetLoaderProvider().get(SolaImage.class)
        .get(assetId)
        .executeWhenLoaded(solaImage -> {
          this.solaImage = solaImage;
          this.currentAssetId = assetId;
          invalidateLayout();
        });
    }

    if (solaImage != null) {
      return new GuiElementDimensions(solaImage.getWidth(), solaImage.getHeight());
    }

    return new GuiElementDimensions(0, 0);
  }

  /**
   * ImageGuiElement is not focusable so this will return false.
   *
   * @return false
   */
  @Override
  public boolean isFocusable() {
    return false;
  }

  /**
   * ImageGuiElement does not render children so this method will do nothing.
   *
   * @param children the child elements to add
   * @return this
   */
  @Override
  public GuiElement<BaseStyles> appendChildren(GuiElement<?>... children) {
    return this;
  }

  /**
   * @return the id of the {@link SolaImage} asset to be rendered
   */
  public String getAssetId() {
    return assetId;
  }

  /**
   * Sets the id of the {@link SolaImage} asset to be rendered.
   *
   * @param assetId the new asset id of the image
   */
  public void setAssetId(String assetId) {
    this.assetId = assetId;
    invalidateLayout();
  }
}
