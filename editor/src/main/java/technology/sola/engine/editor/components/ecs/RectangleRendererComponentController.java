package technology.sola.engine.editor.components.ecs;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import technology.sola.engine.ecs.Entity;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.RectangleRendererComponent;

public class RectangleRendererComponentController implements ComponentController<RectangleRendererComponent> {
  private final Entity entity;

  @FXML
  private ColorPicker colorPicker;
  @FXML
  private CheckBox checkBoxFill;

  public RectangleRendererComponentController(Entity entity) {
    this.entity = entity;
  }

  @Override
  public String getFxmlResource() {
    return "RectangleRendererComponent.fxml";
  }

  @Override
  public void populateInitialValues() {
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

  @Override
  public RectangleRendererComponent create() {
    var colorPickerColor = colorPicker.getValue();
    var color = new Color(
      (int)(colorPickerColor.getOpacity() * 255),
      (int)(colorPickerColor.getRed() * 255),
      (int)(colorPickerColor.getGreen() * 255),
      (int)(colorPickerColor.getBlue() * 255)
    );

    return new RectangleRendererComponent(color, checkBoxFill.isSelected());
  }

  public void initialize() {
    populateInitialValues();

    colorPicker.valueProperty().addListener(((observable, oldValue, newValue) -> {
      entity.addComponent(create());
    }));
    checkBoxFill.selectedProperty().addListener(((observable, oldValue, newValue) -> {
      entity.addComponent(create());
    }));
  }
}
