package technology.sola.engine.editor.tools.audio;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.editor.SolaEditorConstants;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.core.utils.DialogService;
import technology.sola.engine.editor.core.utils.ToastService;
import technology.sola.logging.SolaLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * NewAudioDialogContent is a form for creating a new audio asset that can be easily nested in
 * a {@link DialogService#custom(String, Parent)}.
 */
@NullMarked
public class NewAudioDialogContent extends EditorPanel {
  private static final SolaLogger LOGGER = SolaLogger.of(NewAudioDialogContent.class, SolaEditorConstants.LOG_FILE);
  private final Button chooseFileButton;
  private final TextField audioClipNameTextField;
  private final TextField extensionTextField;
  private final Button cancelButton;
  private final Button createButton;
  @Nullable
  private File audioClipFile;

  /**
   * Creates a new instance. The parentFolder specified is where the new font asset will be created if the create
   * button is clicked. After a new audio clip is created the onAfterCreate {@link Runnable} will fire.
   *
   * @param parentFolder  the parent {@link File} to create the sprites asset in
   * @param onAfterCreate the callback to run after audio clip creation completes
   */
  public NewAudioDialogContent(File parentFolder, Runnable onAfterCreate) {
    setSpacing(8);

    chooseFileButton = new Button("Choose a file");
    audioClipNameTextField = new TextField("new-audio-clip");
    extensionTextField = new TextField("");

    // buttons
    cancelButton = new Button("Cancel");
    createButton = new Button("Create");

    initializeUiStateAndEvents(parentFolder, onAfterCreate);

    HBox hBox = new HBox();
    hBox.setSpacing(8);
    hBox.getChildren().addAll(chooseFileButton, audioClipNameTextField, extensionTextField);

    getChildren().addAll(
      hBox,
      buildButtonsUi()
    );
  }

  private void initializeUiStateAndEvents(File parentFolder, Runnable onAfterCreate) {
    createButton.setDisable(true);
    extensionTextField.setDisable(true);
    extensionTextField.setPrefWidth(50);

    chooseFileButton.setOnAction(event -> {
      var newFile = DialogService.filePicker("Pick a file for the AudioClip", new FileChooser.ExtensionFilter(
        "Audio", List.of("*.wav", "*.mp3"))
      );

      if (newFile == null) {
        return;
      }

      audioClipFile = newFile;

      String[] parts = audioClipFile.getName().split("\\.");

      audioClipNameTextField.setText(parts[0]);
      extensionTextField.setText(parts[1]);

      createButton.setDisable(false);
    });

    createButton.setOnAction(event -> {
      try {
        // copy image
        var newAudioClipName = audioClipNameTextField.getText() + "." + extensionTextField.getText();

        Files.copy(audioClipFile.toPath(), new File(parentFolder, newAudioClipName).toPath(), StandardCopyOption.REPLACE_EXISTING);

        // finish
        onAfterCreate.run();
        closeParentStage();
      } catch (IOException ex) {
        ToastService.error("Error creating new AudioClip");
        LOGGER.error(ex.getMessage(), ex);
      }
    });
    cancelButton.setOnAction((event) -> {
      closeParentStage();
    });
  }

  private HBox buildButtonsUi() {
    HBox container = new HBox();

    container.setSpacing(8);

    container.getChildren().addAll(
      cancelButton,
      createButton
    );

    return container;
  }
}
