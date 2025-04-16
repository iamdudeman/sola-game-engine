package technology.sola.engine.editor.core.components.control;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

import java.util.regex.Pattern;

/**
 * IntegerSpinner is a {@link Spinner} that is set for integers only.
 */
public class IntegerSpinner extends Spinner<Integer> {
  /**
   * Creates an integer spinner with desired min and max values.
   *
   * @param min the minimum value
   * @param max the maximum value
   */
  public IntegerSpinner(int min, int max) {
    setEditable(true);

    setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, min, 1));

    getEditor().setTextFormatter(new TextFormatter<>(
      new IntegerStringConverter(),
      min,
      c -> Pattern.matches("\\d*", c.getText()) ? c : null)
    );
  }
}
