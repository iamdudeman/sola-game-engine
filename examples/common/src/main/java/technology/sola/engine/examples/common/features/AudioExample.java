package technology.sola.engine.examples.common.features;

import technology.sola.engine.assets.BulkAssetLoader;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.SectionGuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.TextStyles;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.engine.graphics.gui.style.property.CrossAxisChildren;
import technology.sola.engine.graphics.gui.style.property.Direction;
import technology.sola.engine.graphics.gui.style.theme.GuiTheme;
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
  private static final String FONT_ASSET_ID = "arial_NORMAL_16";
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
        TextStyles.create().setFontAssetId(FONT_ASSET_ID).setTextColor(Color.BLUE).build()
      )))
      .addStyle(SectionGuiElement.class, List.of(ConditionalStyle.always(
        BaseStyles.create().setBackgroundColor(Color.WHITE).build()
      )))
    ;

    defaultsConfigurator.useGui(guiTheme);

    platform.getViewport().setAspectMode(AspectMode.STRETCH);
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    new BulkAssetLoader(assetLoaderProvider)
      .addAsset(Font.class, FONT_ASSET_ID, "assets/font/arial_NORMAL_16.font.json")
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
