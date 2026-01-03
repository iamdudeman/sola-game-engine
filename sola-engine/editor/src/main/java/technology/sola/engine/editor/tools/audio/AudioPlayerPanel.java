package technology.sola.engine.editor.tools.audio;

import javafx.scene.layout.VBox;
import technology.sola.engine.editor.core.components.EditorPanel;

import java.io.File;

class AudioPlayerPanel extends EditorPanel {
  private File audioFile;

  AudioPlayerPanel(File audioFile) {
    this.audioFile = audioFile;
  }
}
