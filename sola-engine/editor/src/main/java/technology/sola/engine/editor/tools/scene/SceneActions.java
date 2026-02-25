package technology.sola.engine.editor.tools.scene;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.Component;
import technology.sola.ecs.World;
import technology.sola.engine.assets.scene.Scene;
import technology.sola.engine.assets.scene.SceneJsonMapper;
import technology.sola.engine.editor.SolaEditorCustomization;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.core.utils.ToastService;
import technology.sola.json.SolaJson;
import technology.sola.json.mapper.JsonMapper;
import technology.sola.logging.SolaLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@NullMarked
class SceneActions extends EditorPanel {
  private static final SolaLogger LOGGER = SolaLogger.of(SceneActions.class);
  private final SceneJsonMapper sceneJsonMapper;
  private final EntityTreeView entityTreeView;
  private final EntityComponentsPanel entityComponentsPanel;
  private final Button saveButton;

  @Nullable
  private String activeSceneFile;
  @Nullable
  private Scene activeScene;

  SceneActions(
    @Nullable String activeSceneFile,
    @Nullable String activeSelectedEntityUniqueId,
    SolaEditorCustomization customization,
    EntityTreeView entityTreeView,
    EntityComponentsPanel entityComponentsPanel
  ) {
    this.sceneJsonMapper = configureSceneJsonMapper(customization);
    this.entityTreeView = entityTreeView;
    this.entityComponentsPanel = entityComponentsPanel;
    this.activeSceneFile = activeSceneFile;
    this.activeScene = activeSceneFile == null ? null : loadInitialScene(activeSceneFile, activeSelectedEntityUniqueId);

    HBox hBox = new HBox();

    saveButton = buildSaveSceneButton();

    hBox.setSpacing(8);
    hBox.getChildren().addAll(saveButton, buildLoadSceneButton(), buildNewSceneButton());

    setSpacing(8);
    setVgrow(entityTreeView, Priority.ALWAYS);

    getChildren().addAll(hBox, entityTreeView);

    if (activeScene != null && activeSceneFile != null) {
      setScene(activeScene, activeSceneFile);
      saveButton.setDisable(false);
    }
  }

  @Nullable
  String getActiveSceneFile() {
    return activeSceneFile;
  }

  private Button buildSaveSceneButton() {
    Button saveButton = new Button("Save");

    if (activeScene == null) {
      saveButton.setDisable(true);
    }

    saveButton.setOnAction(e -> {

      try {
        Files.writeString(Path.of(activeSceneFile), sceneJsonMapper.toJson(activeScene).toString());
      } catch (IOException ex) {
        ToastService.error("Error saving scene.");
        LOGGER.error("Error saving scene.", ex);
      }
    });

    return saveButton;
  }

  private Button buildLoadSceneButton() {
    Button loadSceneButton = new Button("Load");

    loadSceneButton.setOnAction(e -> {
      FileChooser fileChooser = new FileChooser();

      fileChooser.setTitle("Load Scene");

      File sceneDirectory = new File("assets/scenes");

      if (!sceneDirectory.exists()) {
        if (!sceneDirectory.mkdirs()) {
          ToastService.error("Error creating assets/scenes directory");
        }
      }

      fileChooser.setInitialDirectory(sceneDirectory);
      fileChooser.setInitialFileName("main.scene.json");

      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Scene files (*.scene.json)", "*.scene.json");

      fileChooser.getExtensionFilters().add(extFilter);

      File loadSceneFile = fileChooser.showOpenDialog(getScene().getWindow());

      try {
        var jsonString = Files.readString(loadSceneFile.toPath());
        var scene = sceneJsonMapper.toObject(new SolaJson().parse(jsonString).asObject());

        setScene(scene, loadSceneFile.getAbsolutePath());
      } catch (IOException ex) {
        ToastService.error("Error loading scene.");
        LOGGER.error("Error loading scene.", ex);
      }
    });

    return loadSceneButton;
  }

  private Button buildNewSceneButton() {
    Button newSceneButton = new Button("New");

    newSceneButton.setOnAction(e -> {
      FileChooser fileChooser = new FileChooser();

      fileChooser.setTitle("Create New Scene");

      File sceneDirectory = new File("assets/scenes");

      if (!sceneDirectory.exists()) {
        if (!sceneDirectory.mkdirs()) {
          ToastService.error("Error creating assets/scenes directory");
        }
      }

      fileChooser.setInitialDirectory(sceneDirectory);
      fileChooser.setInitialFileName("main.scene.json");

      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Scene files (*.scene.json)", "*.scene.json");

      fileChooser.getExtensionFilters().add(extFilter);

      File newSceneFile = fileChooser.showSaveDialog(getScene().getWindow());

      try {
        var newScene = new Scene(new World(10));

        Files.writeString(newSceneFile.toPath(), sceneJsonMapper.toJson(newScene).toString());

        setScene(newScene, newSceneFile.getAbsolutePath());
      } catch (IOException ex) {
        ToastService.error("Error creating new scene.");
        LOGGER.error("Error creating new scene.", ex);
      }
    });

    return newSceneButton;
  }

  @Nullable
  private Scene loadInitialScene(String sceneFilePath, @Nullable String selectedEntityUniqueId) {
    try {
      var jsonString = Files.readString(Path.of(sceneFilePath));
      var scene = sceneJsonMapper.toObject(new SolaJson().parse(jsonString).asObject());

      if (selectedEntityUniqueId != null) {
        var entity = scene.world().findEntityByUniqueId(selectedEntityUniqueId);

        if (entity != null) {
          Platform.runLater(() -> {
            entityTreeView.selectEntity(entity);
          });
        }
      }

      return scene;
    } catch (IOException ex) {
      ToastService.error(ex.getMessage());
      LOGGER.error("Error loading initial scene.", ex);
      return null; // todo
    }
  }

  private void setScene(Scene scene, String activeSceneFile) {
    this.activeScene = scene;
    this.activeSceneFile = activeSceneFile;
    entityTreeView.populate(scene.world());
    entityComponentsPanel.updateWorld(scene.world());
    entityComponentsPanel.selectEntity(null);

    saveButton.setDisable(false);
  }

  private SceneJsonMapper configureSceneJsonMapper(SolaEditorCustomization customization) {
    List<JsonMapper<? extends Component>> componentJsonMappers = new ArrayList<>();

    customization.componentEditorModules()
      .forEach(module -> componentJsonMappers.add(module.getJsonMapper()));

    SceneJsonMapper sceneJsonMapper = new SceneJsonMapper();

    sceneJsonMapper.configure(componentJsonMappers);

    return sceneJsonMapper;
  }
}
