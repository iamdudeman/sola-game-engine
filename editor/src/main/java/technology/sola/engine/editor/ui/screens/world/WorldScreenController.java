package technology.sola.engine.editor.ui.screens.world;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.Entity;
import technology.sola.engine.ecs.World;
import technology.sola.engine.ecs.io.Base64WorldSerializer;
import technology.sola.engine.editor.ui.ecs.EntityComponents;
import technology.sola.engine.editor.ui.control.EntityListView;
import technology.sola.engine.editor.core.EditorSola;
import technology.sola.engine.editor.core.FolderUtils;
import technology.sola.engine.editor.core.SolaEditorContext;
import technology.sola.engine.editor.ui.screens.SolaEditorScreen;
import technology.sola.engine.graphics.components.CameraComponent;
import technology.sola.engine.platform.javafx.JavaFxSolaPlatform;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public class WorldScreenController implements SolaEditorScreen {
  private final Stage owner;
  private final SolaEditorContext solaEditorContext;
  private final Property<World> worldProperty = new SimpleObjectProperty<>(null);
  private final Property<File> worldFileProperty = new SimpleObjectProperty<>(null);
  private final ObservableList<Entity> entityList = FXCollections.observableList(new ArrayList<>());

  @FXML
  private VBox container;
  @FXML
  private MenuItem menuItemNew;
  @FXML
  private MenuItem menuItemOpen;
  @FXML
  private MenuItem menuItemSave;
  @FXML
  private Menu menuWorld;
  @FXML
  private MenuItem menuItemNewEntity;
  @FXML
  private CheckMenuItem menuItemLivePreview;
  @FXML
  private EntityListView entityListView;
  @FXML
  private EntityComponents entityComponents;

  private EditorSola editorSola;
  private final SolaPlatform solaPlatform;

  public WorldScreenController(Stage owner, SolaEditorContext solaEditorContext) {
    this.owner = owner;
    this.solaEditorContext = solaEditorContext;

    solaPlatform = new JavaFxSolaPlatform(false);
  }

  @Override
  public String getFxmlResource() {
    return "WorldScreen.fxml";
  }

  @Override
  public void initialize() {
    entityComponents.setComponentsMenu(solaEditorContext.getConfiguration().getEntityComponentMenus());

    File worldsFolder = new FolderUtils(solaEditorContext).getOrCreateFolder("assets/worlds");
    FileChooser.ExtensionFilter solaExtensionFilter = new FileChooser.ExtensionFilter("World file", "*.world");
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("New World");
    fileChooser.setInitialDirectory(worldsFolder);
    fileChooser.getExtensionFilters().add(solaExtensionFilter);
    fileChooser.setSelectedExtensionFilter(solaExtensionFilter);

    entityListView.setItems(entityList);

    worldProperty.addListener(((observable, oldValue, newValue) -> {
      menuWorld.setDisable(newValue == null);
      menuItemSave.setDisable(newValue == null);

      if (newValue != null) {
        var editorCamera = newValue.getEntityByName("editorCamera");

        if (editorCamera == null) {
          CameraComponent editorCameraComponent = new CameraComponent();

          editorCameraComponent.setPriority(Integer.MIN_VALUE);

          newValue.createEntity()
            .setName("editorCamera")
            .addComponent(new TransformComponent(-50, -50))
            .addComponent(editorCameraComponent);
        }
      }
    }));

    menuItemNew.setOnAction(event -> {
      fileChooser.setInitialFileName("new_world.world");

      File worldFile = fileChooser.showSaveDialog(owner);

      worldFileProperty.setValue(worldFile);

      try {
        if (worldFile != null) {
          worldFile.createNewFile();

          worldProperty.setValue(new World(100));

          saveWorld();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    menuItemOpen.setOnAction(event -> {
      fileChooser.setInitialFileName(null);

      File worldFile = fileChooser.showOpenDialog(owner);

      if (worldFile != null) {
        worldFileProperty.setValue(worldFile);
        loadWorld();
      }
    });

    menuItemSave.setOnAction(event -> {
      saveWorld();
    });

    menuItemNewEntity.setOnAction(event -> {
      Entity entity = worldProperty.getValue()
        .createEntity()
        .addComponent(new TransformComponent())
        .setName("New Entity");

      entityList.add(entity);
    });

    menuItemLivePreview.selectedProperty().addListener(((observable, oldValue, newValue) -> {
      if (newValue) {
        menuItemNewEntity.setDisable(true);
        entityListView.setDisable(true);
        editorSola.setWorld(copyWorld());
        editorSola.startPreview();
      } else {
        editorSola.stopPreview();
        editorSola.setWorld(worldProperty.getValue());
        entityListView.setDisable(false);
        menuItemNewEntity.setDisable(false);
      }
    }));

    entityListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
      entityComponents.setEntity(newValue);
    }));

    editorSola = new EditorSola(solaEditorContext.solaConfigurationProperty().getValue(), entityListView.getSelectionModel());
    updateEditorSola();

    solaEditorContext.solaEditorConfigurationDirtyProperty().addListener(((observable, oldValue, newValue) -> {
      if (newValue) {
        updateEditorSola();
      }
    }));
  }

  private synchronized void updateEditorSola() {
    editorSola.stop();
    editorSola.setSolaConfiguration(solaEditorContext.solaConfigurationProperty().getValue());
    editorSola.setLayers(solaEditorContext.solaLayersProperty().getValue());
    solaPlatform.play(editorSola);
  }

  private World copyWorld() {
    World world = new World(worldProperty.getValue().getMaxEntityCount());

    worldProperty.getValue().getEntitiesWithComponents()
      .forEach(entity -> {
        if ("editorCamera".equals(entity.getName())) return;

        Entity copyEntity = world.createEntity(entity.getUniqueId());
        copyEntity.setName(entity.getName());

        entity.getCurrentComponents().forEach(componentClass -> {
          copyEntity.addComponent(entity.getComponent(componentClass).copy());
        });
      });

    return world;
  }

  private void saveWorld() {
    String serializedWorld = new Base64WorldSerializer().stringify(worldProperty.getValue());

    try {
      Files.write(worldFileProperty.getValue().toPath(), serializedWorld.getBytes(StandardCharsets.UTF_8));
    } catch (IOException ex) {
      ex.printStackTrace(); // todo handle this
    }
  }

  private void loadWorld() {
    try {
      String serializedWorld = Files.readString(worldFileProperty.getValue().toPath());

      worldProperty.setValue(new Base64WorldSerializer().parse(serializedWorld));
      entityList.clear();
      entityList.addAll(worldProperty.getValue().getEntitiesWithComponents());

      editorSola.setWorld(worldProperty.getValue());
    } catch (IOException ex) {
      ex.printStackTrace(); // todo handle this
    }
  }
}
