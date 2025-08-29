package technology.sola.engine.graphics.gui.elements;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementDimensions;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.List;

/**
 * ImageGuiElement is a {@link GuiElement} that renders a {@link SolaImage} in a GUI. It does not render child elements.
 */
@NullMarked
public class ImageGuiElement extends GuiElement<BaseStyles, ImageGuiElement> {
  // props
  @Nullable
  private String assetId;

  // internals
  @Nullable
  private SolaImage solaImage;
  @Nullable
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

  @Override
  public ImageGuiElement self() {
    return this;
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
   * @param children the child elements that will not be added
   * @return this
   */
  @Override
  public ImageGuiElement appendChildren(GuiElement<?, ?>... children) {
    return this;
  }

  /**
   * ImageGuiElement does not render children so this method will do nothing.
   *
   * @param child the child element that will not be removed
   * @return this
   */
  @Override
  public ImageGuiElement removeChild(GuiElement<?, ?> child) {
    return this;
  }

  /**
   * ImageGuiElement does not render children so this method will return an empty List.
   *
   * @return empty List
   */
  @Override
  public List<GuiElement<?, ?>> getChildren() {
    return List.of();
  }

  /**
   * @return the id of the {@link SolaImage} asset to be rendered
   */
  @Nullable
  public String getAssetId() {
    return assetId;
  }

  /**
   * Sets the id of the {@link SolaImage} asset to be rendered.
   *
   * @param assetId the new asset id of the image
   * @return this
   */
  public ImageGuiElement setAssetId(String assetId) {
    this.assetId = assetId;
    invalidateLayout();

    return this;
  }
}
