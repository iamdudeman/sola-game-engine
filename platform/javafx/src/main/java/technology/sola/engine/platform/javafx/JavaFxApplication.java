package technology.sola.engine.platform.javafx;

import javafx.application.Application;
import javafx.stage.Stage;

public class JavaFxApplication extends Application {
  private static JavaFxContainer javaFxContainer;

  public static void start(JavaFxContainer javaFxContainer, String... args) {
    JavaFxApplication.javaFxContainer = javaFxContainer;
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    javaFxContainer.start(primaryStage);
  }
}
