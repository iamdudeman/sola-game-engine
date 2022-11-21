package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.assets.BulkAssetLoader;
import technology.sola.engine.assets.audio.AudioClip;
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
import technology.sola.engine.graphics.screen.AspectMode;

public class AudioExample extends Sola {
  private SolaGui solaGui;

  public AudioExample() {
    super(SolaConfiguration.build("Audio Example", 600, 400).withTargetUpdatesPerSecond(30).withGameLoopRestingOn());
  }

  @Override
  protected void onInit() {
    solaInitialization.useAsyncInitialization();
    platform.getViewport().setAspectMode(AspectMode.STRETCH);
    solaGui = SolaGui.useModule(assetLoaderProvider, platform);

    solaGui.globalProperties.setDefaultTextColor(Color.WHITE);
    solaGui.globalProperties.setDefaultFontAssetId("arial");

    new BulkAssetLoader(assetLoaderProvider)
      .addAsset(Font.class, "arial", "assets/arial_NORMAL_16.json")
      .addAsset(AudioClip.class, "test_song", "assets/asgaseg.wav")
      .loadAll()
      .onComplete(assets -> {
        if (assets[1] instanceof AudioClip audioClip) {
          audioClip.setVolume(0.5f);

          solaGui.setGuiRoot(buildGui(audioClip), 15, 15);
        }

        solaInitialization.completeAsyncInitialization();
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
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setGap(8).padding.set(10)
    );

    rootElement.addChild(
      solaGui.createElement(
        TextGuiElement::new,
        TextGuiElement.Properties::new,
        p -> p.setText("Play a Song").padding.set(5)
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
      p -> p.padding.set(5)
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
      p -> p.padding.set(5)
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
