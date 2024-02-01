package technology.sola.engine.input;

import java.util.HashMap;
import java.util.Map;

/**
 * KeyboardLayout contains information for the current keyboard layout. This includes things like
 * what characters should render when shift is pressed.
 */
public final class KeyboardLayout {
  private static Map<Character, Character> shiftMap;
  private static Type type = Type.QWERTY;
  private static HasShiftFunction hasShiftFunction;

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

  public static char shift(char character) {
    return shiftMap.get(character);
  }

  public static boolean hasShift(int keyCode) {
    return hasShiftFunction.hasShift(keyCode);
  }

  public static Type getType() {
    return type;
  }

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
