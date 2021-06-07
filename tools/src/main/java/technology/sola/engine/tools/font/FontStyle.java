package technology.sola.engine.tools.font;

public enum FontStyle {
  NORMAL(0),
  BOLD(1),
  ITALIC(2),
  ;

  private final int code;

  FontStyle(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
