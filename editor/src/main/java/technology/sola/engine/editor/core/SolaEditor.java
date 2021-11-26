package technology.sola.engine.editor.core;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import technology.sola.engine.editor.screens.LandingScreenController;
import technology.sola.engine.editor.screens.SolaEditorScreen;

public class SolaEditor extends Application {
  public static void start(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    SolaEditorContext solaEditorContext = new SolaEditorContext(primaryStage);
    final Scene scene = SolaEditorScreen.loadSceneForController(new LandingScreenController(solaEditorContext));

    primaryStage.setOnCloseRequest(event -> {
      // TODO probably use a dialog to prompt "are you sure?" first
      Platform.exit();
    });
    primaryStage.setX(Screen.getPrimary().getVisualBounds().getWidth() / 8);
    primaryStage.setY(Screen.getPrimary().getVisualBounds().getHeight() / 4);
    primaryStage.setTitle("Sola Editor");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
