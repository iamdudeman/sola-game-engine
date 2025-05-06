package technology.sola.engine.editor.core.utils;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import technology.sola.engine.editor.core.EditorScene;

import java.io.File;

/**
 * DialogService contains methods for showing various types of dialogs.
 */
public class DialogService {
  private static Stage parentStage;

  /**
   * Initializes the DialogService so dialogs will behave properly when created later.
   *
   * @param parentStage the {@link Stage} that owns the dialogs
   */
  public static void initialize(Stage parentStage) {
    DialogService.parentStage = parentStage;
  }

  /**
   * Shows a custom dialog with desired title and content.
   *
   * @param title   the title of the dialog
   * @param content the content of the dialog
   */
  public static void custom(String title, Parent content) {
    assertInitialized();

    Stage stage = new Stage();

    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initOwner(parentStage);

    stage.setTitle(title);
    stage.setScene(new EditorScene(content));

    stage.show();

    stage.setX(parentStage.getX() + parentStage.getWidth() / 2 - stage.getWidth() / 2);
    stage.setY(parentStage.getY() + parentStage.getHeight() / 2 - stage.getHeight() / 2);
  }

  /**
   * Shows a warning dialog with desired title and content. Typically used for warning the user about an action that
   * cannot be reversed (i.e. deleting a folder containing other files). This method will wait until the user has
   * selected an option. True will be returned if the user has confirmed.
   *
   * @param title   the title of the dialog
   * @param content the content of the dialog
   * @return true if the user confirmed
   */
  public static boolean warningConfirmation(String title, String content) {
    var alert = new Alert(
      Alert.AlertType.WARNING,
      content,
      ButtonType.YES, ButtonType.CANCEL
    );

    alert.initOwner(parentStage);
    alert.setHeaderText(null);
    alert.setTitle(title);
    alert.setResizable(false);

    var result = alert.showAndWait();

    return result.isPresent() && result.get() == ButtonType.YES;
  }

  /**
   * Shows a file picker dialog with desired title and extension filter. The selected {@link File} will be returned if
   * the user confirms selection otherwise null will be returned if the user cancels.
   *
   * @param title           the title of the dialog
   * @param extensionFilter the {@link javafx.stage.FileChooser.ExtensionFilter}
   * @return the file or null
   */
  public static File filePicker(String title, FileChooser.ExtensionFilter extensionFilter) {
    FileChooser fileChooser = new FileChooser();

    fileChooser.setTitle(title);
    fileChooser.setInitialDirectory(new File("./"));
    fileChooser.getExtensionFilters().add(extensionFilter);
    fileChooser.setSelectedExtensionFilter(extensionFilter);

    return fileChooser.showOpenDialog(parentStage);
  }

  private static void assertInitialized() {
    if (DialogService.parentStage == null) {
      throw new IllegalStateException("CustomDialog has not been initialized.");
    }
  }

  private DialogService() {
  }
}
