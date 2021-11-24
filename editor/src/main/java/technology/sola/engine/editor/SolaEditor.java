package technology.sola.engine.editor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import technology.sola.engine.editor.controllers.LandingScreenController;

import java.io.IOException;

public class SolaEditor extends Application {
  public static void start(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    final Scene scene = JavaFxController.loadSceneForController(new LandingScreenController());

    primaryStage.setTitle("Sola Editor");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
