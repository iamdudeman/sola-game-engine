package technology.sola.engine.editor.ui.ecs.physics;

import com.sun.javafx.scene.control.DoubleField;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.editor.core.SolaEditorContext;
import technology.sola.engine.editor.ui.ecs.ComponentController;
import technology.sola.engine.physics.component.ColliderComponent;

public class ColliderComponentController extends ComponentController<ColliderComponent> {
  @FXML
  private ComboBox<String> comboBoxColliderType;
  @FXML
  private Label labelWidth;
  @FXML
  private Label labelHeight;
  @FXML
  private Label labelRadius;
  @FXML
  private DoubleField doubleFieldWidth;
  @FXML
  private DoubleField doubleFieldHeight;
  @FXML
  private DoubleField doubleFieldRadius;

  public ColliderComponentController(SolaEditorContext solaEditorContext) {
    super(solaEditorContext);
  }

  @Override
  public void initialize() {
    comboBoxColliderType.valueProperty().addListener(((observable, oldValue, newValue) -> {
      if ("AABB".equals(newValue)) {
        labelWidth.setVisible(true);
        labelWidth.setManaged(true);
        labelHeight.setVisible(true);
        labelHeight.setManaged(true);
        labelRadius.setVisible(false);
        labelRadius.setManaged(false);
      } else if ("Circle".equals(newValue)) {
        labelWidth.setVisible(false);
        labelWidth.setManaged(false);
        labelHeight.setVisible(false);
        labelHeight.setManaged(false);
        labelRadius.setVisible(true);
        labelRadius.setManaged(true);
      }

      entity.addComponent(createComponentFromFields());
    }));

    doubleFieldWidth.valueProperty().addListener(observable -> entity.addComponent(createComponentFromFields()));
    doubleFieldHeight.valueProperty().addListener(observable -> entity.addComponent(createComponentFromFields()));
    doubleFieldRadius.valueProperty().addListener(observable -> entity.addComponent(createComponentFromFields()));
  }

  @Override
  public ColliderComponent createDefault() {
    return ColliderComponent.aabb();
  }

  @Override
  public Class<ColliderComponent> getComponentClass() {
    return ColliderComponent.class;
  }

  @Override
  protected String getFxmlResource() {
    return "ColliderComponent.fxml";
  }

  @Override
  protected void updateFieldValuesFromEntity() {
    ColliderComponent colliderComponent = entity.getComponent(ColliderComponent.class);

    if (colliderComponent != null) {
      switch (colliderComponent.getColliderType()) {
        case AABB:
          comboBoxColliderType.setValue("AABB");
          var temp = colliderComponent.asRectangle(new TransformComponent());
          doubleFieldWidth.setValue(temp.getWidth());
          doubleFieldHeight.setValue(temp.getHeight());
          doubleFieldRadius.setValue(0.5);
          break;
        case CIRCLE:
          comboBoxColliderType.setValue("Circle");
          var temp2 = colliderComponent.asCircle(new TransformComponent());
          doubleFieldWidth.setValue(1);
          doubleFieldHeight.setValue(1);
          doubleFieldRadius.setValue(temp2.getRadius());
      }
    }
  }

  private ColliderComponent createComponentFromFields() {
    if (comboBoxColliderType.getValue().equals("AABB")) {
      return ColliderComponent.aabb((float)doubleFieldWidth.getValue(), (float) doubleFieldHeight.getValue());
    }

    return ColliderComponent.circle((float)doubleFieldRadius.getValue());
  }
}
