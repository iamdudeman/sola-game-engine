package technology.sola.engine.assets.scene;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Component;
import technology.sola.ecs.io.json.WorldJsonMapper;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.CameraComponent;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.json.JsonObject;
import technology.sola.json.mapper.JsonMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * SceneJsonMapper is a {@link JsonMapper} implementation for {@link Scene}s.
 */
@NullMarked
public class SceneJsonMapper implements JsonMapper<Scene> {
  private WorldJsonMapper worldJsonMapper;

  /**
   * Creates an instance of this {@link JsonMapper} with default {@link Component} {@link JsonMapper}s configured.
   */
  public SceneJsonMapper() {
    worldJsonMapper = new WorldJsonMapper(getDefaultJsonMappers());
  }

  /**
   * Configures the {@link SceneJsonMapper} with the desired {@link Component} {@link JsonMapper}s. The default
   * component JsonMappers are included automatically.
   *
   * @param additionalJsonMappers the component JSON mappers to add
   */
  public void configure(List<JsonMapper<? extends Component>> additionalJsonMappers) {
    List<JsonMapper<? extends Component>> jsonMappers = new ArrayList<>();

    jsonMappers.addAll(getDefaultJsonMappers());
    jsonMappers.addAll(additionalJsonMappers);

    worldJsonMapper = new WorldJsonMapper(jsonMappers);
  }

  @Override
  public Class<Scene> getObjectClass() {
    return Scene.class;
  }

  @Override
  public JsonObject toJson(Scene object) {
    JsonObject jsonObject = new JsonObject();

    jsonObject.put("world", worldJsonMapper.toJson(object.world()));

    return jsonObject;
  }

  @Override
  public Scene toObject(JsonObject jsonObject) {
    var world = jsonObject.getObject("world");

    return new Scene(worldJsonMapper.toObject(world));
  }

  private List<JsonMapper<? extends Component>> getDefaultJsonMappers() {
    return List.of(
      new TransformComponent.Mapper(),
      new CameraComponent.Mapper(),
      new CircleRendererComponent.Mapper()
    );
  }
}
