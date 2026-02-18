package technology.sola.engine.editor.core.components.input;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.FloatStringConverter;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class FloatField extends TextField {
  private static final String FLOAT_REGEX = "-?\\d*\\.?\\d*";
  private final TextFormatter<Float> floatTextFormatter;

  public FloatField() {
    this(0f);
  }

  public FloatField(float initialValue) {
    super();

    floatTextFormatter = new TextFormatter<>(new FloatStringConverter(), initialValue, change -> {
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

  public ObjectProperty<Float> floatValueProperty() {
    return floatTextFormatter.valueProperty();
  }
}
