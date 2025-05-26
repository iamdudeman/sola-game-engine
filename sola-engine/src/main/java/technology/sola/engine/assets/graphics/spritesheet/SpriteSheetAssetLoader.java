package technology.sola.engine.assets.graphics.spritesheet;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.json.JsonElementAsset;

/**
 * SpriteSheetAssetLoader is an {@link AssetLoader} implementation for {@link SpriteSheet}s.
 */
@NullMarked
public class SpriteSheetAssetLoader extends AssetLoader<SpriteSheet> {
  private final AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader;
  private final AssetLoader<SolaImage> solaImageAssetLoader;

  /**
   * Creates an instance of this asset loader.
   *
   * @param jsonElementAssetAssetLoader the {@link JsonElementAsset} {@link AssetLoader} to use internally
   * @param solaImageAssetLoader        the {@link SolaImage} {@link AssetLoader} to use internally
   */
  public SpriteSheetAssetLoader(AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader, AssetLoader<SolaImage> solaImageAssetLoader) {
    this.jsonElementAssetAssetLoader = jsonElementAssetAssetLoader;
    this.solaImageAssetLoader = solaImageAssetLoader;
  }

  @Override
  public Class<SpriteSheet> getAssetClass() {
    return SpriteSheet.class;
  }

  @Override
  protected AssetHandle<SpriteSheet> loadAsset(String path) {
    AssetHandle<SpriteSheet> spriteSheetAssetHandle = new AssetHandle<>();

    jsonElementAssetAssetLoader.getNewAsset(path, path)
      .executeWhenLoaded(jsonElementAsset -> {
        SpriteSheetInfo spriteSheetInfo = new SpriteSheetInfoJsonMapper().toObject(jsonElementAsset.asObject());
        String spriteImageName = spriteSheetInfo.spriteSheet();

        solaImageAssetLoader.getNewAsset(spriteImageName, path.substring(0, path.lastIndexOf("/")) + "/" + spriteImageName).executeWhenLoaded(solaImage -> {
          SpriteSheet spriteSheet = new SpriteSheet(solaImage);

          spriteSheetInfo.sprites().forEach(spriteSheet::addSpriteDefinition);

          spriteSheetAssetHandle.setAsset(spriteSheet);
        });
      });

    return spriteSheetAssetHandle;
  }
}
