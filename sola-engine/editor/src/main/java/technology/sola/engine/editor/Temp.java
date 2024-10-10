package technology.sola.engine.editor;

import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.platform.javafx.SolaJavaFx;
import technology.sola.engine.platform.javafx.assets.JavaFxPathUtils;

import java.io.IOException;

public class Temp {
  private static final Logger LOGGER = LoggerFactory.getLogger(Temp.class);

  public void run() {
    SolaJavaFx.startOnApplicationThread(() -> {
      Stage primaryStage = new Stage();

      Scene scene = new Scene(mainPain());

      setApplicationIcon(primaryStage);
      primaryStage.setScene(scene);
      primaryStage.setTitle("sola editor");

      primaryStage.show();
    });
  }

  private SplitPane mainPain() {
    SplitPane splitPane = new SplitPane();

    splitPane.orientationProperty().set(Orientation.VERTICAL);

    splitPane.getItems().addAll(topPane(), bottomPane());

    return splitPane;
  }

  private Node topPane() {
    SplitPane topPane = new SplitPane();

    VBox leftControl  = new VBox(new Label("Left Control"));
    VBox centerStuff  = new VBox(new Label("Center stuff"));
    VBox rightControl = new VBox(new Label("Right Control"));

    leftControl.setPrefWidth(200);
    centerStuff.setMinWidth(400);
    centerStuff.setMinHeight(400);
    rightControl.setPrefWidth(200);

    topPane.getItems().addAll(leftControl, centerStuff, rightControl);

    return topPane;
  }

  private Node bottomPane() {
    VBox vBox = new VBox(new Label("Bottom Pane"));

    vBox.setPrefHeight(150);

    return vBox;
  }

  private void setApplicationIcon(Stage stage) {
    try {
      var url = JavaFxPathUtils.asUrl("assets/icon.jpg");

      if (url == null) {
        url = JavaFxPathUtils.asUrl("assets/icon.png");
      }

      if (url == null) {
        LOGGER.warn("Icon not found");
      } else {
        stage.getIcons().add(new Image(url.openStream()));
      }
    } catch (IOException ex) {
      LOGGER.error("Failed to load icon", ex);
    }
  }
}
