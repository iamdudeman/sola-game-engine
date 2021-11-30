package technology.sola.engine.editor.components.ecs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import technology.sola.engine.ecs.Component;
import technology.sola.engine.ecs.Entity;

import java.io.IOException;

// TODO WIP
public abstract class AbstractComponentController<T extends Component> {
  protected Entity entity;

  protected abstract String getFxmlResource();

  public abstract Class<T> getComponentClass();

  protected abstract T createDefault();

  protected abstract void setFieldValuesFromEntity();

  public abstract T getComponentFromForm();

  public abstract void selectedStateChange(boolean isSelected);

  public void setEntity(Entity entity) {
    this.entity = entity;

    setFieldValuesFromEntity();
  }

  public Node getNode() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(getFxmlResource()));

    loader.setController(this);

    return loader.load();
  }
}
