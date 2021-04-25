package technology.sola.engine.platform;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.math.linear.Vector2D;

public class JavaFxSpecificFile extends Application {
  private static final Logger logger = LoggerFactory.getLogger(JavaFxSpecificFile.class);

  public static void start(String... args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Hello World!");
    Button btn = new Button();
    btn.setText("Say 'Hello World'");
    btn.setOnAction(event -> {
      System.out.println("Hello World! " + new Vector2D(5, 6));
      logger.info("Test");
    });

    StackPane root = new StackPane();
    root.getChildren().add(btn);
    primaryStage.setScene(new Scene(root, 300, 250));
    primaryStage.show();
  }
}
