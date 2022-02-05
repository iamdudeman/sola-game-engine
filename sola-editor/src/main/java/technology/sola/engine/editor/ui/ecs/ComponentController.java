package technology.sola.engine.editor.ui.ecs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.engine.editor.core.SolaEditorContext;

import java.io.IOException;

public abstract class ComponentController<T extends Component<?>> {
  protected Entity entity;
  protected SolaEditorContext solaEditorContext;
  private Node node;

  public ComponentController(SolaEditorContext solaEditorContext) {
    this.solaEditorContext = solaEditorContext;

    FXMLLoader loader = new FXMLLoader(getClass().getResource(getFxmlResource()));

    loader.setController(this);

    try {
      node = loader.load();
    } catch (IOException ex) {
      // todo handle this better
      ex.printStackTrace();
    }
  }

  public abstract void initialize();

  public abstract T createDefault();

  public abstract Class<T> getComponentClass();

  public void setEntity(Entity entity) {
    this.entity = entity;

    if (entity != null) {
      updateFieldValuesFromEntity();
    }
  }

  public Node getNode() {
    return node;
  }

  protected abstract String getFxmlResource();

  protected abstract void updateFieldValuesFromEntity();
}
