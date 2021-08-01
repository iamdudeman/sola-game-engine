package technology.sola.engine.input;

public enum Key {
  SPACE(0x20, "Space"),

  ONE(0x31, "1"),
  TWO(0x32, "2"),
  THREE(0x33, "3"),

  A(0x41, "A"),
  B(0x42, "B"),
  C(0x43, "C"),
  D(0x44, "D"),
  S(0x53, "S"),
  W(0x57, "W"),
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
