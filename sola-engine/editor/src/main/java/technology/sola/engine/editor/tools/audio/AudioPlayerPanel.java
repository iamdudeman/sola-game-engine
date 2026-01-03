package technology.sola.engine.editor.tools.audio;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.editor.core.components.EditorPanel;

import java.util.Locale;

@NullMarked
class AudioPlayerPanel extends EditorPanel {
  private final AssetLoader<AudioClip> audioClipAssetLoader;
  private final Button playPauseButton;
  private final Slider volumeSlider;
  private final Text volumeText;
  @Nullable
  private AudioClip audioClip;

  AudioPlayerPanel(AssetLoader<AudioClip> audioClipAssetLoader, String fileId) {
    this.audioClipAssetLoader = audioClipAssetLoader;
    playPauseButton = buildPlayButton();
    volumeSlider = buildVolumeSlider();
    volumeText = new Text("---.-");

    getChildren().add(buildButtonsUi());

    loadAudioClip(fileId);
  }

  void loadAudioClip(String fileId) {
    playPauseButton.setText("Play");
    playPauseButton.setDisable(true);
    volumeSlider.setDisable(true);
    volumeText.setText("---.-");

    audioClipAssetLoader
      .get(fileId)
      .executeWhenLoaded(audioClip -> {
        this.audioClip = audioClip;

        audioClip.addFinishListener(ac -> {
          ac.stop();
          playPauseButton.setText("Play");
        });

        playPauseButton.setDisable(false);
        volumeSlider.setDisable(false);

        volumeSlider.setValue(audioClip.getVolume());
        volumeText.setText(String.format(Locale.US, "%.2f", volumeSlider.getValue()));
      });
  }

  private Pane buildButtonsUi() {
    var container = new VBox();

    var volumeContainer = new HBox();

    volumeContainer.setSpacing(8);
    volumeContainer.getChildren().addAll(volumeText, volumeSlider);

    container.getChildren().addAll(playPauseButton, volumeContainer);

    container.setSpacing(8);
    container.setStyle("-fx-font-size: 20px");

    return container;
  }

  private Button buildPlayButton() {
    var playPauseButton = new Button("Play");
    playPauseButton.setDisable(true);
    playPauseButton.setOnAction(event -> {
      if (audioClip == null) {
        return;
      }

      if (audioClip.isPlaying()) {
        playPauseButton.setText("Play");
        audioClip.pause();
      } else {
        playPauseButton.setText("Pause");
        audioClip.play();
      }
    });

    return playPauseButton;
  }

  private Slider buildVolumeSlider() {
    var volumeSlider = new Slider(0, 1, 0.01);

    volumeSlider.setDisable(true);
    volumeSlider.setShowTickMarks(true);
    volumeSlider.setShowTickLabels(true);
    volumeSlider.setMajorTickUnit(0.25);
    volumeSlider.setMinorTickCount(4);
    volumeSlider.setBlockIncrement(0.05);
    volumeSlider.setSnapToTicks(true);

    volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
      if (audioClip == null) {
        return;
      }

      audioClip.setVolume(newValue.floatValue());
      volumeText.setText(String.format(Locale.US, "%.2f", volumeSlider.getValue()));
    });

    volumeSlider.setLabelFormatter(new StringConverter<>() {
      @Override
      public String toString(Double aDouble) {
        return "" + (int) (aDouble * 100);
      }

      @Override
      public Double fromString(String s) {
        return Double.parseDouble(s) / 100;
      }
    });

    return volumeSlider;
  }
}
