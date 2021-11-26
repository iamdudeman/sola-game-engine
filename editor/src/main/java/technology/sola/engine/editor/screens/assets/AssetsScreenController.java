package technology.sola.engine.editor.screens.assets;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import technology.sola.engine.editor.FolderUtils;
import technology.sola.engine.editor.JavaFxController;
import technology.sola.engine.editor.SolaEditorContext;
import technology.sola.engine.editor.components.FileTreeItem;
import technology.sola.engine.editor.components.dialog.NewFontDialog;

import java.io.File;

public class AssetsScreenController implements JavaFxController {
  @FXML
  private MenuItem menuItemCreateFont;

  @FXML
  private VBox container;

  private final Stage owner;
  private final SolaEditorContext solaEditorContext;

  public AssetsScreenController(Stage owner, SolaEditorContext solaEditorContext) {
    this.owner = owner;
    this.solaEditorContext = solaEditorContext;
  }

  @Override
  public String getFxmlResource() {
    return "AssetsScreen.fxml";
  }

  @Override
  public void initialize() {
    FolderUtils folderUtils = new FolderUtils(solaEditorContext);
    File assetsFolder = folderUtils.getOrCreateFolder("assets");
    folderUtils.getOrCreateFolder("assets/fonts");

    TreeView<File> fileView = new TreeView<>(new FileTreeItem(assetsFolder));

    container.getChildren().add(fileView);

    menuItemCreateFont.setOnAction(event -> {
      new NewFontDialog(owner, solaEditorContext).showAndWait();
    });
  }
}
