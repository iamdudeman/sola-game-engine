package technology.sola.engine.examples.javafx;

import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.platform.javafx.JavaFxApplication;
import technology.sola.engine.platform.javafx.JavaFxContainer;
import technology.sola.engine.platform.javafx.SolaImageAssetMapper;

public class Main  {
  public static void main(String[] args) {
    JavaFxContainer javaFxContainer = new JavaFxContainer("JavaFX Test" ,800, 600, renderer -> {
      renderer.clear();
      renderer.setPixel(5, 5, Color.WHITE);
      renderer.setPixel(6, 5, Color.BLUE);
      renderer.setPixel(6, 6, Color.RED);
      renderer.setPixel(6, 7, Color.GREEN);

      renderer.drawLine(20, 50, 20, 100, Color.WHITE);
      renderer.drawLine(50, 20, 100, 20, Color.WHITE);

      renderer.fillRect(100, 100, 60, 80, Color.GREEN);
      renderer.drawRect(100, 100, 60, 80, Color.RED);

      renderer.drawRect(300, 150, 5, 5, Color.GREEN);
      renderer.fillCircle(300, 150, 100.5f, Color.BLUE);
      renderer.drawCircle(300, 150, 100.5f, Color.RED);

      AssetLoader assetLoader = new AssetLoader();
      assetLoader.addAssetMapper(new SolaImageAssetMapper());
      assetLoader.addAsset("test_tiles", "test_tiles.png");
      SolaImage solaImage = assetLoader.getAsset("test_tiles", SolaImage.class);
      renderer.drawImage(400, 400, solaImage);
      renderer.drawImage(400, 530, solaImage.getSubImage(1, 1, 16, 16));
    });

    JavaFxApplication.start(javaFxContainer, args);
  }
}
