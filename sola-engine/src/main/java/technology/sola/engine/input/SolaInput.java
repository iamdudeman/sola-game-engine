package technology.sola.engine.input;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/*
todo
this could be populated by a controls.input.json file or something like that
[
  {
    "id": "jump",
    "controls": [
      [{ "type": "key", "key": "A" }, { "type": "key", "key": "SHIFT" }]
    ]
  },
  {
    "id": "jump",
    "controls": [
      { "type": "mouse", "button": "PRIMARY" }
    ]
  }
]
 */

public class SolaInput {

  public SolaInput(KeyboardInput keyboardInput, MouseInput mouseInput) {
    // todo
  }

  public boolean isOn(String inputId) {
    throw new RuntimeException("not yet implemented");
  }

  public void add(Input input) {
    throw new RuntimeException("not yet implemented");
  }

  public record Input(
    String id, List<List<Control>> controls
  ) {
  }

  interface Control {
    String type();
  }

  public record KeyControl(Key key) implements Control {
    @Override
    public String type() {
      return "key";
    }
  }

  public record MouseControl(MouseButton button) implements Control {
    @Override
    public String type() {
      return "mouse";
    }
  }
}
