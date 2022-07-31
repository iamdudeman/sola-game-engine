package technology.sola.engine.editor.ui.screens.assets;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import technology.sola.engine.editor.core.FolderUtils;
import technology.sola.engine.editor.core.SolaEditorContext;
import technology.sola.engine.editor.ui.control.FileTreeView;
import technology.sola.engine.editor.ui.dialog.NewFontDialog;
import technology.sola.engine.editor.ui.dialog.NewMaterialDialog;
import technology.sola.engine.editor.ui.dialog.NewSpriteSheetDialog;
import technology.sola.engine.editor.ui.screens.SolaEditorScreen;

import java.io.File;

public class AssetsScreenController implements SolaEditorScreen {
  @FXML
  private MenuItem menuItemCreateFont;
  @FXML
  private MenuItem menuItemCreateSpriteSheet;
  @FXML
  private MenuItem menuItemCreateMaterial;

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

    container.getChildren().add(new FileTreeView(assetsFolder));

    menuItemCreateFont.setOnAction(event -> {
      new NewFontDialog(owner, solaEditorContext).showAndWait();
    });
    menuItemCreateSpriteSheet.setOnAction(event -> {
      new NewSpriteSheetDialog(owner, solaEditorContext).showAndWait();
    });
    menuItemCreateMaterial.setOnAction(event -> {
      new NewMaterialDialog(owner, solaEditorContext).showAndWait();
    });
  }
}
