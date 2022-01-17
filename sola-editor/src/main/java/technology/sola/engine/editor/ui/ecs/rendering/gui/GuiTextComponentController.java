package technology.sola.engine.editor.ui.ecs.rendering.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import technology.sola.engine.editor.core.FolderUtils;
import technology.sola.engine.editor.core.SolaEditorContext;
import technology.sola.engine.editor.ui.ecs.ComponentController;
import technology.sola.engine.editor.ui.ecs.rendering.ColorUtils;
import technology.sola.engine.graphics.gui.components.GuiTextComponent;

import java.io.File;

public class GuiTextComponentController extends ComponentController<GuiTextComponent> {
  @FXML
  private ComboBox<String> comboBoxFont;
  @FXML
  private TextField textFieldText;
  @FXML
  private ColorPicker colorPickerColor;

  public GuiTextComponentController(SolaEditorContext solaEditorContext) {
    super(solaEditorContext);
  }

  @Override
  public void initialize() {
    comboBoxFont.valueProperty().addListener(observable -> entity.addComponent(createComponentFromFields()));
    textFieldText.textProperty().addListener(observable -> entity.addComponent(createComponentFromFields()));
    colorPickerColor.valueProperty().addListener(observable -> entity.addComponent(createComponentFromFields()));

    solaEditorContext.projectFilePropertyProperty().addListener(((observable, oldValue, newValue) -> {
      if (newValue != null) {
        comboBoxFont.getItems().clear();

        FolderUtils folderUtils = new FolderUtils(solaEditorContext);
        File fontFolder = folderUtils.getOrCreateFolder("assets/fonts");
        File[] fontFiles = fontFolder.listFiles();

        if (fontFiles != null) {
          for (File fontFile : fontFiles) {
            if (fontFile.getName().endsWith(".json")) {
              comboBoxFont.getItems().add(fontFile.getName());
            }
          }
        }
      }
    }));
  }

  @Override
  public GuiTextComponent createDefault() {
    return new GuiTextComponent();
  }

  @Override
  public Class<GuiTextComponent> getComponentClass() {
    return GuiTextComponent.class;
  }

  @Override
  protected String getFxmlResource() {
    return "GuiTextComponent.fxml";
  }

  @Override
  protected void updateFieldValuesFromEntity() {
    GuiTextComponent guiTextComponent = entity.getComponent(GuiTextComponent.class);

    if (guiTextComponent != null) {
      comboBoxFont.setValue(guiTextComponent.getFontAssetId());
      textFieldText.setText(guiTextComponent.getText());
      colorPickerColor.setValue(ColorUtils.toJavaFxColor(guiTextComponent.getColor()));
    }
  }

  private GuiTextComponent createComponentFromFields() {
    return new GuiTextComponent(
      comboBoxFont.getValue(),
      textFieldText.getText(),
      ColorUtils.toSolaColor(colorPickerColor.getValue())
    );
  }
}
