package technology.sola.engine.editor.font;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import technology.sola.engine.assets.graphics.font.FontStyle;
import technology.sola.engine.editor.core.EditorScene;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.core.components.control.IntegerSpinner;
import technology.sola.engine.editor.core.notifications.CustomDialog;
import technology.sola.engine.tooling.font.FontListTool;
import technology.sola.engine.tooling.font.FontRasterizerTool;

import java.io.File;
import java.util.Arrays;

public class NewFontDialog extends CustomDialog {
  public NewFontDialog(File parentFolder) {
    super();

    setTitle("New Font");

    VBox vBox = new VBox();
    EditorPanel root = new EditorPanel(vBox);

    vBox.setSpacing(8);

    var fonts = new FontListTool().execute().split("\n");

    ComboBox<String> fontChoice = new ComboBox<>(FXCollections.observableArrayList(
      fonts
    ));

    ComboBox<String> styleChoice = new ComboBox<>(FXCollections.observableArrayList(
      Arrays.stream(FontStyle.values()).map(FontStyle::toString).toArray(String[]::new)
    ));

    var size = new IntegerSpinner(12, 120);

    TextField characters = new TextField();

    characters.setText(FontRasterizerTool.DEFAULT_CHARACTERS);

    vBox.getChildren().addAll(
      fontChoice,
      styleChoice,
      size,
      characters,
      new Button("Cancel"),
      new Button("Create")
    );

    setScene(new EditorScene(root));
  }
}
