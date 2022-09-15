package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.module.graphics.gui.SolaGui;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.control.ButtonGuiElement;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;

public class GuiExample extends Sola {
  private SolaGui solaGui;

  public GuiExample() {
    super(SolaConfiguration.build("Gui Example", 800, 600).withTargetUpdatesPerSecond(30).withGameLoopRestingOn());
  }

  @Override
  protected void onInit() {
    solaGui = SolaGui.createInstance(assetLoaderProvider, platform);

    solaGui.globalProperties.setDefaultTextColor(Color.WHITE);
    solaGui.setGuiRoot(buildGui());

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
      ButtonGuiElement.Properties::new,
      p -> p.margin.setRight(15).padding.set(15)
    );
    checkButton.setOnAction(() -> checkButton.properties().setColorBackground(checkButton.properties().getColorBackground().equals(Color.RED) ? new Color(128, 128, 128) : Color.RED));

    ButtonGuiElement toggleOtherButton = solaGui.createElement(
      ButtonGuiElement::new,
      ButtonGuiElement.Properties::new,
      p -> p.setText("Toggle other button").padding.set(5)
    );
    toggleOtherButton.setOnAction(() -> checkButton.properties().setHidden(!checkButton.properties().isHidden()));

    ButtonGuiElement toggleFontButton = solaGui.createElement(
      ButtonGuiElement::new,
      ButtonGuiElement.Properties::new,
      p -> p.setText("Change font").padding.set(5)
    );
    toggleFontButton.setOnAction(() -> solaGui.globalProperties.setDefaultFontAssetId("times_NORMAL_18"));


    StreamGuiElementContainer firstContainer = solaGui.createElement(
      StreamGuiElementContainer::new,
      StreamGuiElementContainer.Properties::new,
      p -> p.setPreferredDimensions(400, 260).setBorderColor(Color.YELLOW).padding.set(5)
    );

    StreamGuiElementContainer firstSubContainer = solaGui.createElement(
      StreamGuiElementContainer::new,
      StreamGuiElementContainer.Properties::new,
      p -> p.setPreferredDimensions(130, 200).setDirection(StreamGuiElementContainer.Direction.VERTICAL).setBorderColor(Color.WHITE).padding.set(5).margin.setLeft(10)
    );
    firstSubContainer.addChild(
      solaGui.createElement(ButtonGuiElement::new, ButtonGuiElement.Properties::new, p -> p.setText("Sub First")),
      solaGui.createElement(ButtonGuiElement::new, ButtonGuiElement.Properties::new, p -> p.setText("Sub Second").margin.set(15, 0))
    );

    firstContainer.addChild(
      solaGui.createElement(ButtonGuiElement::new, ButtonGuiElement.Properties::new, p -> p.setText("First")),
      solaGui.createElement(ButtonGuiElement::new, ButtonGuiElement.Properties::new, p -> p.setText("Second").padding.set(5).margin.set(0, 15)),
      solaGui.createElement(ButtonGuiElement::new, ButtonGuiElement.Properties::new, p -> p.setText("Third").margin.setTop(3)),
      firstSubContainer
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
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setPreferredDimensions(500, 500).setBorderColor(Color.GREEN).padding.set(10).setPosition(15, 15)
    );

    rootElement.addChild(
      solaGui.createElement(
        TextGuiElement::new,
        TextGuiElement.Properties::new,
        p -> p.setText("Gui Example").margin.setBottom(10)
      ),
      firstContainer,
      secondContainer,
      toggleFontButton,
      createKeyTesterElement()
    );

    return rootElement;
  }

  private GuiElement<?> createKeyTesterElement() {
    TextGuiElement textGuiElement = solaGui.createElement(
      TextGuiElement::new,
      TextGuiElement.Properties::new,
      p -> p.setText("Type a key").setColorText(Color.WHITE).setFocusable(true).padding.set(3).margin.setTop(10).setFocusOutlineColor(Color.LIGHT_BLUE)
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
}
