package technology.sola.engine.editor.core.components.input;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.FloatStringConverter;
import org.jspecify.annotations.NullMarked;

/**
 * FloatField is a {@link TextField} that is set for floats only.
 */
@NullMarked
public class FloatField extends TextField {
  private static final String FLOAT_REGEX = "-?\\d*\\.?\\d*";
  private final TextFormatter<Float> floatTextFormatter;

  /**
   * Creates a FloatField with an initial value of 0.
   */
  public FloatField() {
    this(0f);
  }

  /**
   * Creates a FloatField with the desired initial value.
   *
   * @param initialValue the initial value of the input
   */
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

  /**
   * @return the observable float property
   */
  public ObjectProperty<Float> floatValueProperty() {
    return floatTextFormatter.valueProperty();
  }
}
