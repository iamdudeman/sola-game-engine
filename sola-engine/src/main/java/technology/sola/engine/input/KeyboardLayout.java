package technology.sola.engine.input;

import java.util.Map;

public final class KeyboardLayout {
  private static Map<Character, Character> shiftMap;
  private static Type type = Type.Qwerty;

  static {
    setType(Type.Qwerty);
  }

  public enum Type {
    Qwerty
  }

  public static char shift(char character) {
    return shiftMap.get(character);
  }

  public static Type getType() {
    return type;
  }

  public static void setType(Type type) {
    KeyboardLayout.type = type;

    if (type == Type.Qwerty) {
      shiftMap = Map.of(
        '1', '!',
        '2', '@',
        '3', '#',
        '4', '$',
        '5', '%',
        '6', '^',
        '7', '&',
        '8', '*',
        '9', '(',
        '0', ')'
      );
    }
  }

  private KeyboardLayout() {
  }
}
