package technology.sola.engine.input;

import java.util.Arrays;

/**
 * MouseButton enum contains information about mouse buttons.
 */
public enum MouseButton {
  /**
   * Represents no button.
   */
  NONE(0),
  /**
   * Represents primary (button 1, usually the left) mouse button.
   */
  PRIMARY(1),
  /**
   * Represents middle (button 2) mouse button.
   */
  MIDDLE(2),
  /**
   * Represents secondary (button 3, usually the right) mouse button.
   */
  SECONDARY(3),
  /**
   * Represents back (button 4) mouse button.
   */
  BACK(4),
  /**
   * Represents forward (button 5) mouse button.
   */
  FORWARD(5);

  private final int code;

  /**
   * Returns the MouseButton enum value for a integer button code.
   *
   * @param buttonCode the integer button code
   * @return MouseButton value
   * @throws IllegalArgumentException if invalid button code
   */
  public static MouseButton valueOf(int buttonCode) {
    if (buttonCode < 0 || buttonCode > MouseButton.values().length) {
      throw new IllegalArgumentException(buttonCode + " must be in MouseButton values " + Arrays.toString(MouseButton.values()));
    }

    return MouseButton.values()[buttonCode];
  }

  MouseButton(int code) {
    this.code = code;
  }

  /**
   * @return the integer code of the mouse button
   */
  public int getCode() {
    return code;
  }

  @Override
  public String toString() {
    return code + ": " + name();
  }
}
