package technology.sola.engine.graphics.components;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Component;
import technology.sola.json.JsonObject;
import technology.sola.json.mapper.JsonMapper;

/**
 * CameraComponent is a {@link Component} that contains data for a 2d camera for an {@link technology.sola.ecs.Entity}.
 */
@NullMarked
public class CameraComponent implements Component {
  private int priority = 0;

  /**
   * The camera with the lowest priority value will be used during rendering.
   *
   * @return the priority value
   */
  public int getPriority() {
    return priority;
  }

  /**
   * Updates the priority for this camera. The camera with the lowest priority will be used during rendering.
   *
   * @param priority the new priority
   */
  public void setPriority(int priority) {
    this.priority = priority;
  }

  public static class Mapper implements JsonMapper<CameraComponent> {
    @Override
    public Class<CameraComponent> getObjectClass() {
      return CameraComponent.class;
    }

    @Override
    public JsonObject toJson(CameraComponent cameraComponent) {
      JsonObject jsonObject = new JsonObject();

      jsonObject.put("priority", cameraComponent.getPriority());

      return jsonObject;
    }

    @Override
    public CameraComponent toObject(JsonObject jsonObject) {
      var cameraComponent = new CameraComponent();

      cameraComponent.setPriority(jsonObject.getInt("priority"));

      return cameraComponent;
    }
  }
}
