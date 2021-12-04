package technology.sola.engine.editor.ui.ecs.rendering;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import technology.sola.engine.editor.ui.ecs.ComponentController;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CircleRendererComponent;

public class CircleRendererComponentController extends ComponentController<CircleRendererComponent> {
  @FXML
  private ColorPicker colorPicker;
  @FXML
  private CheckBox checkBoxFill;

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
  public CircleRendererComponent createDefault() {
    return new CircleRendererComponent(Color.BLACK);
  }

  @Override
  public Class<CircleRendererComponent> getComponentClass() {
    return CircleRendererComponent.class;
  }

  @Override
  protected String getFxmlResource() {
    return "CircleRendererComponent.fxml";
  }

  @Override
  protected void updateFieldValuesFromEntity() {
    CircleRendererComponent circleRendererComponent = entity.getComponent(CircleRendererComponent.class);

    if (circleRendererComponent != null) {
      colorPicker.setValue(ColorUtils.toJavaFxColor(circleRendererComponent.getColor()));
      checkBoxFill.setSelected(circleRendererComponent.isFilled());
    }
  }

  private CircleRendererComponent createComponentFromFields() {
    return new CircleRendererComponent(ColorUtils.toSolaColor(colorPicker.getValue()), checkBoxFill.isSelected());
  }
}
