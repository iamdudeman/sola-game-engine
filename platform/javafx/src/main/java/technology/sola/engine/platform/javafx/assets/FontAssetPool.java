package technology.sola.engine.platform.javafx.assets;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.graphics.font.FontInfo;
import technology.sola.engine.platform.javafx.assets.exception.FailedFontLoadException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
    Gson gson = new Gson();
    File file = new File(path);

    try (JsonReader reader = new JsonReader(new FileReader(file))) {
      FontInfo fontInfo = gson.fromJson(reader, FontInfo.class);
      SolaImage fontImage = solaImageAssetPool.addAndGetAsset(
        fontInfo.getFontGlyphFile(),
        path.replace(file.getName(), "") + fontInfo.getFontGlyphFile()
      );

      return new Font(fontImage, fontInfo);
    } catch (IOException ex) {
      throw new FailedFontLoadException(path);
    }
  }
}
