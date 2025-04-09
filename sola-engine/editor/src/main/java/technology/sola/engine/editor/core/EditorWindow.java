package technology.sola.engine.editor.core;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.editor.font.FontLeftPanel;
import technology.sola.engine.platform.javafx.SolaJavaFx;
import technology.sola.engine.platform.javafx.assets.JavaFxPathUtils;

import java.io.IOException;
import java.util.List;

public class EditorWindow {
  private static final Logger LOGGER = LoggerFactory.getLogger(EditorWindow.class);
  private SplitPane mainPane;

  private SplitPane topPane;
  private Region leftPanel;
  private Region centerPanel;
  private Region rightPanel;

  private Region bottomPanel;

  private List<EditorTab> editorTabs;

  public void show() {
    SolaJavaFx.startOnApplicationThread(() -> {
      editorTabs = buildTabs();

      Stage primaryStage = new Stage();

      Scene scene = new Scene(mainPane());

      switchTab(editorTabs.get(0));

      scene.getStylesheets().add("utility-styles.css");

      setApplicationIcon(primaryStage);
      primaryStage.setScene(scene);
      primaryStage.setTitle("sola editor");

      primaryStage.show();
    });
  }

  private List<EditorTab> buildTabs() {
    return List.of(
      new EditorTab(
        "Font", new FontLeftPanel()
      ),
      new EditorTab("Blah")
    );
  }

  private Parent mainPane() {
    mainPane = new SplitPane();

    mainPane.orientationProperty().set(Orientation.VERTICAL);

    topPane = new SplitPane();

    mainPane.getItems().addAll(topPane);

    var parent = new HBox(toolbar(), mainPane);

    // todo hook up "remembered" size from a config file
    parent.setPrefHeight(600);
    parent.setPrefWidth(800);

    mainPane.prefWidthProperty().bind(parent.widthProperty());
    mainPane.prefHeightProperty().bind(parent.heightProperty());

    return parent;
  }

  private Node toolbar() {
    ToolBar toolBar = new ToolBar();

    toolBar.setOrientation(Orientation.VERTICAL);

    var toolbarItems = toolBar.getItems();

    editorTabs.forEach(editorTab -> {
      toolbarItems.add(createEditorTabButton(editorTab));
    });


    return toolBar;
  }

  private Button createEditorTabButton(EditorTab editorTab) {
    Button button = new Button(editorTab.label());

    button.setOnAction((event) -> {
      switchTab(editorTab);
    });

    return button;
  }

  private void switchTab(EditorTab editorTab) {
    var items = topPane.getItems();
    var newLeftPanel = editorTab.leftPanel();
    var newCenterPanel = editorTab.centerPanel();
    var newRightPanel = editorTab.rightPanel();
    var newBottomPanel = editorTab.bottomPanel();

    // todo hook up values from a config file here
    newLeftPanel.setPrefWidth(200);
    newCenterPanel.setMinWidth(400);
    newCenterPanel.setMinHeight(400);
    newRightPanel.setPrefWidth(200);

    if (leftPanel == null) {
      items.add(0, newLeftPanel);
    } else {
      items.set(items.indexOf(leftPanel), newLeftPanel);
    }

    if (centerPanel == null) {
      items.add(1, newCenterPanel);
    } else {
      items.set(items.indexOf(centerPanel), newCenterPanel);
    }

    if (rightPanel == null) {
      items.add(2, newRightPanel);
    } else {
      items.set(items.indexOf(rightPanel), newRightPanel);
    }

    if (bottomPanel == null) {
      mainPane.getItems().add(1, newBottomPanel);
    } else {
      mainPane.getItems().set(mainPane.getItems().indexOf(bottomPanel), newBottomPanel);
    }

    leftPanel = newLeftPanel;
    centerPanel = newCenterPanel;
    rightPanel = newRightPanel;
    bottomPanel = newBottomPanel;
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
