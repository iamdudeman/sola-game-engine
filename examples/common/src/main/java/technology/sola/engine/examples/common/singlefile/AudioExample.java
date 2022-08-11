package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.assets.BulkAssetLoader;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.module.graphics.gui.SolaGui;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementGlobalProperties;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.control.ButtonGuiElement;
import technology.sola.engine.graphics.renderer.Renderer;

public class AudioExample extends Sola {
  private SolaGui solaGui;

  @Override
  protected SolaConfiguration getConfiguration() {
    return new SolaConfiguration("Gui Example", 800, 600, 30, true);
  }

  @Override
  protected void onInit() {
    solaInitialization.setAsyncInitialization();
    BulkAssetLoader bulkAssetLoader = new BulkAssetLoader(assetPoolProvider);

    bulkAssetLoader.loadAll(
      new BulkAssetLoader.BulkAssetDescription(Font.class, GuiElementGlobalProperties.DEFAULT_FONT_ASSET_ID, "assets/monospaced_NORMAL_16.json"),
      new BulkAssetLoader.BulkAssetDescription(AudioClip.class, "test_song", "assets/asgaseg.wav")
    ).onComplete(assets -> {
      if (assets[1] instanceof AudioClip audioClip) {
        audioClip.setVolume(0.5f);

        solaGui = SolaGui.createInstance(assetPoolProvider, platform);
        solaGui.globalProperties.setDefaultTextColor(Color.WHITE);
        solaGui.setGuiRoot(buildGui(audioClip));
      }

      solaInitialization.completeAsync();
    });
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGui.render();
  }

  private GuiElement<?> buildGui(AudioClip audioClip) {
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
      buildControlsContainer(audioClip),
      buildVolumeContainer(audioClip)
    );

    return rootElement;
  }

  private StreamGuiElementContainer buildVolumeContainer(AudioClip audioClip) {
    StreamGuiElementContainer volumeContainer = solaGui.createElement(
      StreamGuiElementContainer::new,
      StreamGuiElementContainer.Properties::new,
      p -> p.setPreferredDimensions(400, 80).padding.set(5).margin.set(8, 0)
    );

    TextGuiElement volumeTextGuiElement = solaGui.createElement(
      TextGuiElement::new,
      TextGuiElement.Properties::new,
      p -> p.setText(formatAudioVolume(audioClip.getVolume()))
    );

    volumeContainer.addChild(
      createButton("Vol Up", () -> {
        float newVolume = audioClip.getVolume() + 0.05f;

        if (newVolume > 1) {
          newVolume = 1;
        }

        audioClip.setVolume(newVolume);
        volumeTextGuiElement.properties().setText(formatAudioVolume(newVolume));
      }),
      createButton("Vol Down", () -> {
        float newVolume = audioClip.getVolume() - 0.05f;

        if (newVolume < 0) {
          newVolume = 0;
        }

        audioClip.setVolume(newVolume);
        volumeTextGuiElement.properties().setText(formatAudioVolume(newVolume));
      }),
      volumeTextGuiElement
    );

    return volumeContainer;
  }

  private StreamGuiElementContainer buildControlsContainer(AudioClip audioClip) {
    StreamGuiElementContainer controlsContainer = solaGui.createElement(
      StreamGuiElementContainer::new,
      StreamGuiElementContainer.Properties::new,
      p -> p.setPreferredDimensions(400, 80).padding.set(5).margin.set(8, 0)
    );

    controlsContainer.addChild(
      createButton("Loop", () -> audioClip.loop(AudioClip.CONTINUOUS_LOOPING)),
      createButton("Play Once", audioClip::play),
      createButton("Pause", audioClip::pause),
      createButton("Stop", audioClip::stop)
    );

    return controlsContainer;
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

  private String formatAudioVolume(float volume) {
    return "" + Math.round(volume * 100);
  }
}
