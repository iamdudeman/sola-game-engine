package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.assets.audio.AudioClip;
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

public class AudioExample extends Sola {
  @Override
  protected SolaConfiguration getConfiguration() {
    return new SolaConfiguration("Gui Example", 800, 600, 30, true);
  }

  @Override
  protected void onInit() {
    assetPoolProvider.getAssetPool(Font.class)
      .addAssetId(GuiElementGlobalProperties.DEFAULT_FONT_ASSET_ID, "assets/monospaced_NORMAL_18.json");

    assetPoolProvider.getAssetPool(AudioClip.class)
        .addAssetId("test_song", "assets/asgaseg.wav");

    solaGui.globalProperties.setDefaultTextColor(Color.WHITE);
    solaGui.setGuiRoot(buildGui());
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGui.render(renderer);
  }

  private GuiElement<?> buildGui() {
    // Loop, Play Once, Pause, Stop

    StreamGuiElementContainer controlsContainer = solaGui.createElement(
      StreamGuiElementContainer::new,
      StreamGuiElementContainer.Properties::new,
      p -> p.setPreferredDimensions(400, 80).padding.set(5).margin.set(8, 0)
    );

    ButtonGuiElement loopButton = createButton("Loop", () -> {
      assetPoolProvider.getAssetPool(AudioClip.class)
        .getAsset("test_song")
        .loop(AudioClip.CONTINUOUS_LOOPING);
    });

    ButtonGuiElement playButton = createButton("Play Once", () -> {
      assetPoolProvider.getAssetPool(AudioClip.class)
        .getAsset("test_song")
        .play();
    });

    ButtonGuiElement pauseButton = createButton("Pause", () -> {
      assetPoolProvider.getAssetPool(AudioClip.class)
        .getAsset("test_song")
        .pause();
    });

    ButtonGuiElement stopButton = createButton("Stop", () -> {
      assetPoolProvider.getAssetPool(AudioClip.class)
        .getAsset("test_song")
        .stop();
    });

    controlsContainer.addChild(
      loopButton,
      playButton,
      pauseButton,
      stopButton
    );

    StreamGuiElementContainer rootElement = solaGui.createElement(
      StreamGuiElementContainer::new,
      StreamGuiElementContainer.Properties::new,
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setPreferredDimensions(500, 500).padding.set(10).setPosition(15, 15)
    );

    rootElement.addChild(
      solaGui.createElement(
        TextGuiElement::new,
        TextGuiElement.Properties::new,
        p -> p.setText("Play a Song").margin.setBottom(10)
      ),
      controlsContainer
    );

    return rootElement;
  }

  private ButtonGuiElement createButton(String text, Runnable action) {
    ButtonGuiElement buttonGuiElement = solaGui.createElement(
      ButtonGuiElement::new,
      ButtonGuiElement.Properties::new,
      p -> p.setText(text).padding.set(5).margin.setRight(5)
    );

    buttonGuiElement.setOnAction(action);

    return buttonGuiElement;
  }
}
