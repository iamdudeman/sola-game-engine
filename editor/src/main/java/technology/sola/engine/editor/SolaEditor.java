package technology.sola.engine.editor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import technology.sola.engine.editor.screens.LandingScreenController;

public class SolaEditor extends Application {
  public static void start(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    SolaEditorContext solaEditorContext = new SolaEditorContext(primaryStage);
    final Scene scene = JavaFxController.loadSceneForController(new LandingScreenController(solaEditorContext));

    primaryStage.setOnCloseRequest(event -> {
      // TODO probably use a dialog to prompt "are you sure?" first
      Platform.exit();
    });
    primaryStage.setTitle("Sola Editor");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
