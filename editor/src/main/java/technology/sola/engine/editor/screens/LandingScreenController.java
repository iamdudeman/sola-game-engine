package technology.sola.engine.editor.screens;

import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import technology.sola.engine.editor.core.FolderUtils;
import technology.sola.engine.editor.core.SolaEditorContext;
import technology.sola.engine.editor.screens.assets.AssetsScreenController;

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
  private CheckMenuItem checkMenuItemAssets;

  private final SolaEditorContext solaEditorContext;
  private Stage assetsStage = null;

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
      textCurrentProject.setText(newValue.getName());
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
              .createDirectories("assets/fonts", "assets/sprites", "assets/worlds");
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
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

        assetsStage.setX(solaEditorContext.getPrimaryStage().getX() + solaEditorContext.getPrimaryStage().getWidth() + 5);
        assetsStage.setY(solaEditorContext.getPrimaryStage().getY());
        assetsStage.show();
      } else {
        assetsStage.hide();
      }
    });
  }
}
