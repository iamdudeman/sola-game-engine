package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.assets.BulkAssetLoader;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.elements.SectionGuiElement;
import technology.sola.engine.graphics.guiv2.elements.TextGuiElement;
import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.engine.graphics.guiv2.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.ConditionalStyle;
import technology.sola.engine.graphics.guiv2.style.property.CrossAxisChildren;
import technology.sola.engine.graphics.guiv2.style.property.Direction;
import technology.sola.engine.graphics.guiv2.style.theme.GuiTheme;
import technology.sola.engine.graphics.screen.AspectMode;

import java.util.List;

/**
 * AudioExample is a {@link technology.sola.engine.core.Sola} for demoing audio for the sola game engine.
 *
 * <ul>
 *   <li>{@link AudioClip}</li>
 * </ul>
 */
public class AudioExample extends SolaWithDefaults {
  private GuiTheme guiTheme;

  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public AudioExample() {
    super(SolaConfiguration.build("Audio Example", 600, 400).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    guiTheme = GuiTheme.getDefaultLightTheme()
      .addStyle(TextGuiElement.class, List.of(ConditionalStyle.always(
        TextStyles.create().setFontAssetId("arial_NORMAL_16").setTextColor(Color.BLUE).build()
      )))
      .addStyle(SectionGuiElement.class, List.of(ConditionalStyle.always(
        BaseStyles.create().setBackgroundColor(Color.WHITE).build()
      )))
    ;

    defaultsConfigurator.useGuiV2(guiTheme);

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

          guiDocument.setRootElement(guiRoot);
          guiTheme.applyToTree(guiRoot);
        }

        completeAsyncInit.run();
      });
  }

  private GuiElement<?> buildGui(AudioClip audioClip) {
    SectionGuiElement sectionGuiElement = new SectionGuiElement();

    sectionGuiElement.setStyle(List.of(
      ConditionalStyle.always(BaseStyles.create()
        .setDirection(Direction.COLUMN)
        .setCrossAxisChildren(CrossAxisChildren.CENTER)
        .setGap(8)
        .setPadding(10)
        .build())
    ));

    sectionGuiElement.appendChildren(
      new TextGuiElement().setText("Play a Song").setStyle(List.of(
        ConditionalStyle.always(TextStyles.create().setPadding(5).build())
      )),
      buildControlsContainer(audioClip),
      buildVolumeContainer(audioClip)
    );

    return sectionGuiElement;
  }

  private GuiElement<?> buildVolumeContainer(AudioClip audioClip) {
    TextGuiElement volumeTextGuiElement = new TextGuiElement()
      .setText(formatAudioVolume(audioClip.getVolume()));
    SectionGuiElement sectionGuiElement = new SectionGuiElement();

    sectionGuiElement.setStyle(List.of(
      ConditionalStyle.always(
        BaseStyles.create()
          .setDirection(Direction.ROW)
          .setCrossAxisChildren(CrossAxisChildren.CENTER)
          .setGap(5)
          .setPadding(5)
          .build()
      )
    ));

    sectionGuiElement.appendChildren(
      createButton("Vol Up", () -> {
        float newVolume = audioClip.getVolume() + 0.05f;

        if (newVolume > 1) {
          newVolume = 1;
        }

        audioClip.setVolume(newVolume);
        volumeTextGuiElement.setText(formatAudioVolume(newVolume));
      }),
      createButton("Vol Down", () -> {
        float newVolume = audioClip.getVolume() - 0.05f;

        if (newVolume < 0) {
          newVolume = 0;
        }

        audioClip.setVolume(newVolume);
        volumeTextGuiElement.setText(formatAudioVolume(newVolume));
      }),
      volumeTextGuiElement
    );

    return sectionGuiElement;
  }

  private GuiElement<?> buildControlsContainer(AudioClip audioClip) {
    SectionGuiElement sectionGuiElement = new SectionGuiElement();

    sectionGuiElement.setStyle(List.of(
      ConditionalStyle.always(BaseStyles.create().setDirection(Direction.ROW).setGap(5).setPadding(5).build())
    ));

    sectionGuiElement.appendChildren(
      createButton("Loop", () -> audioClip.loop(AudioClip.CONTINUOUS_LOOPING)),
      createButton("Play Once", audioClip::play),
      createButton("Pause", audioClip::pause),
      createButton("Stop", audioClip::stop)
    );

    return sectionGuiElement;
  }

  private ButtonGuiElement createButton(String text, Runnable action) {
    ButtonGuiElement buttonGuiElement = new ButtonGuiElement();

    buttonGuiElement.setStyle(List.of(
      ConditionalStyle.always(
        BaseStyles.create().setPadding(5).build()
      )
    ));

    buttonGuiElement.appendChildren(
      new TextGuiElement().setText(text)
    );

    buttonGuiElement.setOnAction(action);

    return buttonGuiElement;
  }

  private String formatAudioVolume(float volume) {
    return Integer.toString(Math.round(volume * 100));
  }
}
