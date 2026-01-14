package technology.sola.engine.assets;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.assets.exception.MissingAssetLoaderException;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheet;
import technology.sola.engine.assets.list.AssetList;
import technology.sola.engine.assets.scene.Scene;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The AssetLoaderProvider class holds instances of {@link AssetLoader}s for various {@link Asset} types.
 */
@NullMarked
public class AssetLoaderProvider {
  private final Map<Class<? extends Asset>, AssetLoader<?>> assetLoaderMap = new HashMap<>();

  /**
   * Adds an {@link AssetLoader} that can be retrieved and used later.
   *
   * @param assetLoader the {@code AssetLoader} to add
   */
  public void add(AssetLoader<?> assetLoader) {
    assetLoaderMap.put(assetLoader.getAssetClass(), assetLoader);
  }

  /**
   * Retrieves an {@link AssetLoader} for the desired {@link Asset} type via the asset's class.
   *
   * @param assetClass the class of asset to load
   * @param <T>        the type of Asset
   * @return the {@code AssetLoader} instance for the desired asset class
   * @throws MissingAssetLoaderException if there is no asset loader added for the desired asset
   */
  @SuppressWarnings("unchecked")
  public <T extends Asset> AssetLoader<T> get(Class<T> assetClass) {
    AssetLoader<?> assetLoader = assetLoaderMap.get(assetClass);

    if (assetLoader == null) {
      throw new MissingAssetLoaderException(assetClass);
    }

    return (AssetLoader<T>) assetLoader;
  }

  public void loadAssetsFromAssetList(Runnable onComplete) {
    this.get(AssetList.class).getNewAsset(AssetList.ID, AssetList.PATH)
      .executeWhenLoaded(assetList -> {
        BulkAssetLoader bulkAssetLoader = new BulkAssetLoader(this);

        addAllAssetMapping(AudioClip.class, assetList.audioAssets(), bulkAssetLoader);
        addAllAssetMapping(Font.class, assetList.fontAssets(), bulkAssetLoader);
        addAllAssetMapping(GuiJsonDocument.class, assetList.guiAssets(), bulkAssetLoader);
        addAllAssetMapping(SpriteSheet.class, assetList.spriteSheetAssets(), bulkAssetLoader);
        addAllAssetMapping(Scene.class, assetList.sceneAssets(), bulkAssetLoader);

        bulkAssetLoader.loadAll()
          .onComplete(assets -> onComplete.run());
      });
  }

  private <T extends Asset> void addAllAssetMapping(
    Class<T> assetClass,
    List<AssetList.AssetDetails<T>> assetDetails,
    BulkAssetLoader bulkAssetLoader
  ) {
    for (var assetDetail : assetDetails) {
      if (assetDetail.isBlocking()) {
        bulkAssetLoader.addAsset(assetClass, assetDetail.id(), assetDetail.path());
      } else {
        this.get(assetClass).addAssetMapping(assetDetail.id(), assetDetail.path());
      }
    }
  }
}
