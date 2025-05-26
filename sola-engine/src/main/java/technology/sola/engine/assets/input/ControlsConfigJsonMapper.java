package technology.sola.engine.assets.input;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.defaults.controls.*;
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
@NullMarked
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
        JsonArray controlInputConditionsJson = new JsonArray();

        for (var controlInputCondition : controlInputs.conditions()) {
          controlInputConditionsJson.add(controlInputConditionToJson(controlInputCondition));
        }

        controlInputsJson.add(controlInputConditionsJson);
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
    Map<String, List<ControlInput>> controls = new HashMap<>();
    JsonArray controlsJson = jsonObject.getArray("controls");

    for (var controlJsonElement : controlsJson) {
      var controlJson = controlJsonElement.asObject();
      String id = controlJson.getString("id");
      List<ControlInput> controlInputs = new ArrayList<>();

      for (var controlInputsJsonElement : controlJson.getArray("inputs")) {
        List<ControlInputCondition<?>> controlInputConditions = new ArrayList<>();

        for (var controlInputConditionJsonElement : controlInputsJsonElement.asArray()) {
          controlInputConditions.add(jsonToControlInputCondition(controlInputConditionJsonElement.asObject()));
        }

        controlInputs.add(new ControlInput(controlInputConditions));
      }

      controls.put(id, controlInputs);
    }

    return new ControlsConfig(controls);
  }

  private ControlInputCondition<?> jsonToControlInputCondition(JsonObject jsonObject) {
    String type = jsonObject.getString("type");

    return switch (type) {
      case "key" -> new KeyControlInputCondition(
        Key.valueOf(jsonObject.getString("key")),
        KeyControlInputCondition.State.valueOf(jsonObject.getString("state"))
      );
      case "mouse" -> new MouseButtonControlInputCondition(
        MouseButton.valueOf(jsonObject.getString("button")),
        MouseButtonControlInputCondition.State.valueOf(jsonObject.getString("state"))
      );
      case "wheel" -> new MouseWheelControlInputCondition(
        MouseWheelControlInputCondition.State.valueOf(jsonObject.getString("state"))
      );
      default -> throw new IllegalArgumentException("Input type " + type + " not supported");
    };
  }

  private JsonObject controlInputConditionToJson(ControlInputCondition<?> controlInputCondition) {
    if (controlInputCondition instanceof KeyControlInputCondition keyControlCondition) {
      return new JsonObject()
        .put("type", "key")
        .put("key", keyControlCondition.key().name())
        .put("state", keyControlCondition.state().name());
    } else if (controlInputCondition instanceof MouseButtonControlInputCondition mouseButtonControlCondition) {
      return new JsonObject()
        .put("type", "mouse")
        .put("button", mouseButtonControlCondition.button().name())
        .put("state", mouseButtonControlCondition.state().name());
    } else if (controlInputCondition instanceof MouseWheelControlInputCondition mouseWheelControlInputCondition) {
      return new JsonObject()
        .put("type", "wheel")
        .put("state", mouseWheelControlInputCondition.state().name());
    } else {
      throw new IllegalArgumentException("ControlInput " + controlInputCondition.getClass() + " not supported");
    }
  }
}
