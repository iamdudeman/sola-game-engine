package technology.sola.engine.editor.components.ecs;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.RectangleRendererComponent;

public class RectangleRendererComponentController extends ComponentController<RectangleRendererComponent> {
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
      colorPicker.setValue(new javafx.scene.paint.Color(
        rectangleRendererComponent.getColor().getRed() / 255f,
        rectangleRendererComponent.getColor().getGreen() / 255f,
        rectangleRendererComponent.getColor().getBlue() / 255f,
        rectangleRendererComponent.getColor().getAlpha() / 255f
      ));
      checkBoxFill.setSelected(rectangleRendererComponent.isFilled());
    }
  }

  private RectangleRendererComponent createComponentFromFields() {
    var colorPickerColor = colorPicker.getValue();
    var color = new Color(
      (int)(colorPickerColor.getOpacity() * 255),
      (int)(colorPickerColor.getRed() * 255),
      (int)(colorPickerColor.getGreen() * 255),
      (int)(colorPickerColor.getBlue() * 255)
    );

    return new RectangleRendererComponent(color, checkBoxFill.isSelected());
  }
}
