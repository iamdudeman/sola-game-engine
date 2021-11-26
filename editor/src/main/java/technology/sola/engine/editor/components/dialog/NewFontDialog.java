package technology.sola.engine.editor.components.dialog;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Spinner;
import javafx.stage.Modality;
import javafx.stage.Window;
import technology.sola.engine.editor.FolderUtils;
import technology.sola.engine.editor.SolaEditorContext;
import technology.sola.engine.tools.font.FontRasterizer;

import java.io.File;
import java.io.IOException;

public class NewFontDialog extends Dialog<File> {
  @FXML
  private ComboBox<String> comboBoxFontName;

  @FXML
  private Spinner<Integer> spinnerFontSize;

  @FXML
  private ComboBox<String> comboBoxFontStyle;

  public NewFontDialog(Window owner, SolaEditorContext solaEditorContext) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("NewFontDialog.fxml"));

    loader.setController(this);

    DialogPane dialogPane = null;
    try {
      dialogPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    initOwner(owner);
    initModality(Modality.APPLICATION_MODAL);

    setResizable(false);
    setTitle("New Font");
    setDialogPane(dialogPane);
    setResultConverter(buttonType -> {
      if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
        File fontFolder = new FolderUtils(solaEditorContext).getOrCreateFolder("assets/fonts");

        new FontRasterizer(fontFolder)
          .rasterizeFont(comboBoxFontName.getValue(), comboBoxFontStyle.getValue(), spinnerFontSize.getValue());
      }

      return null;
    });
  }
}
