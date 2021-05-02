package technology.sola.engine.platform.javafx;

import javafx.application.Application;
import javafx.stage.Stage;

public class JavaFxApplication extends Application {
  private static SolaJavaFx solaJavaFx;

  public static void start(SolaJavaFx solaJavaFx, String... args) {
    JavaFxApplication.solaJavaFx = solaJavaFx;
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    solaJavaFx.start(primaryStage);
  }
}
