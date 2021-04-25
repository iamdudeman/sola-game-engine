package technology.sola.engine.graphics;

public class Color {
  public static final Color BLACK = new Color(255, 0, 0, 0);
  public static final Color BLANK = new Color(0, 0, 0, 0);
  public static final Color BLUE = new Color(0, 0, 0, 255);
  public static final Color GREEN = new Color(0, 0, 255, 0);
  public static final Color RED = new Color(0, 255, 0, 0);
  public static final Color WHITE = new Color(255, 255, 255, 255);

  private final int alpha;
  private final int red;
  private final int green;
  private final int blue;
  private final int hexInt;

  public Color(int red, int green, int blue) {
    this(255, red, green, blue);
  }

  public Color(int alpha, int red, int green, int blue) {
    this.alpha = alpha;
    this.red = red;
    this.green = green;
    this.blue = blue;
    this.hexInt = ((0xff & alpha) << 24) | ((0xff & red) << 16) | ((0xff & green) << 8) | (0xff & blue);
  }

  public int hexInt() {
    return hexInt;
  }

  public int getAlpha() {
    return alpha;
  }

  public int getRed() {
    return red;
  }

  public int getGreen() {
    return green;
  }

  public int getBlue() {
    return blue;
  }
}
