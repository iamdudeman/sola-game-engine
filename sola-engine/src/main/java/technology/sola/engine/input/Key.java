package technology.sola.engine.input;

public enum Key {
  ENTER(10, "Enter"),
  //
  CARRIAGE_RETURN(13, "Carriage Return"),
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
  ZERO(48, "0"),
  ONE(49, "1"),
  TWO(50, "2"),
  THREE(51, "3"),
  //
  A(65, "A"),
  B(66, "B"),
  C(67, "C"),
  D(68, "D"),
  E(69, "E"),
  F(70, "F"),
  G(71, "G"),
  H(72, "H"),
  //
  S(83, "S"),
  //
  W(87, "W"),
  X(88, "X"),
  Y(89, "Y"),
  Z(90, "Z"),
  //
  NUM_ZERO(96, "Num 0"),
  NUM_ONE(97, "Num 1"),
  //
  F11(122, "F11"),
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
