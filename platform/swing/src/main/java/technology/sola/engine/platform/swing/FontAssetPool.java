package technology.sola.engine.platform.swing;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import technology.sola.engine.assets.AbstractAssetPool;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.graphics.font.FontInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class FontAssetPool extends AbstractAssetPool<Font> {
  private AbstractAssetPool<SolaImage> solaImageAssetPool;

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

    try {
      File file = new File(path);
      JsonReader reader = new JsonReader(new FileReader(file));
      FontInfo fontInfo = gson.fromJson(reader, FontInfo.class);
      SolaImage fontImage = solaImageAssetPool.addAndGetAsset(fontInfo.getFile(), file.getPath().replace(file.getName(), "") + fontInfo.getFile());

      Font font = Font.createFont(fontImage, fontInfo);

      return font;
    } catch (FileNotFoundException ex) {
      throw new RuntimeException(ex);
    }
  }
}
