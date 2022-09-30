package technology.sola.engine.graphics.gui.elements;

import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.core.module.graphics.gui.SolaGui;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.properties.GuiElementGlobalProperties;
import technology.sola.engine.graphics.gui.properties.GuiElementProperties;
import technology.sola.engine.graphics.renderer.Renderer;

public class ImageGuiElement extends GuiElement<ImageGuiElement.Properties> {
  private int imageWidth;
  private int imageHeight;
  private String currentAssetId;
  private SolaImage solaImage;

  public ImageGuiElement(SolaGui solaGui, Properties properties) {
    super(solaGui, properties);
  }

  @Override
  public int getContentWidth() {
    return imageWidth;
  }

  @Override
  public int getContentHeight() {
    return imageHeight;
  }

  @Override
  public void recalculateLayout() {
    if (properties.assetId != null && !properties.assetId.equals(currentAssetId)) {
      this.currentAssetId = properties().assetId;
      solaGui.getAssetLoaderProvider()
        .get(SolaImage.class)
        .get(currentAssetId)
        .executeWhenLoaded(solaImage -> {
          this.solaImage = solaImage;
          properties().setLayoutChanged(true);
        });
    }

    if (solaImage != null) {
      imageWidth = solaImage.getWidth();
      imageHeight = solaImage.getHeight();
    }
  }

  @Override
  public void renderSelf(Renderer renderer, int x, int y) {
    if (solaImage != null) {
      renderer.drawImage(x, y, solaImage);
    }
  }

  public static class Properties extends GuiElementProperties {
    private String assetId;

    public Properties(GuiElementGlobalProperties globalProperties) {
      super(globalProperties);
    }

    public String getAssetId() {
      return assetId;
    }

    /**
     * Asset id for the {@link SolaImage} to be rendered. Asset id mapping must already be added to the
     * {@link technology.sola.engine.assets.AssetLoader<SolaImage>}.
     *
     * @param assetId the asset id for the {@code SolaImage}
     * @return this {@link Properties} instance
     */
    public Properties setAssetId(String assetId) {
      this.assetId = assetId;
      setLayoutChanged(true);

      return this;
    }
  }
}
