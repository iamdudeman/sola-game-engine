package technology.sola.engine.tools.font.model;

import java.awt.*;

public enum FontStyle {
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
