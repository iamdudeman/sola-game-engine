package technology.sola.engine.editor.core.components;

import javafx.scene.text.Text;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.editor.core.styles.Css;

/**
 * ThemedText is an extension of {@link Text} that provides a consistent text theming across the editor.
 */
@NullMarked
public class ThemedText extends Text {
  /**
   * Creates an instance with default kind {@link Kind#PARAGRAPH}.
   *
   * @param text the text string
   */
  public ThemedText(String text) {
    this(Kind.PARAGRAPH, text);
  }

  /**
   * Creates an instance with desired {@link Kind} of text.
   *
   * @param kind the desired kind of text
   * @param text the text string
   */
  public ThemedText(Kind kind, String text) {
    super(text);

    var style = switch (kind) {
      case PARAGRAPH -> Css.Util.TEXT_PARAGRAPH;
    };

    getStyleClass().add(style);
  }

  /**
   * The different kinds of themed text available.
   */
  public enum Kind {
    /**
     * Standard usage paragraph text.
     */
    PARAGRAPH,
  }
}
