package technology.sola.engine.editor.ui.dialog;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Window;
import technology.sola.engine.editor.core.FolderUtils;
import technology.sola.engine.editor.core.SolaEditorContext;
import technology.sola.json.JsonArray;
import technology.sola.json.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class NewSpriteSheetDialog extends Dialog<File> {
  @FXML
  private VBox container;
  @FXML
  private Button buttonPickImage;
  @FXML
  private Label labelCurrentImage;
  @FXML
  private ImageView imageViewSpriteSheet;

  private File selectedFile;

  public NewSpriteSheetDialog(Window owner, SolaEditorContext solaEditorContext) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("NewSpriteSheetDialog.fxml"));

    loader.setController(this);

    DialogPane dialogPane = null;
    try {
      dialogPane = loader.load();
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    initOwner(owner);
    initModality(Modality.APPLICATION_MODAL);

    setResizable(true);
    setTitle("New Sprite Sheet");
    setDialogPane(dialogPane);
    setResultConverter(buttonType -> {
      if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
        File spritesFolder = new FolderUtils(solaEditorContext).getOrCreateFolder("assets/sprites");

        try {
          Files.copy(selectedFile.toPath(), new File(spritesFolder, labelCurrentImage.getText()).toPath());
          Files.writeString(
            new File(spritesFolder, labelCurrentImage.getText().replace(".png", ".json")).toPath(),
            createInitialSpriteSheetJson(labelCurrentImage.getText())
          );
        } catch (IOException ex) {
          ex.printStackTrace();;
        }
      }

      return null;
    });

    buttonPickImage.setOnAction(event -> {
      FileChooser.ExtensionFilter pngExtensionFilter = new FileChooser.ExtensionFilter("Png files", "*.png");
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Pick image");
      fileChooser.getExtensionFilters().add(pngExtensionFilter);
      fileChooser.setSelectedExtensionFilter(pngExtensionFilter);

      selectedFile = fileChooser.showOpenDialog(owner);

      Image image = new Image(selectedFile.toURI().toString());

      labelCurrentImage.setText(selectedFile.getName());
      imageViewSpriteSheet.setImage(image);
    });
  }

  private String createInitialSpriteSheetJson(String imageFileName) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("sprites", new JsonArray());
    jsonObject.put("spriteSheet", imageFileName);

    return jsonObject.toString();
  }
}
