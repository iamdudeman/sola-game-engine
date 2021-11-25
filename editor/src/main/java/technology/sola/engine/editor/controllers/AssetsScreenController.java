package technology.sola.engine.editor.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import technology.sola.engine.editor.JavaFxController;
import technology.sola.engine.editor.SolaEditorContext;
import technology.sola.engine.editor.components.FileTreeItem;
import technology.sola.engine.editor.dialog.NewFontDialog;

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
    TreeView<File> fileView = new TreeView<>(
      new FileTreeItem(new File(solaEditorContext.projectFilePropertyProperty().getValue().getParentFile(), "assets"))
    );

    container.getChildren().add(fileView);

    menuItemCreateFont.setOnAction(event -> {
      new NewFontDialog(owner, solaEditorContext).showAndWait();
    });
  }
}
