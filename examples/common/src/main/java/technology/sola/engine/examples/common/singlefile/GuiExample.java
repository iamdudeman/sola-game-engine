package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.module.graphics.gui.SolaGui;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.BaseTextGuiElement;
import technology.sola.engine.graphics.gui.elements.ImageGuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.control.ButtonGuiElement;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;

public class GuiExample extends Sola {
  private SolaGui solaGui;

  public GuiExample() {
    super(SolaConfiguration.build("Gui Example", 800, 600).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit() {
    solaGui = SolaGui.useModule(assetLoaderProvider, platform, eventHub);

    solaGui.globalProperties.setDefaultTextColor(Color.WHITE);
    solaGui.setGuiRoot(buildGui(), 15, 15);

    solaGui.getElementById("changeFont", ButtonGuiElement.class)
      .setOnAction(() -> solaGui.globalProperties.setDefaultFontAssetId("times_NORMAL_18"));

    assetLoaderProvider.get(SolaImage.class)
      .addAssetMapping("test", "assets/test_tiles.png");
    assetLoaderProvider.get(Font.class)
      .addAssetMapping("times_NORMAL_18", "assets/times_NORMAL_18.json");
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGui.render();
  }

  private GuiElement<?> buildGui() {
    ButtonGuiElement checkButton = solaGui.createElement(
      ButtonGuiElement::new,
      p -> p.margin.setRight(15).setWidth(30).setHeight(30)
    );
    checkButton.setOnAction(() -> checkButton.properties().setBackgroundColor(checkButton.properties().getBackgroundColor().equals(Color.RED) ? new Color(128, 128, 128) : Color.RED));

    ButtonGuiElement toggleOtherButton = solaGui.createElement(
      ButtonGuiElement::new,
      p -> p.setText("Toggle other button").setTextAlign(BaseTextGuiElement.TextAlign.RIGHT).padding.set(5).setWidth(250)
    );
    toggleOtherButton.setOnAction(() -> checkButton.properties().setHidden(!checkButton.properties().isHidden()));

    return solaGui.createElement(
      StreamGuiElementContainer::new,
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setGap(15).padding.set(3).setBorderColor(Color.GREEN),
      solaGui.createElement(
        TextGuiElement::new,
        p -> p.setText("Gui Example").margin.setBottom(10)
      ),
      solaGui.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(10).setBorderColor(Color.YELLOW).padding.set(5),
        solaGui.createElement(ButtonGuiElement::new, p -> p.setText("First")),
        solaGui.createElement(ButtonGuiElement::new, p -> p.setText("Second").padding.set(5)),
        solaGui.createElement(ButtonGuiElement::new, p -> p.setText("Third").margin.setTop(30)),
        solaGui.createElement(
          StreamGuiElementContainer::new,
          p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setBorderColor(Color.RED).padding.set(5)
            .setBackgroundColor(new Color(100, 50, 50, 255)),
          solaGui.createElement(ButtonGuiElement::new, p -> p.setText("Sub First")),
          solaGui.createElement(ButtonGuiElement::new, p -> p.setText("Sub Second").margin.set(15, 0))
        )
      ),
      solaGui.createElement(
        StreamGuiElementContainer::new,
        p -> p.setHorizontalAlignment(StreamGuiElementContainer.HorizontalAlignment.RIGHT).setBorderColor(Color.ORANGE).padding.set(5).setWidth(410),
        checkButton,
        toggleOtherButton
      ),
      solaGui.createElement(
        ButtonGuiElement::new,
        p -> p.setText("Change font").padding.set(5).setId("changeFont")
      ),
      createKeyTesterElement(),
      createImageContainer(),
      solaGui.createElement(
        TextGuiElement::new,
        p -> p.setText("This is a longer text that should wrap a bit. It might even wrap to a third line.").padding.set(15).setWidth(380).setBorderColor(Color.DARK_GRAY)
      )
    );
  }

  private GuiElement<?> createKeyTesterElement() {
    TextGuiElement textGuiElement = solaGui.createElement(
      TextGuiElement::new,
      p -> p.setText("Type a key").setColorText(Color.WHITE).setFocusable(true).padding.set(3).margin.setTop(10).setFocusOutlineColor(Color.LIGHT_BLUE).setBorderColor(Color.WHITE)
    );

    textGuiElement.setOnKeyPressCallback(guiKeyEvent -> {
      Key keyExists = null;

      for (Key key : Key.values()) {
        if (key.getCode() == guiKeyEvent.getKeyCode()) {
          keyExists = key;
          break;
        }
      }

      String keyEnumName = keyExists == null ? "No Key enum entry: " : keyExists.getName() + ": ";

      textGuiElement.properties().setText(keyEnumName + guiKeyEvent.getKeyCode());
    });

    return textGuiElement;
  }

  private GuiElement<?> createImageContainer() {
    return solaGui.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(5),
      solaGui.createElement(
        ImageGuiElement::new,
        p -> p.setAssetId("test").setBorderColor(Color.ORANGE).padding.set(5)
      ),
      solaGui.createElement(
        ImageGuiElement::new,
        p -> p.setAssetId("test").setWidth(50).setHeight(50).setBorderColor(Color.GREEN).padding.set(5)
      ),
      solaGui.createElement(
        ImageGuiElement::new,
        p -> p.setAssetId("test").setWidth(150).setHeight(150).setBorderColor(Color.YELLOW).padding.set(5)
      )
    );
  }
}
