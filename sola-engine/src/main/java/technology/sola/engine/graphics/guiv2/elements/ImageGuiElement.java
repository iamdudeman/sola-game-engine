package technology.sola.engine.graphics.guiv2.elements;

import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.GuiElementDimensions;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;

// TODO this isn't working yet
public class ImageGuiElement extends GuiElement<BaseStyles> {
  // props
  private String assetId;

  // internals
  private SolaImage transformedImage;
  private String currentAssetId;

  @Override
  public void renderContent(Renderer renderer) {
    if (transformedImage != null) {
      renderer.setBlendFunction(BlendMode.MASK);
      renderer.drawImage(transformedImage, contentBounds.x(), contentBounds.y());
    }
  }

  @Override
  public GuiElementDimensions calculateContentDimensions() {
    if (assetId != null && !assetId.equals(currentAssetId)) {
      getAssetLoaderProvider().get(SolaImage.class)
        .get(assetId)
        .executeWhenLoaded(solaImage -> {
          this.transformedImage = solaImage;
          this.currentAssetId = assetId;
          invalidateLayout();
        });
    }

    if (transformedImage != null) {
      transformedImage = transformedImage.resize(contentBounds.width(), contentBounds.height());

      return new GuiElementDimensions(transformedImage.getWidth(), transformedImage.getHeight());
    }

    return new GuiElementDimensions(0, 0);
  }

  public String getAssetId() {
    return assetId;
  }

  public void setAssetId(String assetId) {
    this.assetId = assetId;
    invalidateLayout();
  }
}
