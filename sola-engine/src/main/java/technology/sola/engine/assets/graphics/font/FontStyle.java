package technology.sola.engine.assets.graphics.font;

public enum FontStyle {
  NORMAL(0),
  ITALIC(1),
  BOLD(2),
  BOLD_ITALIC(3),
  ;

  private final int code;

  FontStyle(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
