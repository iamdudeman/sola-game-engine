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
      colorPicker.setValue(new javafx.scene.paint.Color(
        circleRendererComponent.getColor().getRed() / 255f,
        circleRendererComponent.getColor().getGreen() / 255f,
        circleRendererComponent.getColor().getBlue() / 255f,
        circleRendererComponent.getColor().getAlpha() / 255f
      ));
      checkBoxFill.setSelected(circleRendererComponent.isFilled());
    }
  }

  private CircleRendererComponent createComponentFromFields() {
    var colorPickerColor = colorPicker.getValue();
    var color = new Color(
      (int)(colorPickerColor.getOpacity() * 255),
      (int)(colorPickerColor.getRed() * 255),
      (int)(colorPickerColor.getGreen() * 255),
      (int)(colorPickerColor.getBlue() * 255)
    );

    return new CircleRendererComponent(color, checkBoxFill.isSelected());
  }
}
