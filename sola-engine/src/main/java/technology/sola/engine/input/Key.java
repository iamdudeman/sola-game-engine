package technology.sola.engine.input;

public enum Key {
  ESCAPE(0x1B, "Escape"),
  //
  SPACE(0x20, "Space"),
  //
  ZERO(0x30, "0"),
  ONE(0x31, "1"),
  TWO(0x32, "2"),
  THREE(0x33, "3"),
  //
  A(0x41, "A"),
  B(0x42, "B"),
  C(0x43, "C"),
  D(0x44, "D"),
  E(0x45, "E"),
  F(0x46, "F"),
  G(0x47, "G"),
  H(0x48, "H"),
  //
  S(0x53, "S"),
  //
  W(0x57, "W"),
  X(0x58, "X"),
  Y(0x59, "Y"),
  Z(0x5A, "Z")
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