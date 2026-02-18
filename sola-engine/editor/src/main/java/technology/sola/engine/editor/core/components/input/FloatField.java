package technology.sola.engine.editor.core.components.input;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.FloatStringConverter;

public class FloatField extends TextField {
  private static final String FLOAT_REGEX = "-?\\d*\\.?\\d*";
  private final TextFormatter<Float> floatTextFormatter;

  public FloatField() {
    super();

    floatTextFormatter = new TextFormatter<>(new FloatStringConverter(), 0.0f, change -> {
      String newText = change.getControlNewText();

      if (newText.matches(FLOAT_REGEX)) {
        return change;
      } else {
        // Reject the change
        return null;
      }
    });

    // Set the formatter to the TextField
    this.setTextFormatter(floatTextFormatter);
  }

  public float getFloatValue() {
    return floatTextFormatter.getValue();
  }

  public void setFloatValue(float value) {
    floatTextFormatter.setValue(value);
  }
}
