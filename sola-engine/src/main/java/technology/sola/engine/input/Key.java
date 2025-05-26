package technology.sola.engine.input;

import org.jspecify.annotations.NullMarked;

/**
 * Key enum contains information about keyboard keys.
 */
@NullMarked
public enum Key {
  // spacer for missing keys

  /**
   * Backspace key.
   */
  BACKSPACE(8, "Backspace"),
  /**
   * Tab key.
   */
  TAB(9, "Tab"),
  /**
   * Enter key.
   */
  ENTER(10, "Enter"),

  // spacer for missing keys

  /**
   * Carriage Return key.
   */
  CARRIAGE_RETURN(13, "Carriage Return"),

  // spacer for missing keys

  /**
   * Shift key.
   */
  SHIFT(16, "Shift"),
  /**
   * Control key.
   */
  CONTROL(17, "Control"),
  /**
   * Alt key.
   */
  ALT(18, "Alt"),

  // spacer for missing keys

  /**
   * Caps Lock key.
   */
  CAPS_LOCK(20, "Caps Lock"),

  // spacer for missing keys

  /**
   * Escape key.
   */
  ESCAPE(27, "Escape"),

  // spacer for missing keys

  /**
   * Space key.
   */
  SPACE(32, "Space"),
  /**
   * Page Up key.
   */
  PAGE_UP(33, "Page Up"),
  /**
   * Page Down key.
   */
  PAGE_DOWN(34, "Page Down"),
  /**
   * End key.
   */
  END(35, "End"),
  /**
   * Home key.
   */
  HOME(36, "Home"),
  /**
   * Left key.
   */
  LEFT(37, "Left"),
  /**
   * Up key.
   */
  UP(38, "Up"),
  /**
   * Right key.
   */
  RIGHT(39, "Right"),
  /**
   * Down key.
   */
  DOWN(40, "Down"),

  // spacer for missing keys

  /**
   * Comma key.
   */
  COMMA(44, "Comma"),
  /**
   * Hyphen key.
   */
  HYPHEN(45, "Hyphen"),
  /**
   * Period key.
   */
  PERIOD(46, "Period"),
  /**
   * Forward Slash key.
   */
  FORWARD_SLASH(47, "Forward Slash"),
  /**
   * 0 key.
   */
  ZERO(48, "0"),
  /**
   * 1 key.
   */
  ONE(49, "1"),
  /**
   * 2 key.
   */
  TWO(50, "2"),
  /**
   * 3 key.
   */
  THREE(51, "3"),
  /**
   * 4 key.
   */
  FOUR(52, "4"),
  /**
   * 5 key.
   */
  FIVE(53, "5"),
  /**
   * 6 key.
   */
  SIX(54, "6"),
  /**
   * 7 key.
   */
  SEVEN(55, "7"),
  /**
   * 8 key.
   */
  EIGHT(56, "8"),
  /**
   * 9 key.
   */
  NINE(57, "9"),

  // spacer for missing keys

  /**
   * Semi Colon key.
   */
  SEMI_COLON(59, "Semi Colon"),

  // spacer for missing keys

  /**
   * Equals key.
   */
  EQUALS(61, "Equals"),

  // spacer for missing keys

