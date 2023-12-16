package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.elements.SectionGuiElement;
import technology.sola.engine.graphics.guiv2.elements.TextGuiElement;
import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.screen.AspectMode;

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
      .addAssetMapping("test", "assets/test_tiles.png");
    assetLoaderProvider.get(Font.class)
      .addAssetMapping("times_NORMAL_18", "assets/times_NORMAL_18.json");

    platform.getViewport().setAspectMode(AspectMode.MAINTAIN);

//    guiDocument.getRoot()
//      .appendChildren(
//        new SectionGuiElement(BaseStyles.create().setBackgroundColor(Color.WHITE).setBorderColor(Color.DARK_GRAY).setPadding("10").setGap(10).build())
//          .appendChildren(
//            new SectionGuiElement(BaseStyles.create().setBackgroundColor(Color.BLUE).setBorderColor(Color.RED).setHeight("50%").setGap(5).setPaddingHorizontal("5").build())
//              .appendChildren(
//                new TextGuiElement(TextStyles.create().setBorderColor(Color.GREEN).setPadding("5").setTextColor(Color.YELLOW).build()).setText("Hello world"),
//                new SectionGuiElement(BaseStyles.create().setHeight("30px").setBackgroundColor(Color.YELLOW).setBorderColor(Color.DARK_GRAY).build())
//              ),
//            new SectionGuiElement(BaseStyles.create().setBackgroundColor(Color.GREEN).setPadding("5").setHeight("60").build())
//              .appendChildren(
//                new SectionGuiElement(BaseStyles.create().setHeight("50%").setBackgroundColor(Color.RED).setBorderColor(Color.BLACK).build())
//              )
//          )
//      );
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    assetLoaderProvider.get(GuiJsonDocument.class).getNewAsset("test-gui", "assets/gui/test_gui2.json")
      .executeWhenLoaded(guiJsonDocument -> {

        guiDocument.setRootElement(guiJsonDocument.rootElement());

        completeAsyncInit.run();
      });
  }
}
