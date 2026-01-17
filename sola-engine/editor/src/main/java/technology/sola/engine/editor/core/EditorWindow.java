package technology.sola.engine.editor.core;

import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.editor.SolaEditorConstants;
import technology.sola.engine.editor.tools.ToolPanel;
import technology.sola.engine.editor.core.config.EditorConfig;
import technology.sola.engine.editor.core.config.WindowBounds;
import technology.sola.engine.editor.core.utils.DialogService;
import technology.sola.engine.editor.core.utils.ToastService;
import technology.sola.engine.editor.tools.assetlist.AssetListToolPanel;
import technology.sola.engine.editor.tools.audio.AudioToolPanel;
import technology.sola.engine.editor.tools.font.FontToolPanel;
import technology.sola.engine.editor.tools.sprites.SpriteSheetToolPanel;
import technology.sola.engine.platform.javafx.SolaJavaFx;
import technology.sola.engine.platform.javafx.assets.JavaFxPathUtils;
import technology.sola.json.JsonObject;
import technology.sola.logging.SolaLogger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EditorWindow initializes the primary JavaFX {@link Stage} used by the sola editor. It also reads the editor
 * configuration and initializes the various editor tools.
 */
@NullMarked
public class EditorWindow {
  private static final SolaLogger LOGGER = SolaLogger.of(EditorWindow.class, SolaEditorConstants.LOG_FILE);
  @Nullable
  private EditorConfig editorConfig;
  @Nullable
  private VBox toolContent;

  @Nullable
  private List<ToolPanel<?>> editorToolPanels;

  /**
   * Configures and shows the main editor window.
   */
  public void show() {
    SolaJavaFx.startOnApplicationThread(() -> {
      editorConfig = EditorConfig.readConfigFile();

      editorToolPanels = List.of(
        new AssetListToolPanel(editorConfig),
        new AudioToolPanel(editorConfig),
        new FontToolPanel(editorConfig),
        new SpriteSheetToolPanel(editorConfig)
      );

      Stage primaryStage = new Stage();

      initializeEditorConfigurationEvents(primaryStage);

      Scene scene = new EditorScene(mainPane());

      setApplicationIcon(primaryStage);
      primaryStage.setScene(scene);
      primaryStage.setTitle("sola editor");

      ToastService.initialize(primaryStage);
      DialogService.initialize(primaryStage);

      primaryStage.show();
    });
  }

  private Parent mainPane() {
    toolContent = new VBox();

    var parent = new HBox(toolbar(), toolContent);

    toolContent.prefWidthProperty().bind(parent.widthProperty());
    toolContent.prefHeightProperty().bind(parent.heightProperty());

    var selectedTool = editorToolPanels.stream()
      .filter(toolPanel -> toolPanel.getToolId().equals(editorConfig.selectedTool()))
      .findFirst()
      .orElse(editorToolPanels.get(0));

    toolContent.widthProperty().addListener((observable, oldValue, newValue) -> {
      ((Region) toolContent.getChildren().get(0)).setPrefWidth(newValue.doubleValue());
    });
    toolContent.heightProperty().addListener((observable, oldValue, newValue) -> {
      ((Region) toolContent.getChildren().get(0)).setPrefHeight(newValue.doubleValue());
    });

    switchTab(selectedTool);

    return parent;
  }

  private Node toolbar() {
    ToolBar toolBar = new ToolBar();

    toolBar.setOrientation(Orientation.VERTICAL);

    var toolbarItems = toolBar.getItems();

    editorToolPanels.forEach(toolPanel -> {
      var button = createEditorTabButton(toolPanel, toolbarItems);

      button.setDisable(
        toolPanel.getToolId().equals(editorConfig.selectedTool())
      );

      toolbarItems.add(button);
    });

    return toolBar;
  }

  private Button createEditorTabButton(ToolPanel<?> toolPanel, ObservableList<Node> toolbarItems) {
    Button button = new Button(toolPanel.getToolLabel());

    button.setOnAction((event) -> {
      toolbarItems.forEach(toolbarItem -> toolbarItem.setDisable(false));
      button.setDisable(true);
      switchTab(toolPanel);
    });

    return button;
  }

  private void switchTab(ToolPanel<?> toolPanel) {
    toolPanel.setPrefHeight(toolContent.getHeight());
    toolPanel.setPrefWidth(toolContent.getWidth());

    toolContent.getChildren().clear();
    toolContent.getChildren().add(toolPanel);
    toolPanel.onSwitch();
  }

  private void initializeEditorConfigurationEvents(Stage primaryStage) {
    primaryStage.setX(editorConfig.window().x());
    primaryStage.setY(editorConfig.window().y());
    primaryStage.setWidth(editorConfig.window().width());
    primaryStage.setHeight(editorConfig.window().height());

    primaryStage.setOnCloseRequest(event -> {
      // window size
      var windowBounds = new WindowBounds(
        (int) primaryStage.getX(), (int) primaryStage.getY(),
        (int) primaryStage.getWidth(), (int) primaryStage.getHeight()
      );

      // selected tool
      String selectedId = toolContent.getChildren().get(0).getId();

      // tool specific configurations
      Map<String, JsonObject> toolConfigs = new HashMap<>();

      editorToolPanels.forEach(toolPanel -> {
        toolConfigs.put(toolPanel.getToolId(), toolPanel.buildToolConfigForSaving());
      });

      // save it all
      EditorConfig.writeConfigFile(new EditorConfig(windowBounds, selectedId, toolConfigs));
    });
  }

  private void setApplicationIcon(Stage stage) {
    try {
      var url = JavaFxPathUtils.asUrl("assets/icon.jpg");

      if (url == null) {
        url = JavaFxPathUtils.asUrl("assets/icon.png");
      }

      if (url == null) {
        LOGGER.warning("Icon not found");
      } else {
        stage.getIcons().add(new Image(url.openStream()));
      }
    } catch (IOException ex) {
      LOGGER.error("Failed to load icon", ex);
    }
  }
}
