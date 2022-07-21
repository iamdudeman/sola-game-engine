package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementGlobalProperties;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.control.ButtonGuiElement;

public class GuiExample extends Sola {
  @Override
  protected SolaConfiguration getConfiguration() {
    return new SolaConfiguration("Gui Example", 800, 600, 30, true);
  }

  @Override
  protected void onInit() {
    assetPoolProvider.getAssetPool(Font.class)
      .addAssetId("times_NORMAL_18", "assets/times_NORMAL_18.json")
      .addAssetId(GuiElementGlobalProperties.DEFAULT_FONT_ASSET_ID, "assets/monospaced_NORMAL_18.json");


    solaGui.globalProperties.setDefaultTextColor(Color.WHITE);
    solaGui.setGuiRoot(buildGui());
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGui.render(renderer);
  }

  private GuiElement<?> buildGui() {
    ButtonGuiElement checkButton = solaGui.createElement(
      ButtonGuiElement::new,
      ButtonGuiElement.Properties::new,
      p -> p.margin.setRight(15).padding.set(15)
    );
    checkButton.setOnClick(event -> checkButton.properties().setColorBackground(checkButton.properties().getColorBackground().equals(Color.RED) ? new Color(128, 128, 128) : Color.RED));

    ButtonGuiElement toggleOtherButton = solaGui.createElement(
      ButtonGuiElement::new,
      ButtonGuiElement.Properties::new,
      p -> p.setText("Toggle other button").padding.set(5)
    );
    toggleOtherButton.setOnClick((event) -> checkButton.properties().setHidden(!checkButton.properties().isHidden()));

    ButtonGuiElement toggleFontButton = solaGui.createElement(
      ButtonGuiElement::new,
      ButtonGuiElement.Properties::new,
      p -> p.setText("Change font").padding.set(5)
    );
    toggleFontButton.setOnClick(event -> solaGui.globalProperties.setDefaultFontAssetId("times_NORMAL_18"));


    StreamGuiElementContainer firstContainer = solaGui.createElement(
      StreamGuiElementContainer::new,
      StreamGuiElementContainer.Properties::new,
      p -> p.setPreferredDimensions(400, 60).setBorderColor(Color.YELLOW).padding.set(5)
    );

    firstContainer.addChild(
      solaGui.createElement(ButtonGuiElement::new, ButtonGuiElement.Properties::new, p -> p.setText("First")),
      solaGui.createElement(ButtonGuiElement::new, ButtonGuiElement.Properties::new, p -> p.setText("Second").padding.set(5).margin.set(0, 15)),
      solaGui.createElement(ButtonGuiElement::new, ButtonGuiElement.Properties::new, p -> p.setText("Third").margin.setTop(3))
    );


    StreamGuiElementContainer secondContainer = solaGui.createElement(
      StreamGuiElementContainer::new,
      StreamGuiElementContainer.Properties::new,
      p -> p.setPreferredDimensions(400, 80).setBorderColor(Color.ORANGE).padding.set(5).margin.set(8, 0)
    );

    secondContainer.addChild(
      checkButton,
      toggleOtherButton
    );


    StreamGuiElementContainer rootElement = solaGui.createElement(
      StreamGuiElementContainer::new,
      StreamGuiElementContainer.Properties::new,
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setPreferredDimensions(400, 280).setBorderColor(Color.GREEN).padding.set(10).setPosition(15, 15)
    );

    rootElement.addChild(
      solaGui.createElement(
        TextGuiElement::new,
        TextGuiElement.Properties::new,
        p -> p.setText("Gui Example").margin.setBottom(10)
      ),
      firstContainer,
      secondContainer,
      toggleFontButton
    );

    return rootElement;
  }
}
