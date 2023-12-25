package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.assets.BulkAssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.Key;

public class GuiV2Example extends SolaWithDefaults {
  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public GuiV2Example() {
    super(SolaConfiguration.build("Gui V2 Example", 800, 700).withTargetUpdatesPerSecond(30));
  }


  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGuiV2();

    assetLoaderProvider.get(SolaImage.class)
      .addAssetMapping("test_tiles", "assets/test_tiles.png");
    assetLoaderProvider.get(Font.class)
      .addAssetMapping("times_NORMAL_18", "assets/times_NORMAL_18.json");

    platform.getViewport().setAspectMode(AspectMode.MAINTAIN);

    platform.onKeyPressed(keyEvent -> {
      if (keyEvent.keyCode() == Key.ONE.getCode()) {
        guiDocument.setRootElement(assetLoaderProvider.get(GuiJsonDocument.class).get("gui").getAsset().rootElement());
      }
      if (keyEvent.keyCode() == Key.TWO.getCode()) {
        guiDocument.setRootElement(assetLoaderProvider.get(GuiJsonDocument.class).get("row").getAsset().rootElement());
      }
      if (keyEvent.keyCode() == Key.THREE.getCode()) {
        guiDocument.setRootElement(assetLoaderProvider.get(GuiJsonDocument.class).get("column").getAsset().rootElement());
      }
      if (keyEvent.keyCode() == Key.FOUR.getCode()) {
        guiDocument.setRootElement(assetLoaderProvider.get(GuiJsonDocument.class).get("text").getAsset().rootElement());
      }
      if (keyEvent.keyCode() == Key.FIVE.getCode()) {
        guiDocument.setRootElement(assetLoaderProvider.get(GuiJsonDocument.class).get("image").getAsset().rootElement());
      }
      if (keyEvent.keyCode() == Key.SIX.getCode()) {
        guiDocument.setRootElement(assetLoaderProvider.get(GuiJsonDocument.class).get("button").getAsset().rootElement());
      }
    });
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    new BulkAssetLoader(assetLoaderProvider)
      .addAsset(GuiJsonDocument.class, "gui", "assets/gui/test_gui.json")
      .addAsset(GuiJsonDocument.class, "row", "assets/gui/test_row.json")
      .addAsset(GuiJsonDocument.class, "column", "assets/gui/test_column.json")
      .addAsset(GuiJsonDocument.class, "text", "assets/gui/test_text.json")
      .addAsset(GuiJsonDocument.class, "image", "assets/gui/test_image.json")
      .addAsset(GuiJsonDocument.class, "button", "assets/gui/test_button.json")
      .loadAll()
      .onComplete(assets -> {
        if (assets[0] instanceof GuiJsonDocument guiJsonDocument) {
          guiDocument.setRootElement(guiJsonDocument.rootElement());
        }

        completeAsyncInit.run();
      });
  }
}
