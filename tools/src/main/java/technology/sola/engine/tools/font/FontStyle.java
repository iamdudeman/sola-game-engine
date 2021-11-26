package technology.sola.engine.tools.font;

import java.awt.*;

enum FontStyle {
  NORMAL(Font.PLAIN),
  BOLD(Font.BOLD),
  ITALIC(Font.ITALIC),
  ;

  private final int code;

  FontStyle(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
