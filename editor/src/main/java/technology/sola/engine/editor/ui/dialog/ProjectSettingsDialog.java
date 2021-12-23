package technology.sola.engine.editor.ui.dialog;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Window;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.editor.core.SolaEditorContext;

import java.io.IOException;

public class ProjectSettingsDialog extends Dialog<SolaConfiguration> {
  @FXML
  private TextField textFieldTitle;
  @FXML
  private TextField textFieldCanvasWidth;
  @FXML
  private TextField textFieldCanvasHeight;
  @FXML
  private Spinner<Integer> spinnerGameLoopFps;
  @FXML
  private CheckBox checkBoxGameLoopResting;

  public ProjectSettingsDialog(Window owner, SolaEditorContext solaEditorContext) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("ProjectSettingsDialog.fxml"));

    loader.setController(this);

    DialogPane dialogPane = null;
    try {
      dialogPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    initOwner(owner);
    initModality(Modality.APPLICATION_MODAL);


    SolaConfiguration solaConfiguration = solaEditorContext.solaConfigurationProperty().getValue();
    textFieldTitle.setText(solaConfiguration.getSolaTitle());
    textFieldCanvasWidth.setText("" + solaConfiguration.getCanvasWidth());
    textFieldCanvasHeight.setText("" + solaConfiguration.getCanvasHeight());
    spinnerGameLoopFps.getValueFactory().setValue(solaConfiguration.getGameLoopTargetUpdatesPerSecond());
    checkBoxGameLoopResting.setSelected(solaConfiguration.isGameLoopRestingAllowed());

    setResizable(false);
    setDialogPane(dialogPane);
    setResultConverter(buttonType -> {
      if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
        return new SolaConfiguration(
          textFieldTitle.getText(),
          Integer.parseInt(textFieldCanvasWidth.getText()),
          Integer.parseInt(textFieldCanvasHeight.getText()),
          spinnerGameLoopFps.getValue(),
          checkBoxGameLoopResting.isSelected()
        );
      }

      return null;
    });
  }
}
