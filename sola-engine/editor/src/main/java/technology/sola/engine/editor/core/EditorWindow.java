package technology.sola.engine.editor.core;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.platform.javafx.SolaJavaFx;
import technology.sola.engine.platform.javafx.assets.JavaFxPathUtils;

import java.io.IOException;

public class EditorWindow {
  private static final Logger LOGGER = LoggerFactory.getLogger(EditorWindow.class);

  public void show() {
    SolaJavaFx.startOnApplicationThread(() -> {
      Stage primaryStage = new Stage();

      Scene scene = new Scene(mainPane());

      setApplicationIcon(primaryStage);
      primaryStage.setScene(scene);
      primaryStage.setTitle("sola editor");

      primaryStage.show();
    });
  }

  private Parent mainPane() {
    SplitPane splitPane = new SplitPane();

    splitPane.orientationProperty().set(Orientation.VERTICAL);

    // todo icon bar on the left which controls content in panes
    //  one per asset type?

    splitPane.getItems().addAll(topPane(), bottomPane());

    var parent = new HBox(toolbar(), splitPane);

    parent.setPrefHeight(600);
    parent.setPrefWidth(800);

    splitPane.prefWidthProperty().bind(parent.widthProperty());
    splitPane.prefHeightProperty().bind(parent.heightProperty());

    return parent;
  }

  private Node toolbar() {
    ToolBar toolBar = new ToolBar();

    toolBar.setOrientation(Orientation.VERTICAL);

    Button buttonOne = new Button("First");
    Button buttonTwo = new Button("Second");

    toolBar.getItems().addAll(buttonOne, buttonTwo);

    return toolBar;
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
