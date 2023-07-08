package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.assets.BulkAssetLoader;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.properties.GuiPropertyDefaults;
import technology.sola.engine.graphics.screen.AspectMode;

public class AudioExample extends SolaWithDefaults {
  public AudioExample() {
    super(SolaConfiguration.build("Audio Example", 600, 400).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGui(new GuiPropertyDefaults(
      "arial_NORMAL_16",
      Color.BLUE,
      Color.WHITE
    ));

    platform.getViewport().setAspectMode(AspectMode.STRETCH);
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    new BulkAssetLoader(assetLoaderProvider)
      .addAsset(Font.class, "arial_NORMAL_16", "assets/arial_NORMAL_16.json")
      .addAsset(AudioClip.class, "test_song", "assets/asgaseg.wav")
      .loadAll()
      .onComplete(assets -> {
        if (assets[1] instanceof AudioClip audioClip) {
          audioClip.setVolume(0.25f);

          var guiRoot = buildGui(audioClip);

          solaGuiDocument.setGuiRoot(guiRoot, 15, 15);
          guiRoot.requestFocus();
        }

        completeAsyncInit.run();
      });
  }

  private GuiElement<?> buildGui(AudioClip audioClip) {
    return solaGuiDocument.createElement(
      StreamGuiElementContainer::new,
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setGap(8).padding.set(10),
      solaGuiDocument.createElement(
        TextGuiElement::new,
        p -> p.setText("Play a Song").padding.set(5)
      ),
      buildControlsContainer(audioClip),
      buildVolumeContainer(audioClip)
    );
  }

  private StreamGuiElementContainer buildVolumeContainer(AudioClip audioClip) {
    TextGuiElement volumeTextGuiElement = solaGuiDocument.createElement(
      TextGuiElement::new,
      p -> p.setText(formatAudioVolume(audioClip.getVolume()))
    );

    return solaGuiDocument.createElement(
      StreamGuiElementContainer::new,
      p -> p.padding.set(5),
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
  }

  private GuiElement<?> buildControlsContainer(AudioClip audioClip) {
    return solaGuiDocument.createElement(
      StreamGuiElementContainer::new,
      p -> p.padding.set(5),
      createButton("Loop", () -> audioClip.loop(AudioClip.CONTINUOUS_LOOPING)),
      createButton("Play Once", audioClip::play),
      createButton("Pause", audioClip::pause),
      createButton("Stop", audioClip::stop)
    );
  }

  private ButtonGuiElement createButton(String text, Runnable action) {
    return solaGuiDocument.createElement(
      ButtonGuiElement::new,
      p -> p.setText(text).padding.set(5).margin.setRight(5)
    ).setOnAction(action);
  }

  private String formatAudioVolume(float volume) {
    return Integer.toString(Math.round(volume * 100));
  }
}
