package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.RenderMode;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.graphics.gui.GuiLayoutDirection;
import technology.sola.engine.graphics.gui.GuiPanel;
import technology.sola.engine.graphics.gui.element.GuiElement;

public class GuiExample extends Sola {
  @Override
  protected SolaConfiguration getConfiguration() {
    return new SolaConfiguration("Gui Example", 800, 600, 30, true);
  }

  @Override
  protected void onInit() {
    assetPoolProvider.getAssetPool(Font.class)
      .addAssetId("default", "assets/monospaced_NORMAL_18.json");

    solaGui.addGuiPanel(buildGuiPanel());
    solaGui.addGuiPanel(buildGuiPanel2());
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGui.render(renderer);
  }

  private GuiPanel buildGuiPanel() {
    GuiPanel guiPanel = new GuiPanel(10, 10, 400, 200);

    guiPanel.setDirection(GuiLayoutDirection.HORIZONTAL);

    SimpleButton simpleButton1 = new SimpleButton("");
    simpleButton1.properties().margin.set(0, 15, 0, 0);
    SimpleButton simpleButton2 = new SimpleButton("Toggle above button");
    simpleButton2.setOnMouseUpCallback((event) -> simpleButton1.properties().setHidden(!simpleButton1.properties().isHidden()));

    guiPanel.addGuiElement(simpleButton1);
    guiPanel.addGuiElement(simpleButton2);

    return guiPanel;
  }

  private GuiPanel buildGuiPanel2() {
    GuiPanel guiPanel = new GuiPanel(10, 200, 400, 200);

    guiPanel.setDirection(GuiLayoutDirection.VERTICAL);

    SimpleButton simpleButton1 = new SimpleButton("");
    simpleButton1.properties().margin.set(0, 15, 10, 0);
    SimpleButton simpleButton2 = new SimpleButton("Toggle above button");
    simpleButton2.setOnMouseUpCallback((event) -> simpleButton1.properties().setHidden(!simpleButton1.properties().isHidden()));

    guiPanel.addGuiElement(simpleButton1);
    guiPanel.addGuiElement(simpleButton2);

    return guiPanel;
  }

  private class SimpleButton extends GuiElement {
    private Color hoverBorderColor = Color.GREEN;
    private Color staticBorderColor = Color.BLUE;

    private Color borderColor = staticBorderColor;


    private Color isSelectedColor = Color.RED;
    private Color notSelectedColor = Color.WHITE;

    private Color selectedColor = notSelectedColor;

    private String text = null;
    private Font font;

    private SimpleButton(String text) {
      this.text = text;

      font = assetPoolProvider.getAssetPool(Font.class).getAsset("default");

      setOnMouseEnterCallback((event) -> {
        borderColor = hoverBorderColor;
        System.out.println("enter");
      });

      setOnMouseExitCallback((event) -> {
        borderColor = staticBorderColor;
        System.out.println("exit");
      });

      setOnMouseUpCallback((event) -> {
        if (selectedColor.equals(notSelectedColor)) {
          selectedColor = isSelectedColor;
        } else {
          selectedColor = notSelectedColor;
        }
        System.out.println("click");
      });
    }

    @Override
    public int getWidth() {
      int textWidth = font.getDimensionsForText(text).width();

      return Math.max(textWidth, 5) + 6;
    }

    @Override
    public int getHeight() {
      return 40;
    }

    @Override
    public void render(Renderer renderer, int x, int y) {
      renderer.fillRect(x, y, getWidth(), getHeight(), borderColor);
      renderer.fillRect(x + 3, y + 3, getWidth() - 6, getHeight() - 6, selectedColor);
      renderer.setFont(font);
      renderer.setRenderMode(RenderMode.MASK);
      renderer.drawString(text, x + 3, y + 10, borderColor);
      renderer.setRenderMode(RenderMode.NORMAL);
    }
  }
}