  /**
   * A key.
   */
  A(65, "A"),
  /**
   * B key.
   */
  B(66, "B"),
  /**
   * C key.
   */
  C(67, "C"),
  /**
   * D key.
   */
  D(68, "D"),
  /**
   * E key.
   */
  E(69, "E"),
  /**
   * F key.
   */
  F(70, "F"),
  /**
   * G key.
   */
  G(71, "G"),
  /**
   * H key.
   */
  H(72, "H"),
  /**
   * I key.
   */
  I(73, "I"),
  /**
   * J key.
   */
  J(74, "J"),
  /**
   * K key.
   */
  K(75, "K"),
  /**
   * L key.
   */
  L(76, "L"),
  /**
   * M key.
   */
  M(77, "M"),
  /**
   * N key.
   */
  N(78, "N"),
  /**
   * O key.
   */
  O(79, "O"),
  /**
   * P key.
   */
  P(80, "P"),
  /**
   * Q key.
   */
  Q(81, "Q"),
  /**
   * R key.
   */
  R(82, "R"),
  /**
   * S key.
   */
  S(83, "S"),
  /**
   * T key.
   */
  T(84, "T"),
  /**
   * U key.
   */
  U(85, "U"),
  /**
   * V key.
   */
  V(86, "V"),
  /**
   * W key.
   */
  W(87, "W"),
  /**
   * X key.
   */
  X(88, "X"),
  /**
   * Y key.
   */
  Y(89, "Y"),
  /**
   * Z key.
   */
  Z(90, "Z"),
  /**
   * Left Bracket key.
   */
  LEFT_BRACKET(91, "Left Bracket"),
  /**
   * Backslash key.
   */
  BACKSLASH(92, "Backslash"),
  /**
   * Right Bracket key.
   */
  RIGHT_BRACKET(93, "Right Bracket"),

  // spacer for missing keys

  /**
   * Num 0 key.
   */
  NUM_ZERO(96, "Num 0"),
  /**
   * Num 1 key.
   */
  NUM_ONE(97, "Num 1"),
  /**
   * Num 2 key.
   */
  NUM_TWO(98, "Num 2"),
  /**
   * Num 3 key.
   */
  NUM_THREE(99, "Num 3"),
  /**
   * Num 4 key.
   */
  NUM_FOUR(100, "Num 4"),
  /**
   * Num 5 key.
   */
  NUM_FIVE(101, "Num 5"),
  /**
   * Num 6 key.
   */
  NUM_SIX(102, "Num 6"),
  /**
   * Num 7 key.
   */
  NUM_SEVEN(103, "Num 7"),
  /**
   * Num 8 key.
   */
  NUM_EIGHT(104, "Num 8"),
  /**
   * Num 9 key.
   */
  NUM_NINE(105, "Num 9"),
  /**
   * Multiply key.
   */
  NUM_MULTIPLY(106, "Multiply"),
  /**
   * Plus key.
   */
  NUM_PLUS(107, "Plus"),

  // spacer for missing keys

  /**
   * Minus key.
   */
  NUM_MINUS(109, "Minus"),
  /**
   * Point key.
   */
  NUM_POINT(110, "Point"),
  /**
   * Divide key.
   */
  NUM_DIVIDE(111, "Divide"),
  /**
   * F1 key.
   */
  F1(112, "F1"),
  /**
   * F2 key.
   */
  F2(113, "F2"),
  /**
   * F3 key.
   */
  F3(114, "F3"),
  /**
   * F4 key.
   */
  F4(115, "F4"),
  /**
   * F5 key.
   */
  F5(116, "F5"),
  /**
   * F6 key.
   */
  F6(117, "F6"),
  /**
   * F7 key.
   */
  F7(118, "F7"),
  /**
   * F8 key.
   */
  F8(119, "F8"),
  /**
   * F8 key.
   */
  F9(120, "F9"),
  /**
   * F10 key.
   */
  F10(121, "F10"),
  /**
   * F11 key.
   */
  F11(122, "F11"),
  /**
   * F12 key.
   */
  F12(123, "F12"),

  // spacer for missing keys

  /**
   * Delete key.
   */
  DELETE(127, "Delete"),

  // spacer for missing keys

  /**
   * Num Lock key.
   */
  NUM_LOCK(144, "Num Lock"),

  // spacer for missing keys

  /**
   * Insert key.
   */
  INSERT(155, "Insert"),

  // spacer for missing keys

  /**
   * Back Quote key.
   */
  BACK_QUOTE(192, "Back Quote"),

  // spacer for missing keys

  /**
   * Single Quote key.
   */
  SINGLE_QUOTE(222, "Single Quote"),
  ;

  private final int code;
  private final String name;

  Key(int code, String name) {
    this.code = code;
    this.name = name;
  }

  /**
   * @return the ASCII code of the Key
   */
  public int getCode() {
    return code;
  }

  /**
   * @return the display name of the Key
   */
  public String getName() {
    return name;
  }
}
