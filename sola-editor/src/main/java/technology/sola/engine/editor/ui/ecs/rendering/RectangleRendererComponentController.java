package technology.sola.engine.editor.ui.ecs.rendering;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import technology.sola.engine.editor.core.SolaEditorContext;
import technology.sola.engine.editor.ui.ecs.ComponentController;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.RectangleRendererComponent;

public class RectangleRendererComponentController extends ComponentController<RectangleRendererComponent> {
  @FXML
  private ColorPicker colorPicker;
  @FXML
  private CheckBox checkBoxFill;

  public RectangleRendererComponentController(SolaEditorContext solaEditorContext) {
    super(solaEditorContext);
  }

  @Override
  public void initialize() {
    colorPicker.valueProperty().addListener(((observable, oldValue, newValue) -> {
      entity.addComponent(createComponentFromFields());
    }));
    checkBoxFill.selectedProperty().addListener(((observable, oldValue, newValue) -> {
      entity.addComponent(createComponentFromFields());
    }));
  }

  @Override
  public RectangleRendererComponent createDefault() {
    return new RectangleRendererComponent(Color.BLACK);
  }

  @Override
  public Class<RectangleRendererComponent> getComponentClass() {
    return RectangleRendererComponent.class;
  }

  @Override
  protected String getFxmlResource() {
    return "RectangleRendererComponent.fxml";
  }

  @Override
  protected void updateFieldValuesFromEntity() {
    RectangleRendererComponent rectangleRendererComponent = entity.getComponent(RectangleRendererComponent.class);

    if (rectangleRendererComponent != null) {
      colorPicker.setValue(ColorUtils.toJavaFxColor(rectangleRendererComponent.getColor()));
      checkBoxFill.setSelected(rectangleRendererComponent.isFilled());
    }
  }

  private RectangleRendererComponent createComponentFromFields() {
    return new RectangleRendererComponent(ColorUtils.toSolaColor(colorPicker.getValue()), checkBoxFill.isSelected());
  }
}
