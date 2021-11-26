package technology.sola.engine.editor.screens.world;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import technology.sola.engine.ecs.World;
import technology.sola.engine.ecs.io.Base64WorldSerializer;
import technology.sola.engine.editor.core.FolderUtils;
import technology.sola.engine.editor.core.SolaEditorContext;
import technology.sola.engine.editor.screens.SolaEditorScreen;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class WorldScreenController implements SolaEditorScreen {
  private final Stage owner;
  private final SolaEditorContext solaEditorContext;

  @FXML
  private MenuItem menuItemNew;

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


    menuItemNew.setOnAction(event -> {
      fileChooser.setInitialFileName("new_world.world");

      File worldFile = fileChooser.showSaveDialog(owner);

      try {
        if (worldFile != null) {
          worldFile.createNewFile();

          String stuff = new Base64WorldSerializer().stringify(new World(10));

          Files.write(worldFile.toPath(), stuff.getBytes(StandardCharsets.UTF_8));
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

  }
}
