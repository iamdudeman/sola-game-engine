package technology.sola.engine.platform.javafx.assets;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.graphics.font.FontInfo;
import technology.sola.engine.graphics.font.mapper.FontInfoJsonMapper;
import technology.sola.engine.platform.javafx.assets.exception.FailedFontLoadException;
import technology.sola.json.SolaJson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FontAssetPool extends AssetPool<Font> {
  private final AssetPool<SolaImage> solaImageAssetPool;

  public FontAssetPool(AssetPool<SolaImage> solaImageAssetPool) {
    this.solaImageAssetPool = solaImageAssetPool;
  }

  @Override
  public Class<Font> getAssetClass() {
    return Font.class;
  }

  @Override
  protected Font loadAsset(String path) {
    File file = new File(path);
    SolaJson solaJson = new SolaJson();

    try {
      FontInfo fontInfo = solaJson.parse(Files.readString(file.toPath()), new FontInfoJsonMapper());
      SolaImage fontImage = solaImageAssetPool.addAndGetAsset(
        fontInfo.fontGlyphFile(),
        path.replace(file.getName(), "") + fontInfo.fontGlyphFile()
      );

      return new Font(fontImage, fontInfo);
    } catch (IOException ex) {
      throw new FailedFontLoadException(path);
    }
  }
}
