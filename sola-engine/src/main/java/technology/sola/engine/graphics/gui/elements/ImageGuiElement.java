package technology.sola.engine.graphics.gui.elements;

import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.core.module.graphics.gui.SolaGui;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.properties.GuiElementGlobalProperties;
import technology.sola.engine.graphics.gui.properties.DefaultGuiElementProperties;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;

public class ImageGuiElement extends GuiElement<ImageGuiElement.Properties> {
  private int imageWidth = 1;
  private int imageHeight = 1;
  private String currentAssetId;
  private SolaImage solaImage;
  private SolaImage transformedImage;

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
      if (properties.getWidth() != null || properties.getHeight() != null) {
        int width = properties().getWidth() == null ? solaImage.getWidth() : properties.getWidth();
        int height = properties().getHeight() == null ? solaImage.getHeight() : properties.getHeight();

        transformedImage = solaImage.resize(
          width - properties.padding.getLeft() - properties.padding.getRight(),
          height - properties.padding.getTop() - properties.padding.getBottom()
        );
      } else {
        transformedImage = solaImage;
      }

      imageWidth = transformedImage.getWidth();
      imageHeight = transformedImage.getHeight();
    }
  }

  @Override
  public void renderSelf(Renderer renderer, int x, int y) {
    if (transformedImage != null) {
      renderer.setBlendMode(BlendMode.MASK);
      renderer.drawImage(transformedImage, x, y);
      renderer.setBlendMode(BlendMode.NO_BLENDING);
    }
  }

  public static class Properties extends DefaultGuiElementProperties {
    private String assetId;

    public Properties(GuiElementGlobalProperties globalProperties) {
      super(globalProperties);
      setFocusable(false);
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
