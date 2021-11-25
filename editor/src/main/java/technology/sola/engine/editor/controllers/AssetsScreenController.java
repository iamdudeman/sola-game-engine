package technology.sola.engine.editor.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import technology.sola.engine.editor.JavaFxController;
import technology.sola.engine.editor.SolaEditorContext;
import technology.sola.engine.editor.dialog.NewFontDialog;

public class AssetsScreenController implements JavaFxController {
  @FXML
  private MenuItem menuItemCreateFont;

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
    menuItemCreateFont.setOnAction(event -> {
      new NewFontDialog(owner, solaEditorContext).showAndWait();
    });
  }
}
