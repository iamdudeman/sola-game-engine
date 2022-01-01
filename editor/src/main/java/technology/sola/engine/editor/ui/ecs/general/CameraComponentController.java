package technology.sola.engine.editor.ui.ecs.general;

import technology.sola.engine.editor.core.SolaEditorContext;
import technology.sola.engine.editor.ui.ecs.ComponentController;
import technology.sola.engine.graphics.components.CameraComponent;

public class CameraComponentController extends ComponentController<CameraComponent> {
  public CameraComponentController(SolaEditorContext solaEditorContext) {
    super(solaEditorContext);
  }

  @Override
  public void initialize() {

  }

  @Override
  public CameraComponent createDefault() {
    return new CameraComponent();
  }

  @Override
  public Class<CameraComponent> getComponentClass() {
    return CameraComponent.class;
  }

  @Override
  protected String getFxmlResource() {
    return "CameraComponent.fxml";
  }

  @Override
  protected void updateFieldValuesFromEntity() {
    // nothing to do here yet
  }
}
