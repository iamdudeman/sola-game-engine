package technology.sola.engine.graphics.guiv2.elements;

import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.GuiElementDimensions;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;

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
          this.solaImage = solaImage;
          this.currentAssetId = assetId;
          getParent().invalidateLayout();
        });
    }

    if (solaImage != null) {
      return new GuiElementDimensions(solaImage.getWidth(), solaImage.getHeight());
    }

    return new GuiElementDimensions(0, 0);
  }

  @Override
  public boolean isFocusable() {
    return false;
  }

  public String getAssetId() {
    return assetId;
  }

  public void setAssetId(String assetId) {
    this.assetId = assetId;
    invalidateLayout();
  }
}
