package technology.sola.engine.editor.ui.ecs.physics;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import technology.sola.engine.editor.core.SolaEditorContext;
import technology.sola.engine.editor.ui.ecs.ComponentController;
import technology.sola.engine.physics.component.DynamicBodyComponent;

// TODO ability to set Material

public class DynamicBodyComponentController extends ComponentController<DynamicBodyComponent> {
  @FXML
  private CheckBox checkBoxKinematic;

  public DynamicBodyComponentController(SolaEditorContext solaEditorContext) {
    super(solaEditorContext);
  }

  @Override
  public void initialize() {
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
      checkBoxKinematic.setSelected(dynamicBodyComponent.isKinematic());
    }
  }

  private DynamicBodyComponent createComponentFromFields() {
    return new DynamicBodyComponent(checkBoxKinematic.isSelected());
  }
}
