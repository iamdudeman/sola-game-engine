package technology.sola.engine.editor.ui.ecs.rendering;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import technology.sola.engine.editor.core.FolderUtils;
import technology.sola.engine.editor.core.SolaEditorContext;
import technology.sola.engine.editor.ui.ecs.ComponentController;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.json.JsonArray;
import technology.sola.json.SolaJson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SpriteComponentController extends ComponentController<SpriteComponent> {
  @FXML
  private ComboBox<String> comboBoxSpriteSheet;
  @FXML
  private ComboBox<String> comboBoxSpriteId;
  @FXML
  private Label labelSpriteId;

  public SpriteComponentController(SolaEditorContext solaEditorContext) {
    super(solaEditorContext);
  }

  @Override
  public void initialize() {
    FolderUtils folderUtils = new FolderUtils(solaEditorContext);

    comboBoxSpriteSheet.valueProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue == null) return;

      labelSpriteId.setDisable(false);

//      entity.addComponent(createComponentFromFields());
      File file = new File(folderUtils.getOrCreateFolder("assets/sprites/"), newValue);
      try {
        String jsonFile = Files.readString(file.toPath());
        JsonArray sprites = new SolaJson().parse(jsonFile).asObject().getArray("sprites");

        comboBoxSpriteId.getItems().clear();
        sprites.forEach(spriteObject -> {
          comboBoxSpriteId.getItems().add(spriteObject.asObject().getString("id"));
        });
        comboBoxSpriteId.getSelectionModel().select(0);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    });
    comboBoxSpriteId.valueProperty().addListener(observable -> entity.addComponent(createComponentFromFields()));

    solaEditorContext.projectFilePropertyProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        labelSpriteId.setDisable(true);
        comboBoxSpriteSheet.getItems().clear();

        File fontFolder = folderUtils.getOrCreateFolder("assets/sprites");
        File[] fontFiles = fontFolder.listFiles();

        if (fontFiles != null) {
          for (File fontFile : fontFiles) {
            if (fontFile.getName().endsWith(".json")) {
              comboBoxSpriteSheet.getItems().add(fontFile.getName());
            }
          }
        }
      }
    });
  }

  @Override
  public SpriteComponent createDefault() {
    return new SpriteComponent(null, null);
  }

  @Override
  public Class<SpriteComponent> getComponentClass() {
    return SpriteComponent.class;
  }

  @Override
  protected String getFxmlResource() {
    return "SpriteComponent.fxml";
  }

  @Override
  protected void updateFieldValuesFromEntity() {
    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);

    if (spriteComponent != null) {
      comboBoxSpriteSheet.setValue(spriteComponent.getSpriteSheetId());
      comboBoxSpriteId.setValue(spriteComponent.getSpriteId());
    }
  }

  private SpriteComponent createComponentFromFields() {
    return new SpriteComponent(
      comboBoxSpriteSheet.getValue(),
      comboBoxSpriteId.getValue()
    );
  }
}
