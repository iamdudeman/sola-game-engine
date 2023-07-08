package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.BaseTextGuiElement;
import technology.sola.engine.graphics.gui.elements.ImageGuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.elements.input.TextInputGuiElement;
import technology.sola.engine.input.Key;

/**
 * GuiExample is a {@link technology.sola.engine.core.Sola} that shows an example custom gui using various
 * {@link GuiElement}s.
 */
public class GuiExample extends SolaWithDefaults {
  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public GuiExample() {
    super(SolaConfiguration.build("Gui Example", 800, 700).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGui();

    var guiRoot = buildGui();
    solaGuiDocument.setGuiRoot(guiRoot, 15, 15);
    guiRoot.requestFocus();

    assetLoaderProvider.get(SolaImage.class)
      .addAssetMapping("test", "assets/test_tiles.png");
    assetLoaderProvider.get(Font.class)
      .addAssetMapping("times_NORMAL_18", "assets/times_NORMAL_18.json");
  }

  private GuiElement<?> buildGui() {
    ButtonGuiElement checkButton = solaGuiDocument.createElement(
      ButtonGuiElement::new,
      p -> p.margin.setRight(15).setWidth(30).setHeight(30)
    );
    checkButton.setOnAction(() -> checkButton.properties().setBackgroundColor(checkButton.properties().getBackgroundColor().equals(Color.RED) ? new Color(128, 128, 128) : Color.RED));

    ButtonGuiElement toggleOtherButton = solaGuiDocument.createElement(
      ButtonGuiElement::new,
      p -> p.setText("Toggle other button").setTextAlign(BaseTextGuiElement.TextAlign.RIGHT).padding.set(5).setWidth(250)
    );
    toggleOtherButton.setOnAction(() -> checkButton.properties().setHidden(!checkButton.properties().isHidden()));

    return solaGuiDocument.createElement(
      StreamGuiElementContainer::new,
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setGap(15).padding.set(3).setBorderColor(Color.GREEN),
      solaGuiDocument.createElement(
        TextGuiElement::new,
        p -> p.setText("Gui Example").margin.setBottom(10)
      ),
      solaGuiDocument.createElement(
        StreamGuiElementContainer::new,
        p -> p.setGap(10).setBorderColor(Color.YELLOW).padding.set(5),
        solaGuiDocument.createElement(ButtonGuiElement::new, p -> p.setText("First")),
        solaGuiDocument.createElement(ButtonGuiElement::new, p -> p.setText("Second").padding.set(5)),
        solaGuiDocument.createElement(ButtonGuiElement::new, p -> p.setText("Third").margin.setTop(30)),
        solaGuiDocument.createElement(
          StreamGuiElementContainer::new,
          p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setBorderColor(Color.RED).padding.set(5)
            .setBackgroundColor(new Color(100, 50, 50, 255)),
          solaGuiDocument.createElement(ButtonGuiElement::new, p -> p.setText("Sub First")),
          solaGuiDocument.createElement(ButtonGuiElement::new, p -> p.setText("Sub Second").margin.set(15, 0))
        )
      ),
      solaGuiDocument.createElement(
        StreamGuiElementContainer::new,
        p -> p.setHorizontalAlignment(StreamGuiElementContainer.HorizontalAlignment.RIGHT).setBorderColor(Color.ORANGE).padding.set(5).setWidth(410),
        checkButton,
        toggleOtherButton
      ),
      solaGuiDocument.createElement(
        StreamGuiElementContainer::new,
        p -> p.setDirection(StreamGuiElementContainer.Direction.HORIZONTAL).setGap(5),
        solaGuiDocument.createElement(
          ButtonGuiElement::new,
          p -> p.setText("Enable text input").padding.set(5)
        ).setOnAction(() -> ((TextInputGuiElement) solaGuiDocument.getElementById("textInput")).properties().setDisabled(false)),
        solaGuiDocument.createElement(
          TextInputGuiElement::new,
          p -> p.setPlaceholder("Placeholder").setMaxLength(15).setDisabled(true).setId("textInput")
        )
      ),
      createKeyTesterElement(),
      createImageContainer(),
      solaGuiDocument.createElement(
        TextGuiElement::new,
        p -> p.setText("This is a longer text that should wrap a bit. It might even wrap to a third line.").padding.set(15).setWidth(380).setBorderColor(Color.DARK_GRAY)
      )
    );
  }

  private GuiElement<?> createKeyTesterElement() {
    TextGuiElement textGuiElement = solaGuiDocument.createElement(
      TextGuiElement::new,
      p -> p.setText("Type a key").setFocusable(true).padding.set(3).margin.setTop(5).setFocusOutlineColor(Color.LIGHT_BLUE).setBorderColor(Color.BLACK).hover.setBorderColor(Color.YELLOW)
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
    return solaGuiDocument.createElement(
      StreamGuiElementContainer::new,
      p -> p.setGap(5),
      solaGuiDocument.createElement(
        ImageGuiElement::new,
        p -> p.setAssetId("test").setBorderColor(Color.ORANGE).padding.set(5)
      ),
      solaGuiDocument.createElement(
        ImageGuiElement::new,
        p -> p.setAssetId("test").setWidth(75).setHeight(75).setBorderColor(Color.GREEN).padding.set(5)
      ),
      solaGuiDocument.createElement(
        ImageGuiElement::new,
        p -> p.setAssetId("test").setWidth(150).setHeight(150).setBorderColor(Color.YELLOW).padding.set(5)
      )
    );
  }
}
