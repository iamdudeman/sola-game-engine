package technology.sola.engine.editor;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SolaEditor extends Application {
  public static void start(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    final Group root = new Group();
    final Scene scene = new Scene(root);

    primaryStage.setTitle("Sola Editor");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
