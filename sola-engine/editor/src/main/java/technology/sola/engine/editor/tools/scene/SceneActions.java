package technology.sola.engine.editor.tools.scene;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import technology.sola.ecs.World;
import technology.sola.engine.assets.scene.Scene;
import technology.sola.engine.assets.scene.SceneAssetLoader;
import technology.sola.engine.assets.scene.SceneJsonMapper;
import technology.sola.engine.editor.SolaEditorCustomization;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.core.utils.ToastService;
import technology.sola.engine.platform.javafx.assets.JavaFxJsonAssetLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class SceneActions extends EditorPanel {
  private final SceneAssetLoader sceneAssetLoader;
  private final SceneJsonMapper sceneJsonMapper;
  private final EntityTreeView entityTreeView;
  private final EntityComponentsPanel entityComponentsPanel;

  SceneActions(SolaEditorCustomization customization, EntityTreeView entityTreeView, EntityComponentsPanel entityComponentsPanel) {
    this.entityTreeView = entityTreeView;
    this.entityComponentsPanel = entityComponentsPanel;

    sceneAssetLoader = new SceneAssetLoader(new JavaFxJsonAssetLoader());
    sceneAssetLoader.configure(customization.componentJsonMappers());
    sceneJsonMapper = new SceneJsonMapper();
    sceneJsonMapper.configure(customization.componentJsonMappers());

    HBox hBox = new HBox();

    hBox.setSpacing(8);
    hBox.getChildren().addAll(buildSaveSceneButton(), buildLoadSceneButton(), buildNewSceneButton());

    setSpacing(8);
    setVgrow(entityTreeView, Priority.ALWAYS);

    getChildren().addAll(hBox, entityTreeView);
  }

  private Button buildSaveSceneButton() {
    Button saveButton = new Button("Save");

    return saveButton;
  }

  private Button buildLoadSceneButton() {
    Button loadSceneButton = new Button("Load");

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

        setScene(newScene);
      } catch (IOException ex) {
        ToastService.error(ex.getMessage());
      }
    });

    return newSceneButton;
  }

  private void setScene(Scene scene) {
    entityTreeView.populate(scene.world());
    entityComponentsPanel.selectEntity(null);
  }
}
