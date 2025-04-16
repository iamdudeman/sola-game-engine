package technology.sola.engine.editor.core.notifications;

import javafx.application.Platform;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomDialog extends Stage {
  private static Stage parentStage;

  public static void initialize(Stage parentStage) {
    CustomDialog.parentStage = parentStage;
  }

  public CustomDialog() {
    assertInitialized();

    initModality(Modality.APPLICATION_MODAL);
    initOwner(CustomDialog.parentStage);

    Platform.runLater(() -> {
      // position custom dialogs at center of window
      setX(parentStage.getX() + parentStage.getWidth() / 2 - getWidth() / 2);
      setY(parentStage.getY() + parentStage.getHeight() / 2 - getHeight() / 2);
    });
  }

  private static void assertInitialized() {
    if (CustomDialog.parentStage == null) {
      throw new IllegalStateException("CustomDialog has not been initialized.");
    }
  }
}
