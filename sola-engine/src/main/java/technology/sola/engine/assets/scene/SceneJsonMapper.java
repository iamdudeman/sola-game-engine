package technology.sola.engine.assets.scene;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Component;
import technology.sola.ecs.io.json.JsonWorldIo;
import technology.sola.ecs.io.json.WorldJsonMapper;
import technology.sola.json.JsonObject;
import technology.sola.json.mapper.JsonMapper;

import java.util.List;

/**
 * SceneJsonMapper is a {@link JsonMapper} implementation for {@link Scene}s.
 */
@NullMarked
public class SceneJsonMapper implements JsonMapper<Scene> {
  private WorldJsonMapper worldJsonMapper = new WorldJsonMapper(List.of());

  /**
   * Configures the {@link SceneJsonMapper} with the desired {@link Component} {@link JsonMapper}s.
   *
   * @param jsonMappers the component JSON mappers
   */
  public void configure(List<JsonMapper<? extends Component>> jsonMappers) {
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
}
