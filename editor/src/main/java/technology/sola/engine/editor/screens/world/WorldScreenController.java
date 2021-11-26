package technology.sola.engine.editor.screens.world;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import technology.sola.engine.ecs.Entity;
import technology.sola.engine.ecs.World;
import technology.sola.engine.ecs.io.Base64WorldSerializer;
import technology.sola.engine.editor.components.EntityListView;
import technology.sola.engine.editor.core.FolderUtils;
import technology.sola.engine.editor.core.SolaEditorContext;
import technology.sola.engine.editor.screens.SolaEditorScreen;

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
  private EntityListView entityListView;

  public WorldScreenController(Stage owner, SolaEditorContext solaEditorContext) {
    this.owner = owner;
    this.solaEditorContext = solaEditorContext;
  }

  @Override
  public String getFxmlResource() {
    return "WorldScreen.fxml";
  }

  @Override
  public void initialize() {
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
        try {
          loadWorld();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });

    menuItemSave.setOnAction(event -> {
      try {
        saveWorld();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    menuItemNewEntity.setOnAction(event -> {
      Entity entity = worldProperty.getValue()
        .createEntity()
        .setName("New Entity");

      entityList.add(entity);
    });

    entityListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
      if (newValue != null) {
        System.out.println("selected " + newValue.getName());
      }
    }));
  }

  private void saveWorld() throws IOException {
    String serializedWorld = new Base64WorldSerializer().stringify(worldProperty.getValue());

    Files.write(worldFileProperty.getValue().toPath(), serializedWorld.getBytes(StandardCharsets.UTF_8));
  }

  private void loadWorld() throws IOException {
    String serializedWorld = Files.readString(worldFileProperty.getValue().toPath());

    worldProperty.setValue(new Base64WorldSerializer().parse(serializedWorld));
    entityList.clear();
    entityList.addAll(worldProperty.getValue().getEntitiesWithComponents());
  }
}
