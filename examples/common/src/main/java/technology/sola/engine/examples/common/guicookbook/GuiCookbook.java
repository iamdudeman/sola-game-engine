package technology.sola.engine.examples.common.guicookbook;

import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.examples.common.guicookbook.general.GeneralCategoryPage;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;

/* todo common properties
    general
     text
     image
    container
     stream container
    form
     button
     text input
*/

public class GuiCookbook extends SolaWithDefaults {
  public GuiCookbook() {
    super(SolaConfiguration.build("Gui Cookbook", 800, 700).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGui();

    solaGuiDocument.globalProperties.setDefaultTextColor(Color.WHITE);
    solaGuiDocument.setGuiRoot(buildGui(), 15, 15);

    assetLoaderProvider.get(SolaImage.class)
      .addAssetMapping("test_tiles", "assets/test_tiles.png");
    assetLoaderProvider.get(Font.class)
      .addAssetMapping("times_NORMAL_18", "assets/times_NORMAL_18.json");
  }

  private GuiElement<?> buildGui() {
    return solaGuiDocument.createElement(
      StreamGuiElementContainer::new,
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setGap(15).padding.set(5).setId("root"),
      solaGuiDocument.createElement(
        TextGuiElement::new,
        p -> p.setText("Gui Cookbook").margin.setBottom(10)
      ),
      solaGuiDocument.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(5).setId("nav"),
        makeNavButton(new GeneralCategoryPage(solaGuiDocument))
      )
    );
  }

  private GuiElement<?> makeNavButton(CategoryPage categoryPage) {
    return solaGuiDocument.createElement(
      ButtonGuiElement::new,
      p -> p.setText(categoryPage.getTitle())
    ).setOnAction(() -> {
      var rootEle = solaGuiDocument.getElementById("root", StreamGuiElementContainer.class);

      rootEle.removeChild(solaGuiDocument.getElementById("category"));
      rootEle.addChild(categoryPage.build());
    });
  }
}
