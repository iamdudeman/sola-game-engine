package technology.sola.engine.assets.input;

import technology.sola.engine.defaults.controls.ControlInput;
import technology.sola.engine.defaults.controls.KeyControlInput;
import technology.sola.engine.defaults.controls.MouseButtonControlInput;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.MouseButton;
import technology.sola.json.JsonArray;
import technology.sola.json.JsonObject;
import technology.sola.json.mapper.JsonMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ControlsConfigJsonMapper is a {@link JsonMapper} implementation for {@link ControlsConfig}.
 */
public class ControlsConfigJsonMapper implements JsonMapper<ControlsConfig> {
  @Override
  public Class<ControlsConfig> getObjectClass() {
    return ControlsConfig.class;
  }

  @Override
  public JsonObject toJson(ControlsConfig controlsConfig) {
    JsonArray controlsJson = new JsonArray();

    for (var controlEntry : controlsConfig.controls().entrySet()) {
      JsonArray controlInputsJson = new JsonArray();

      for (var controlInputs : controlEntry.getValue()) {
        JsonArray controlInputJson = new JsonArray();

        for (var controlInput : controlInputs) {
          controlInputJson.add(controlInputToJson(controlInput));
        }

        controlInputsJson.add(controlInputJson);
      }

      controlsJson.add(
        new JsonObject()
          .put("id", controlEntry.getKey())
          .put("inputs", controlInputsJson)
      );
    }

    return new JsonObject().put("controls", controlsJson);
  }

  @Override
  public ControlsConfig toObject(JsonObject jsonObject) {
    Map<String, List<List<ControlInput<?>>>> controls = new HashMap<>();
    JsonArray controlsJson = jsonObject.getArray("controls");

    for (var controlJsonElement : controlsJson) {
      var controlJson = controlJsonElement.asObject();
      String id = controlJson.getString("id");
      List<List<ControlInput<?>>> controlInputs = new ArrayList<>();

      for (var controlInputsJsonElement : controlJson.getArray("inputs")) {
        List<ControlInput<?>> controlInput = new ArrayList<>();

        for (var controlInputJsonElement : controlInputsJsonElement.asArray()) {
          controlInput.add(jsonToControlInput(controlInputJsonElement.asObject()));
        }

        controlInputs.add(controlInput);
      }

      controls.put(id, controlInputs);
    }

    return new ControlsConfig(controls);
  }

  private ControlInput<?> jsonToControlInput(JsonObject jsonObject) {
    String type = jsonObject.getString("type");

    return switch (type) {
      case "key" -> new KeyControlInput(
        Key.valueOf(jsonObject.getString("key")),
        KeyControlInput.State.valueOf(jsonObject.getString("state"))
      );
      case "mouse" -> new MouseButtonControlInput(
        MouseButton.valueOf(jsonObject.getString("button")),
        MouseButtonControlInput.State.valueOf(jsonObject.getString("state"))
      );
      default -> throw new IllegalArgumentException("Input type " + type + " not supported");
    };
  }

  private JsonObject controlInputToJson(ControlInput<?> controlInput) {
    if (controlInput instanceof KeyControlInput keyControlCondition) {
      return new JsonObject()
        .put("type", "key")
        .put("key", keyControlCondition.key().name())
        .put("state", keyControlCondition.state().name());
    } else if (controlInput instanceof MouseButtonControlInput mouseButtonControlCondition) {
      return new JsonObject()
        .put("type", "mouse")
        .put("button", mouseButtonControlCondition.button().name())
        .put("state", mouseButtonControlCondition.state().name());
    } else {
      throw new IllegalArgumentException("ControlInput " + controlInput.getClass() + " not supported");
    }
  }
}
