package technology.sola.engine.graphics;

import java.util.Objects;

public class Color {
  public static final Color BLACK = new Color(255, 0, 0, 0);
  public static final Color BLANK = new Color(0, 0, 0, 0);
  public static final Color BLUE = new Color(255, 0, 0, 255);
  public static final Color GREEN = new Color(255, 0, 255, 0);
  public static final Color RED = new Color(255, 255, 0, 0);
  public static final Color WHITE = new Color(255, 255, 255, 255);

  private final int alpha;
  private final int r;
  private final int g;
  private final int b;
  private final int hexInt;

  public Color(int r, int g, int b) {
    this(255, r, g, b);
  }

  public Color(int alpha, int r, int g, int b) {
    this.alpha = alpha;
    this.r = r;
    this.g = g;
    this.b = b;
    this.hexInt = ((0xff & alpha) << 24) | ((0xff & r) << 16) | ((0xff & g) << 8) | (0xff & b);
  }

  public Color(int argb) {
    alpha = (argb >> 24) & 0xFF;
    r = (argb >> 16) & 0xFF;
    g = (argb >> 8) & 0xFF;
    b = argb & 0xFF;
    this.hexInt = argb;
  }

  public int hexInt() {
    return hexInt;
  }

  public int getAlpha() {
    return alpha;
  }

  public int getRed() {
    return r;
  }

  public int getGreen() {
    return g;
  }

  public int getBlue() {
    return b;
  }

  public boolean hasAlpha() {
    return alpha != 255;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Color color = (Color) o;
    return hexInt == color.hexInt;
  }

  @Override
  public int hashCode() {
    return Objects.hash(hexInt);
  }
}
