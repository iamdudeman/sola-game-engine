package technology.sola.engine.editor.ui.screens;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.editor.core.FolderUtils;
import technology.sola.engine.editor.core.SolaEditorContext;
import technology.sola.engine.editor.ui.dialog.ProjectSettingsDialog;
import technology.sola.engine.editor.ui.screens.assets.AssetsScreenController;
import technology.sola.engine.editor.ui.screens.world.WorldScreenController;

import java.io.File;
import java.io.IOException;

public class LandingScreenController implements SolaEditorScreen {
  @FXML
  private Menu menuWindows;
  @FXML
  private MenuItem menuItemNew;
  @FXML
  private MenuItem menuItemOpen;

  @FXML
  private Text textCurrentProject;
  @FXML
  private Button buttonEditSettings;

  @FXML
  private CheckMenuItem checkMenuItemAssets;
  @FXML
  private CheckMenuItem checkMenuItemWorld;

  private final SolaEditorContext solaEditorContext;
  private Stage assetsStage = null;
  private Stage worldStage = null;

  public LandingScreenController(SolaEditorContext solaEditorContext) {
    this.solaEditorContext = solaEditorContext;
  }

  @Override
  public String getFxmlResource() {
    return "LandingScreen.fxml";
  }

  @Override
  public void initialize() {
    FileChooser.ExtensionFilter solaExtensionFilter = new FileChooser.ExtensionFilter("Sola files", "*.sola");
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("New Project");
    fileChooser.setInitialDirectory(new File("./"));
    fileChooser.getExtensionFilters().add(solaExtensionFilter);
    fileChooser.setSelectedExtensionFilter(solaExtensionFilter);

    solaEditorContext.projectFilePropertyProperty().addListener((observable, oldValue, newValue) -> {
      menuWindows.setDisable(newValue == null);
      buttonEditSettings.setDisable(newValue == null);
      textCurrentProject.setText(newValue == null ? "" : newValue.getName());
    });

    menuItemNew.setOnAction(event -> {
      fileChooser.setInitialFileName("new_project.sola");

      File projectFile = fileChooser.showSaveDialog(solaEditorContext.getPrimaryStage());

      try {
        if (projectFile != null) {
          File projectFolder = new File(projectFile.getParentFile(), projectFile.getName().replace(".sola", ""));

          projectFolder.mkdir();

          projectFile = new File(projectFolder, projectFile.getName());

          if (projectFile.createNewFile()) {
            solaEditorContext.projectFilePropertyProperty().setValue(projectFile);

            new FolderUtils(solaEditorContext)
              .createDirectories("assets/fonts", "assets/materials", "assets/sprites", "assets/worlds");
          }
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    });

    menuItemOpen.setOnAction(event -> {
      fileChooser.setInitialFileName(null);

      File projectFile = fileChooser.showOpenDialog(solaEditorContext.getPrimaryStage());

      if (projectFile != null) {
        solaEditorContext.projectFilePropertyProperty().setValue(projectFile);
      }
    });

    checkMenuItemAssets.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        if (assetsStage == null) {
          assetsStage = new Stage();
          assetsStage.setTitle("Assets");
          assetsStage.setScene(SolaEditorScreen.loadSceneForController(new AssetsScreenController(assetsStage, solaEditorContext)));
          assetsStage.setOnCloseRequest(event -> {
            checkMenuItemAssets.setSelected(false);
          });
        }

        assetsStage.setX(solaEditorContext.getPrimaryStage().getX());
        assetsStage.setY(solaEditorContext.getPrimaryStage().getY() + solaEditorContext.getPrimaryStage().getHeight() + 5);
        assetsStage.show();
      } else {
        assetsStage.hide();
      }
    });

    checkMenuItemWorld.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        if (worldStage == null) {
          worldStage = new Stage();
          worldStage.setTitle("World");
          worldStage.setScene(SolaEditorScreen.loadSceneForController(new WorldScreenController(worldStage, solaEditorContext)));
          worldStage.setOnCloseRequest(event -> {
            checkMenuItemWorld.setSelected(false);
          });
        }

        worldStage.setX(solaEditorContext.getPrimaryStage().getX() + solaEditorContext.getPrimaryStage().getWidth() + 5);
        worldStage.setY(solaEditorContext.getPrimaryStage().getY());
        worldStage.show();
      } else {
        worldStage.hide();
      }
    });

    buttonEditSettings.setOnAction(event -> {
      new ProjectSettingsDialog(solaEditorContext.getPrimaryStage(), solaEditorContext).showAndWait();
    });
  }
}
