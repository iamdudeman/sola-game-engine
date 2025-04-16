package technology.sola.engine.editor.core.components.control;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

import java.util.regex.Pattern;

public class IntegerSpinner extends Spinner<Integer> {
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
