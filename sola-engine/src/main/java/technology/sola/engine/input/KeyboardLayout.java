package technology.sola.engine.input;

import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;

/**
 * KeyboardLayout contains information for the current keyboard layout. This includes things like
 * what characters should render when shift is pressed. Defaults to {@link Type#QWERTY}.
 */
@NullMarked
public final class KeyboardLayout {
  private static Map<Character, Character> shiftMap = new HashMap<>();
  private static Type type = Type.QWERTY;
  private static HasShiftFunction hasShiftFunction = (keyCode) -> false;

  static {
    setType(Type.QWERTY);
  }

  /**
   * The type of keyboard.
   */
  public enum Type {
    /**
     * Standard QWERTY layout.
     */
    QWERTY
  }

  /**
   * Returns the shifted variant of the character for the current keyboard type. If there is no shifted variant then
   * the original character will return.
   *
   * @param character the character to get the shift variant for
   * @return the shifted variant or the original character if there is no shifted variant
   */
  public static char shift(char character) {
    return shiftMap.getOrDefault(character, character);
  }

  /**
   * Checks to see if a keyCode has a shifted variant character or not.
   *
   * @param keyCode the key code to check
   * @return true if it has a shifted variant for the current keyboard type
   */
  public static boolean hasShift(int keyCode) {
    return hasShiftFunction.hasShift(keyCode);
  }

  /**
   * @return the current {@link Type} of keyboard in use
   */
  public static Type getType() {
    return type;
  }

  /**
   * Sets the {@link Type} of keyboard in use.
   *
   * @param type the new type of keyboard in use
   */
  public static void setType(Type type) {
    KeyboardLayout.type = type;
    shiftMap = new HashMap<>();

    if (type == Type.QWERTY) {
      shiftMap.put('1', '!');
      shiftMap.put('2', '@');
      shiftMap.put('3', '#');
      shiftMap.put('4', '$');
      shiftMap.put('5', '%');
      shiftMap.put('6', '^');
      shiftMap.put('7', '&');
      shiftMap.put('8', '*');
      shiftMap.put('9', '(');
      shiftMap.put('0', ')');
      shiftMap.put('-', '_');
      shiftMap.put('=', '+');
      shiftMap.put('[', '{');
      shiftMap.put(']', '}');
      shiftMap.put('\\', '|');
      shiftMap.put(';', ':');
      shiftMap.put('\'', '"');
      shiftMap.put(',', '<');
      shiftMap.put('.', '>');
      shiftMap.put('/', '?');
      shiftMap.put('`', '~');

      hasShiftFunction = (int keyCode) ->
        (keyCode >= Key.COMMA.getCode() && keyCode <= Key.NINE.getCode())
          || keyCode == Key.SEMI_COLON.getCode()
          || keyCode == Key.EQUALS.getCode()
          || (keyCode >= Key.LEFT_BRACKET.getCode() && keyCode <= Key.RIGHT_BRACKET.getCode());
    }
  }

  private KeyboardLayout() {
  }

  @FunctionalInterface
  private interface HasShiftFunction {
    boolean hasShift(int keyCode);
  }
}
