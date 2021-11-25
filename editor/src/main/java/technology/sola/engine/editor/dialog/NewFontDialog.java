package technology.sola.engine.editor.dialog;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

public class NewFontDialog extends Dialog<File> {
  public NewFontDialog(Window owner) {
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
  }

  public void initialize() {

  }
}
