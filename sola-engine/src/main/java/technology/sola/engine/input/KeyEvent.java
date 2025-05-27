package technology.sola.engine.input;

import org.jspecify.annotations.NullMarked;

/**
 * KeyEvent contains data for a keyboard related event.
 *
 * @param keyCode the ASCII code of the key for the event
 */
@NullMarked
public record KeyEvent(int keyCode) {
}
