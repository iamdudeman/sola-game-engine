package technology.sola.engine.graphics.font;

public enum FontStyle {
  NORMAL(0),
  ITALIC(1),
  BOLD(2)
  ;

  private final int code;

  FontStyle(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
