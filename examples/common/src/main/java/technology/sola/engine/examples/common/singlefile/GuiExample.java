package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementGlobalProperties;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.FlowContainerGuiElement;
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
    FlowContainerGuiElement firstParent = solaGui.createElement(
      FlowContainerGuiElement::new,
      FlowContainerGuiElement.Properties::new,
      p -> p.setPreferredDimensions(400, 60).setBorderColor(Color.YELLOW).padding.set(5)
    );

    firstParent.addChild(
      solaGui.createElement(ButtonGuiElement::new, ButtonGuiElement.Properties::new, p -> p.setText("First")),
      solaGui.createElement(
        ButtonGuiElement::new,
        ButtonGuiElement.Properties::new,
        p -> p.setText("Second").padding.set(5).margin.set(0, 15)
      ),
      solaGui.createElement(ButtonGuiElement::new, ButtonGuiElement.Properties::new, p -> p.setText("Third").margin.setTop(3))
    );

    FlowContainerGuiElement secondParent = solaGui.createElement(
      FlowContainerGuiElement::new,
      FlowContainerGuiElement.Properties::new,
      p -> p.setPreferredDimensions(400, 80).setBorderColor(Color.ORANGE).padding.set(5).margin.set(8, 0)
    );

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

    secondParent.addChild(checkButton, toggleOtherButton);

    ButtonGuiElement toggleFontButton = solaGui.createElement(
      ButtonGuiElement::new,
      ButtonGuiElement.Properties::new,
      p -> p.setText("Toggle font on this button").padding.set(5)
    );
    toggleFontButton.setOnClick(event -> toggleFontButton.properties().setFontAssetId(toggleFontButton.properties().getFontAssetId().equals("times_NORMAL_18") ? GuiElementGlobalProperties.DEFAULT_FONT_ASSET_ID : "times_NORMAL_18"));


    FlowContainerGuiElement rootElement = solaGui.createElement(
      FlowContainerGuiElement::new,
      FlowContainerGuiElement.Properties::new,
      p -> p.setDirection(FlowContainerGuiElement.Direction.VERTICAL).setPreferredDimensions(400, 280).setBorderColor(Color.GREEN).padding.set(10).setPosition(15, 15)
    );

    rootElement.addChild(
      solaGui.createElement(
        TextGuiElement::new,
        TextGuiElement.Properties::new,
        p -> p.setText("Gui Example").margin.setBottom(10)
      ),
      firstParent, secondParent, toggleFontButton
    );

    return rootElement;
  }
}
