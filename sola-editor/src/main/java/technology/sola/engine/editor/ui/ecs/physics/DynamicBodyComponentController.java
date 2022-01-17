package technology.sola.engine.editor.ui.ecs.physics;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import technology.sola.engine.editor.core.FolderUtils;
import technology.sola.engine.editor.core.SolaEditorContext;
import technology.sola.engine.editor.ui.ecs.ComponentController;
import technology.sola.engine.physics.Material;
import technology.sola.engine.physics.component.DynamicBodyComponent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

public class DynamicBodyComponentController extends ComponentController<DynamicBodyComponent> {
  @FXML
  private ComboBox<String> comboBoxMaterial;
  @FXML
  private CheckBox checkBoxKinematic;

  private final Map<String, Material> materialMap = new LinkedHashMap<>();

  public DynamicBodyComponentController(SolaEditorContext solaEditorContext) {
    super(solaEditorContext);
  }

  @Override
  public void initialize() {
    solaEditorContext.projectFilePropertyProperty().addListener(((observable, oldValue, newValue) -> {
      if (newValue != null) {
        materialMap.clear();
        materialMap.put("Default", Material.UNIT_MASS_MATERIAL);

        File materialsFolder = new FolderUtils(solaEditorContext).getOrCreateFolder("assets/materials");

        if (materialsFolder != null) {
          File[] materialFiles = materialsFolder.listFiles();
          if (materialFiles != null) {
            for (File materialFile : materialFiles) {
              try {
                Material material = parseMaterial(Files.readString(materialFile.toPath()));

                materialMap.put(materialFile.getName(), material);
              } catch (IOException ex) {
                // todo handle this
                ex.printStackTrace();
              }
            }
          }
        }

        comboBoxMaterial.setItems(FXCollections.observableList(new ArrayList<>(materialMap.keySet())));
      }
    }));

    comboBoxMaterial.valueProperty().addListener(((observable, oldValue, newValue) -> {
      entity.addComponent(createComponentFromFields());
    }));

    checkBoxKinematic.selectedProperty().addListener(((observable, oldValue, newValue) -> {
      entity.addComponent(createComponentFromFields());
    }));
  }

  @Override
  public DynamicBodyComponent createDefault() {
    return new DynamicBodyComponent();
  }

  @Override
  public Class<DynamicBodyComponent> getComponentClass() {
    return DynamicBodyComponent.class;
  }

  @Override
  protected String getFxmlResource() {
    return "DynamicBodyComponent.fxml";
  }

  @Override
  protected void updateFieldValuesFromEntity() {
    DynamicBodyComponent dynamicBodyComponent = entity.getComponent(DynamicBodyComponent.class);

    if (dynamicBodyComponent != null) {
      materialMap.entrySet().stream()
        .filter(entry -> entry.getValue().equals(dynamicBodyComponent.getMaterial()))
        .findFirst()
        .ifPresentOrElse(
          entry -> comboBoxMaterial.setValue(entry.getKey()),
          () -> comboBoxMaterial.setValue("Default")
        );
      checkBoxKinematic.setSelected(dynamicBodyComponent.isKinematic());
    }
  }

  private DynamicBodyComponent createComponentFromFields() {
    return new DynamicBodyComponent(materialMap.get(comboBoxMaterial.getValue()), checkBoxKinematic.isSelected());
  }

  private Material parseMaterial(String materialString) {
    final byte[] data = Base64.getDecoder().decode(materialString);

    try (final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
      return (Material) ois.readObject();
    } catch (IOException | ClassNotFoundException ex) {
      // TODO handle this better
      throw new RuntimeException(ex);
    }
  }
}
