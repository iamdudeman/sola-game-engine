package technology.sola.engine.examples.common.guicookbook;

import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.examples.common.guicookbook.container.StreamContainerDemo;
import technology.sola.engine.examples.common.guicookbook.general.CommonPropertiesDemo;
import technology.sola.engine.examples.common.guicookbook.general.ImageElementDemo;
import technology.sola.engine.examples.common.guicookbook.general.TextElementDemo;
import technology.sola.engine.examples.common.guicookbook.input.ButtonElementDemo;
import technology.sola.engine.examples.common.guicookbook.input.TextInputElementDemo;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;

/**
 * GuiCookbook is a {@link technology.sola.engine.core.Sola} that demos the various {@link GuiElement}s that have been
 * implemented.
 */
public class GuiCookbook extends SolaWithDefaults {
  /**
   * Creates in instances of this {@link technology.sola.engine.core.Sola}.
   */
  public GuiCookbook() {
    super(SolaConfiguration.build("Gui Cookbook", 1200, 800).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGui();

    var guiRoot = buildGui();
    solaGuiDocument.setGuiRoot(guiRoot);
    guiRoot.requestFocus();

    assetLoaderProvider.get(SolaImage.class)
      .addAssetMapping("duck", "assets/duck.png")
      .addAssetMapping("test_tiles", "assets/test_tiles.png");
    assetLoaderProvider.get(Font.class)
      .addAssetMapping("arial_NORMAL_16", "assets/arial_NORMAL_16.json")
      .addAssetMapping("times_NORMAL_18", "assets/times_NORMAL_18.json");
  }

  private GuiElement<?> buildGui() {
    return solaGuiDocument.createElement(
      StreamGuiElementContainer::new,
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setGap(10).padding.set(10).setWidth(platform.getRenderer().getWidth()).setHeight(platform.getRenderer().getHeight()).setId("root"),
      solaGuiDocument.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(5),
        solaGuiDocument.createElement(
          TextGuiElement::new,
          p -> p.setText("Gui Cookbook").margin.setLeft(10).margin.setRight(10)
        ),
        makeNavButton(new ElementGroup(solaGuiDocument, "General", new CommonPropertiesDemo(solaGuiDocument), new TextElementDemo(solaGuiDocument), new ImageElementDemo(solaGuiDocument))),
        makeNavButton(new ElementGroup(solaGuiDocument, "Container", new StreamContainerDemo(solaGuiDocument))),
        makeNavButton(new ElementGroup(solaGuiDocument, "Input", new ButtonElementDemo(solaGuiDocument), new TextInputElementDemo(solaGuiDocument)))
      )
    );
  }

  private GuiElement<?> makeNavButton(ElementGroup elementGroup) {
    return solaGuiDocument.createElement(
      ButtonGuiElement::new,
      p -> p.setText(elementGroup.getTitle())
    ).setOnAction(() -> {
      var rootEle = solaGuiDocument.getElementById("root", StreamGuiElementContainer.class);

      rootEle.removeChild(solaGuiDocument.getElementById("category"));
      rootEle.addChild(elementGroup.build());
    });
  }
}
