package technology.sola.engine.editor.tools.audio;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.platform.javafx.assets.audio.JavaFxAudioClipAssetLoader;

import java.io.File;

@NullMarked
class AudioPlayerPanel extends EditorPanel {
  @Nullable
  private AudioClip audioClip;
  private Button playPauseButton;

  AudioPlayerPanel(File audioFile) {
    playPauseButton = new Button("Play");
    playPauseButton.setDisable(true);

    playPauseButton.setOnAction(event -> {
      if (audioClip.isPlaying()) {
        playPauseButton.setText("Play");
        audioClip.pause();
      } else {
        playPauseButton.setText("Pause");
        audioClip.play();
      }
    });

    getChildren().add(buildButtonsUi());

    new JavaFxAudioClipAssetLoader()
      .getNewAsset(audioFile.getPath(), audioFile.getAbsolutePath())
      .executeWhenLoaded(audioClip -> {
        this.audioClip = audioClip;

        audioClip.addFinishListener(AudioClip::stop);

        playPauseButton.setDisable(false);
      });
  }

  void cleanup() {
    if (audioClip != null) {
      audioClip.dispose();
    }
  }

  private HBox buildButtonsUi() {
    HBox container = new HBox();

    container.getChildren().add(playPauseButton);

    return container;
  }

  // todo loop button
  // todo stop button
  // todo volume up button
  // todo volume down button
  // todo volume number field
}
