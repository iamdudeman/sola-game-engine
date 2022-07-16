package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.RenderMode;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.graphics.gui.element.ChildlessGuiElement;
import technology.sola.engine.graphics.gui.element.container.HorizontalContainerGuiElement;
import technology.sola.engine.graphics.gui.element.GuiElement;
import technology.sola.engine.graphics.gui.element.GuiElementBaseProperties;
import technology.sola.engine.graphics.gui.element.container.VerticalContainerGuiElement;

public class GuiExample extends Sola {
  @Override
  protected SolaConfiguration getConfiguration() {
    return new SolaConfiguration("Gui Example", 800, 600, 30, true);
  }

  @Override
  protected void onInit() {
    assetPoolProvider.getAssetPool(Font.class)
      .addAssetId("default", "assets/monospaced_NORMAL_18.json");

    solaGui.setGuiRoot(buildGui());
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGui.render(renderer);
  }

  private GuiElement<?> buildGui() {
    HorizontalContainerGuiElement firstParent = new HorizontalContainerGuiElement(400, 60);
    firstParent.properties().padding.set(5);
    firstParent.properties().margin.setBottom(5);
    firstParent.properties().setBorderColor(Color.YELLOW);

    SimpleButton middleButton = new SimpleButton("Second");
    middleButton.properties().margin.set(0, 15);

    firstParent.addChild(new SimpleButton("First"));
    firstParent.addChild(middleButton);
    firstParent.addChild(new SimpleButton("Third"));


    HorizontalContainerGuiElement secondParent = new HorizontalContainerGuiElement(400, 80);
    secondParent.properties().padding.set(5);
    secondParent.properties().setBorderColor(Color.ORANGE);

    SimpleButton checkButton = new SimpleButton("");
    checkButton.properties().margin.set(0, 15, 0, 0);
    checkButton.setOnMouseUpCallback(event -> checkButton.properties().backgroundColor = checkButton.properties().backgroundColor.equals(Color.RED) ? Color.WHITE : Color.RED);
    SimpleButton toggleOtherButton = new SimpleButton("Toggle other button");
    toggleOtherButton.setOnMouseUpCallback((event) -> checkButton.properties().setHidden(!checkButton.properties().isHidden()));

    secondParent.addChild(checkButton);
    secondParent.addChild(toggleOtherButton);


    VerticalContainerGuiElement rootElement = new VerticalContainerGuiElement(400, 200);
    rootElement.properties().padding.set(10);

    rootElement.properties().setPosition(15, 15);
    rootElement.properties().setBorderColor(Color.GREEN);

    rootElement.addChild(firstParent);
    rootElement.addChild(secondParent);

    return rootElement;
  }

  private class SimpleButton extends ChildlessGuiElement<SimpleButton.Properties> {
    private String text = null;
    private Font font;

    private SimpleButton(String text) {
      properties = new Properties();
      this.text = text;

      font = assetPoolProvider.getAssetPool(Font.class).getAsset("default");

      int textWidth = font.getDimensionsForText(text).width();

      properties.setMaxDimensions(Math.max(textWidth, 5) + 6, 40);

      setOnMouseEnterCallback((event) -> properties.borderColor = Color.GREEN);

      setOnMouseExitCallback((event) -> properties.borderColor = Color.BLUE);
    }

    @Override
    public int getContentWidth() {
      int textWidth = font.getDimensionsForText(text).width();

      return Math.max(textWidth, 5) + 6;
    }

    @Override
    public int getContentHeight() {
      return 40;
    }

    @Override
    public void renderSelf(Renderer renderer, int x, int y) {
      renderer.fillRect(x, y, getContentWidth(), getContentHeight(), properties.borderColor);
      renderer.fillRect(x + 3, y + 3, getContentWidth() - 6, getContentHeight() - 6, properties.backgroundColor);
      renderer.setFont(font);
      renderer.setRenderMode(RenderMode.MASK);
      renderer.drawString(text, x + 3, y + 10, properties.borderColor);
      renderer.setRenderMode(RenderMode.NORMAL);
    }

    private static class Properties extends GuiElementBaseProperties {
      private Color borderColor = Color.BLUE;
      private Color backgroundColor = Color.WHITE;
    }
  }
}
