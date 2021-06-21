package technology.sola.engine.platform.swing.assets;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import technology.sola.engine.assets.AbstractAssetPool;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.graphics.font.FontInfo;
import technology.sola.engine.platform.swing.exception.FailedFontLoadException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FontAssetPool extends AbstractAssetPool<Font> {
  private final AbstractAssetPool<SolaImage> solaImageAssetPool;

  public FontAssetPool(AbstractAssetPool<SolaImage> solaImageAssetPool) {
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
