package technology.sola.engine.input;

public enum Key {
  //
  BACKSPACE(8, "Backspace"),
  TAB(9, "Tab"),
  ENTER(10, "Enter"),
  //
  CARRIAGE_RETURN(13, "Carriage Return"),
  //
  SHIFT(16, "Shift"),
  CONTROL(17, "Control"),
  ALT(18, "Alt"),
  //
  CAPS_LOCK(20, "Caps Lock"),
  //
  ESCAPE(27, "Escape"),
  //
  SPACE(32, "Space"),
  PAGE_UP(33, "Page Up"),
  PAGE_DOWN(34, "Page Down"),
  END(35, "End"),
  HOME(36, "Home"),
  LEFT(37, "Left"),
  UP(38, "Up"),
  RIGHT(39, "Right"),
  DOWN(40, "Down"),
  //
  COMMA(44, "Comma"),
  HYPHEN(45, "Hyphen"),
  PERIOD(46, "Period"),
  FORWARD_SLASH(47, "Forward Slash"),
  ZERO(48, "0"),
  ONE(49, "1"),
  TWO(50, "2"),
  THREE(51, "3"),
  FOUR(52, "4"),
  FIVE(53, "5"),
  SIX(54, "6"),
  SEVEN(55, "7"),
  EIGHT(56, "8"),
  NINE(57, "9"),
  //
  SEMI_COLON(59, "Semi Colon"),
  //
  EQUALS(61, "Equals"),
  //
  A(65, "A"),
  B(66, "B"),
  C(67, "C"),
  D(68, "D"),
  E(69, "E"),
  F(70, "F"),
  G(71, "G"),
  H(72, "H"),
  I(73, "I"),
  J(74, "J"),
  K(75, "K"),
  L(76, "L"),
  M(77, "M"),
  N(78, "N"),
  O(79, "O"),
  P(80, "P"),
  Q(81, "Q"),
  R(82, "R"),
  S(83, "S"),
  T(84, "T"),
  U(85, "U"),
  V(86, "V"),
  W(87, "W"),
  X(88, "X"),
  Y(89, "Y"),
  Z(90, "Z"),
  LEFT_BRACKET(91, "Left Bracket"),
  BACK_SLASH(92, "Back Slash"),
  RIGHT_BRACKET(93, "Right Bracket"),
  //
  NUM_ZERO(96, "Num 0"),
  NUM_ONE(97, "Num 1"),
  NUM_TWO(98, "Num 2"),
  NUM_THREE(99, "Num 3"),
  NUM_FOUR(100, "Num 4"),
  NUM_FIVE(101, "Num 5"),
  NUM_SIX(102, "Num 6"),
  NUM_SEVEN(103, "Num 7"),
  NUM_EIGHT(104, "Num 8"),
  NUM_NINE(105, "Num 9"),
  NUM_MULTIPLY(106, "Multiply"),
  NUM_PLUS(107, "Plus"),
  //
  NUM_MINUS(109, "Minus"),
  NUM_POINT(110, "Point"),
  NUM_DIVIDE(111, "Divide"),
  F1(112, "F1"),
  F2(113, "F2"),
  F3(114, "F3"),
  F4(115, "F4"),
  F5(116, "F5"),
  F6(117, "F6"),
  F7(118, "F7"),
  F8(119, "F8"),
  F9(120, "F9"),
  F10(121, "F10"),
  F11(122, "F11"),
  F12(123, "F12"),
  //
  DELETE(127, "Delete"),
  //
  NUM_LOCK(144, "Num Lock"),
  //
  INSERT(155, "Insert"),
  //
  BACK_QUOTE(192, "Back Quote"),
  //
  SINGLE_QUOTE(222, "Single Quote"),
  ;

  private final int code;
  private final String name;

  Key(int code, String name) {
    this.code = code;
    this.name = name;
  }

  public int getCode() {
    return code;
  }

  public String getName() {
    return name;
  }
}
