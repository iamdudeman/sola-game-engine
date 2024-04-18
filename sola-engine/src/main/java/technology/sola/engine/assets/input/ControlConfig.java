package technology.sola.engine.assets.input;

import technology.sola.engine.assets.Asset;
import technology.sola.engine.defaults.input.ControlInput;
import technology.sola.engine.defaults.input.KeyControlInput;
import technology.sola.engine.defaults.input.MouseButtonControlInput;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.MouseButton;
import technology.sola.json.JsonArray;
import technology.sola.json.JsonObject;
import technology.sola.json.mapper.JsonMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// todo update naming

public record ControlConfig(Map<String, List<List<ControlInput<?>>>> controls) implements Asset {
  public static class InputConfigJsonMapper implements JsonMapper<ControlConfig> {
    @Override
    public Class<ControlConfig> getObjectClass() {
      return ControlConfig.class;
    }

    @Override
    public JsonObject toJson(ControlConfig controlConfig) {
      JsonArray controlsJson = new JsonArray();

      for (var controlEntry : controlConfig.controls().entrySet()) {
        JsonArray controlConditionsJson = new JsonArray();

        for (var controlConditions : controlEntry.getValue()) {
          JsonArray controlConditionJson = new JsonArray();

          for (var controlCondition : controlConditions) {
            if (controlCondition instanceof KeyControlInput keyControlCondition) {
              controlConditionJson.add(
                new JsonObject()
                  .put("type", "key")
                  .put("key", keyControlCondition.key().name())
                  .put("state", keyControlCondition.state().name())
              );
            } else if (controlCondition instanceof MouseButtonControlInput mouseButtonControlCondition) {
              controlConditionJson.add(
                new JsonObject()
                  .put("type", "mouse")
                  .put("button", mouseButtonControlCondition.button().name())
                  .put("state", mouseButtonControlCondition.state().name())
              );
            } else {
              throw new IllegalArgumentException("ControlCondition " + controlCondition.getClass() + " not supported");
            }
          }

          controlConditionsJson.add(controlConditionJson);
        }

        controlsJson.add(
          new JsonObject()
            .put("id", controlEntry.getKey())
            .put("inputs", controlConditionsJson)
        );
      }

      return new JsonObject().put("controls", controlsJson);
    }

    @Override
    public ControlConfig toObject(JsonObject jsonObject) {
      Map<String, List<List<ControlInput<?>>>> controls = new HashMap<>();
      JsonArray controlsJson = jsonObject.getArray("controls");

      for (var controlJsonElement : controlsJson) {
        var controlJson = controlJsonElement.asObject();
        String id = controlJson.getString("id");
        List<List<ControlInput<?>>> controlConditions = new ArrayList<>();

        for (var inputsJsonElement : controlJson.getArray("inputs")) {
          List<ControlInput<?>> inputList = new ArrayList<>();

          for (var inputConditionJsonElement : inputsJsonElement.asArray()) {
            inputList.add(jsonToControlInput(inputConditionJsonElement.asObject()));
          }

          controlConditions.add(inputList);
        }

        controls.put(id, controlConditions);
      }

      return new ControlConfig(controls);
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
  }
}
