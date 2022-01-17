package technology.sola.engine.editor.ui.ecs.rendering.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import technology.sola.engine.editor.core.SolaEditorContext;
import technology.sola.engine.editor.ui.ecs.ComponentController;
import technology.sola.engine.editor.ui.ecs.rendering.ColorUtils;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.components.GuiPanelComponent;

public class GuiPanelComponentController extends ComponentController<GuiPanelComponent> {
  @FXML
  private ColorPicker colorPickerBackground;
  @FXML
  private ColorPicker colorPickerBorder;
  @FXML
  private Button buttonClearBorder;

  public GuiPanelComponentController(SolaEditorContext solaEditorContext) {
    super(solaEditorContext);
  }

  @Override
  public void initialize() {
    colorPickerBackground.valueProperty().addListener(((observable, oldValue, newValue) -> {
      entity.addComponent(createComponentFromFields());
    }));
    colorPickerBorder.valueProperty().addListener(((observable, oldValue, newValue) -> {
      entity.addComponent(createComponentFromFields());
    }));
    buttonClearBorder.setOnAction(event -> {
      colorPickerBorder.setValue(null);
    });
  }

  @Override
  public GuiPanelComponent createDefault() {
    return new GuiPanelComponent(Color.BLACK);
  }

  @Override
  public Class<GuiPanelComponent> getComponentClass() {
    return GuiPanelComponent.class;
  }

  @Override
  protected String getFxmlResource() {
    return "GuiPanelComponent.fxml";
  }

  @Override
  protected void updateFieldValuesFromEntity() {
    GuiPanelComponent guiPanelComponent = entity.getComponent(GuiPanelComponent.class);

    if (guiPanelComponent != null) {
      colorPickerBackground.setValue(ColorUtils.toJavaFxColor(guiPanelComponent.getBackgroundColor()));
      colorPickerBorder.setValue(ColorUtils.toJavaFxColor(guiPanelComponent.getBorderColor()));
    }
  }

  private GuiPanelComponent createComponentFromFields() {
    return new GuiPanelComponent(
      ColorUtils.toSolaColor(colorPickerBackground.getValue()),
      ColorUtils.toSolaColor(colorPickerBorder.getValue())
    );
  }
}
