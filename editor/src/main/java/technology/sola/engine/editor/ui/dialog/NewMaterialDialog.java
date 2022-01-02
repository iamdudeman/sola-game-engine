package technology.sola.engine.editor.ui.dialog;

import com.sun.javafx.scene.control.DoubleField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Window;
import technology.sola.engine.ecs.World;
import technology.sola.engine.editor.core.FolderUtils;
import technology.sola.engine.editor.core.SolaEditorContext;
import technology.sola.engine.physics.Material;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;

public class NewMaterialDialog extends Dialog<File> {
  @FXML
  private TextField textFieldName;
  @FXML
  private DoubleField doubleFieldMass;
  @FXML
  private DoubleField doubleFieldRestitution;
  @FXML
  private DoubleField doubleFieldFriction;

  public NewMaterialDialog(Window owner, SolaEditorContext solaEditorContext) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("MaterialDialog.fxml"));

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
    setTitle("New Material");
    setDialogPane(dialogPane);
    setResultConverter(buttonType -> {
      if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
        Material material = new Material(
          (float)doubleFieldMass.getValue(), (float)doubleFieldRestitution.getValue(), (float)doubleFieldFriction.getValue()
        );
        String filename = textFieldName.getText() + ".material";
        File materialsFolder = new FolderUtils(solaEditorContext).getOrCreateFolder("assets/materials");

        try {
          Files.write(new File(materialsFolder, filename).toPath(), stringifyMaterial(material).getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
          // todo handle this
          ex.printStackTrace();
        }
      }

      return null;
    });
  }

  public String stringifyMaterial(Material material) {
    try (
      final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
    ) {
      objectOutputStream.writeObject(material);

      return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    } catch (IOException ex) {
      // TODO handle this better
      throw new RuntimeException(ex);
    }
  }

  public Material parseMaterial(String materialString) {
    final byte[] data = Base64.getDecoder().decode(materialString);

    try (final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
      return (Material) ois.readObject();
    } catch (IOException | ClassNotFoundException ex) {
      // TODO handle this better
      throw new RuntimeException(ex);
    }
  }
}
