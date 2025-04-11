package technology.sola.engine.editor.core.notifications;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * ConfirmationDialog contains methods for showing various types of confirmation dialogs.
 */
public class ConfirmationDialog {
  /**
   * Shows a warning dialog with desired title and content. Typically used for warning the user about an action that
   * cannot be reversed (i.e. deleting a folder containing other files). This method will wait until the user has
   * selected an option. True will be returned if the user has confirmed.
   *
   * @param title   the title of the dialog
   * @param content the content of the dialog
   * @return true if the user confirmed
   */
  public static boolean warning(String title, String content) {
    var alert = new Alert(
      Alert.AlertType.WARNING,
      content,
      ButtonType.YES, ButtonType.CANCEL
    );

    alert.setHeaderText(null);
    alert.setTitle(title);
    alert.setResizable(false);

    var result = alert.showAndWait();

    return result.isPresent() && result.get() == ButtonType.YES;
  }

  private ConfirmationDialog() {
  }
}
