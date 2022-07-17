package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.HorizontalContainerGuiElement;
import technology.sola.engine.graphics.gui.elements.container.VerticalContainerGuiElement;
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
      .addAssetId("monospaced_NORMAL_18", "assets/monospaced_NORMAL_18.json");


    solaGui.setGuiRoot(buildGui());
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGui.render(renderer);
  }

  private GuiElement<?> buildGui() {
    TextGuiElement textGuiElement = new TextGuiElement(assetPoolProvider, "Gui Example");
    textGuiElement.properties()
      .margin.setBottom(10);

    HorizontalContainerGuiElement firstParent = new HorizontalContainerGuiElement(400, 60);
    firstParent.properties()
      .setBorderColor(Color.YELLOW)
      .padding.set(5);

    ButtonGuiElement middleButton = new ButtonGuiElement(assetPoolProvider, "Second");
    middleButton.properties()
      .padding.set(5)
      .margin.set(0, 15);

    firstParent.addChild(new ButtonGuiElement(assetPoolProvider, "First"));
    firstParent.addChild(middleButton);
    firstParent.addChild(new ButtonGuiElement(assetPoolProvider, "Third"));


    HorizontalContainerGuiElement secondParent = new HorizontalContainerGuiElement(400, 80);
    secondParent.properties()
      .setBorderColor(Color.ORANGE)
      .padding.set(5)
      .margin.set(8, 0);

    ButtonGuiElement checkButton = new ButtonGuiElement(assetPoolProvider, "");
    checkButton.properties()
      .margin.setRight(15)
      .padding.set(15);
    checkButton.setOnClick(event -> checkButton.properties().setColorBackground(checkButton.properties().getColorBackground().equals(Color.RED) ? new Color(128, 128, 128) : Color.RED));
    ButtonGuiElement toggleOtherButton = new ButtonGuiElement(assetPoolProvider, "Toggle other button");
    toggleOtherButton.properties()
      .padding.set(5);
    toggleOtherButton.setOnClick((event) -> checkButton.properties().setHidden(!checkButton.properties().isHidden()));

    secondParent.addChild(checkButton);
    secondParent.addChild(toggleOtherButton);


    ButtonGuiElement toggleFontButton = new ButtonGuiElement(assetPoolProvider, "Toggle font on this button");
    toggleFontButton.properties()
      .padding.set(5);
    toggleFontButton.setOnClick(event -> toggleFontButton.properties().setFontName(toggleFontButton.properties().getFontName().equals("times_NORMAL_18") ? "monospaced_NORMAL_18" : "times_NORMAL_18"));


    VerticalContainerGuiElement rootElement = new VerticalContainerGuiElement(400, 280);
    rootElement.properties()
      .setBorderColor(Color.GREEN)
      .padding.set(10)
      .setPosition(15, 15);

    rootElement.addChild(textGuiElement);
    rootElement.addChild(firstParent);
    rootElement.addChild(secondParent);
    rootElement.addChild(toggleFontButton);

    return rootElement;
  }
}
