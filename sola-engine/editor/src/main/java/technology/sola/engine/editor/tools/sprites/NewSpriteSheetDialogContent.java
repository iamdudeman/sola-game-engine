package technology.sola.engine.editor.tools.sprites;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.core.utils.DialogService;
import technology.sola.engine.editor.core.utils.FileUtils;
import technology.sola.engine.editor.core.utils.ToastService;
import technology.sola.json.JsonArray;
import technology.sola.json.JsonObject;
import technology.sola.logging.SolaLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * SpritesFontDialogContent is a form for creating a new sprites asset that can be easily nested in
 * a {@link DialogService#custom(String, Parent)}.
 */
public class NewSpriteSheetDialogContent extends EditorPanel {
  private static final SolaLogger LOGGER = SolaLogger.of(NewSpriteSheetDialogContent.class, "logs/sola-editor.log");
  private final Button chooseFileButton;
  private final TextField spriteSheetNameTextField;
  private final TextField extensionTextField;
  private final Button cancelButton;
  private final Button createButton;
  private File imageFile;

  /**
   * Creates a new instance. The parentFolder specified is where the new font asset will be created if the create
   * button is clicked. After a new font is created the onAfterCreate {@link Runnable} will fire.
   *
   * @param parentFolder  the parent {@link File} to create the sprites asset in
   * @param onAfterCreate the callback to run after font creation completes
   */
  public NewSpriteSheetDialogContent(File parentFolder, Runnable onAfterCreate) {
    setSpacing(8);

    chooseFileButton = new Button("Choose a file");
    spriteSheetNameTextField = new TextField("new-spritesheet");
    extensionTextField = new TextField("");

    // buttons
    cancelButton = new Button("Cancel");
    createButton = new Button("Create");

    initializeUiStateAndEvents(parentFolder, onAfterCreate);

    HBox hBox = new HBox();
    hBox.setSpacing(8);
    hBox.getChildren().addAll(chooseFileButton, spriteSheetNameTextField, extensionTextField);

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
      var newFile = DialogService.filePicker("Pick an image for the SpriteSheet", new FileChooser.ExtensionFilter("Images", List.of("*.jpg", "*.jpeg", "*.png")));

      if (newFile == null) {
        return;
      }

      imageFile = newFile;

      String[] parts = imageFile.getName().split("\\.");

      spriteSheetNameTextField.setText(parts[0]);
      extensionTextField.setText(parts[1]);

      createButton.setDisable(false);
    });

    createButton.setOnAction(event -> {
      try {
        // copy image
        var newImageName = spriteSheetNameTextField.getText() + "." + extensionTextField.getText();

        Files.copy(imageFile.toPath(), new File(parentFolder, newImageName).toPath(), StandardCopyOption.REPLACE_EXISTING);

        // create SpriteSheet json
        JsonObject jsonObject = new JsonObject();

        jsonObject.put("spriteSheet", newImageName);
        jsonObject.put("sprites", new JsonArray());

        FileUtils.writeJson(new File(parentFolder, spriteSheetNameTextField.getText() + ".sprites.json"), jsonObject);

        // finish
        onAfterCreate.run();
        closeParentStage();
      } catch (IOException ex) {
        ToastService.error("Error creating new SpriteSheet");
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
