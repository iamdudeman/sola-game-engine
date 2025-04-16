package technology.sola.engine.editor.core.notifications;

import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import technology.sola.engine.editor.core.EditorScene;

public class CustomDialog extends Stage {
  private static Stage parentStage;

  public static void initialize(Stage parentStage) {
    CustomDialog.parentStage = parentStage;
  }

  public static void show(String title, Parent content) {
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

  private static void assertInitialized() {
    if (CustomDialog.parentStage == null) {
      throw new IllegalStateException("CustomDialog has not been initialized.");
    }
  }

  private CustomDialog() {
  }
}
