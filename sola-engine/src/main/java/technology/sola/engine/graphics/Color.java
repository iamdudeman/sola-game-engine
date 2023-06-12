package technology.sola.engine.graphics;

import java.util.Objects;

/**
 * The Color class represents an argb color that can be used for rendering.
 */
public class Color {
  /**
   * Color constant - rgb(0, 0, 0).
   */
  public static final Color BLACK = new Color(0, 0, 0);
  /**
   * Color constant - argb(0, 0, 0, 0).
   */
  public static final Color BLANK = new Color(0, 0, 0, 0);
  /**
   * Color constant - rgb(0, 0, 255).
   */
  public static final Color BLUE = new Color(0, 0, 255);
  /**
   * Color constant - rgb(169, 169, 169).
   */
  public static final Color DARK_GRAY = new Color(169, 169, 169);
  /**
   * Color constant - rgb(0, 255, 0).
   */
  public static final Color GREEN = new Color(0, 255, 0);
  /**
   * Color constant - rgb(255, 165, 0).
   */
  public static final Color ORANGE = new Color(255, 165, 0);
  /**
   * Color constant - rgb(255, 0, 0).
   */
  public static final Color RED = new Color(255, 0, 0);
  /**
   * Color constant - rgb(173, 216, 230).
   */
  public static final Color LIGHT_BLUE = new Color(173, 216, 230);
  /**
   * Color constant - rgb(211, 211, 211)
   */
  public static final Color LIGHT_GRAY = new Color(211, 211, 211);
  /**
   * Color constant - rgb(255, 255, 0).
   */
  public static final Color YELLOW = new Color(255, 255, 0);
  /**
   * Color constant - rgb(255, 255, 255).
   */
  public static final Color WHITE = new Color(255, 255, 255);

  private final int alpha;
  private final int r;
  private final int g;
  private final int b;
  private final int hexInt;

  /**
   * Creates a Color instance via rgb values. Alpha is set to 255. Values should be set to between 0 and 255 inclusive.
   *
   * @param r red
   * @param g green
   * @param b blue
   */
  public Color(int r, int g, int b) {
    this(255, r, g, b);
  }

  /**
   * Creates a Color instance via argb values. Values should be set to between 0 and 255 inclusive.
   *
   * @param alpha the alpha
   * @param r     red
   * @param g     green
   * @param b     blue
   */
  public Color(int alpha, int r, int g, int b) {
    this.alpha = alpha;
    this.r = r;
    this.g = g;
    this.b = b;
    this.hexInt = ((0xff & alpha) << 24) | ((0xff & r) << 16) | ((0xff & g) << 8) | (0xff & b);
  }

  /**
   * Creates a color instance from a 4 byte integer. Order of bytes should be [alpha, red, green, blue].
   *
   * @param argb the 4 byte integer
   */
  public Color(int argb) {
    alpha = (argb >> 24) & 0xFF;
    r = (argb >> 16) & 0xFF;
    g = (argb >> 8) & 0xFF;
    b = argb & 0xFF;
    this.hexInt = argb;
  }

  /**
   * @return the color has a 4 byte hex integer
   */
  public int hexInt() {
    return hexInt;
  }

  /**
   * @return the alpha value
   */
  public int getAlpha() {
    return alpha;
  }

  /**
   * @return the red value
   */
  public int getRed() {
    return r;
  }

  /**
   * @return the green value
   */
  public int getGreen() {
    return g;
  }

  /**
   * @return the blue value
   */
  public int getBlue() {
    return b;
  }

  /**
   * @return true if alpha value is not 255
   */
  public boolean hasAlpha() {
    return alpha != 255;
  }

  /**
   * Creates a grey scale Color using this Color as the base.
   *
   * @return this color as greyscale
   */
  public Color greyScale() {
    int greyscale = (int) (0.2126f * r + 0.7152f * g + 0.0722f * b);

    return new Color(alpha, greyscale, greyscale, greyscale);
  }

  /**
   * Creates a new Color that is a shade of this Color.
   *
   * @param shadeFactor the shade factor from 0-1
   * @return the shaded Color
   */
  public Color shade(float shadeFactor) {
    int newR = (int) (r * (1 - shadeFactor));
    int newG = (int) (g * (1 - shadeFactor));
    int newB = (int) (b * (1 - shadeFactor));

    return new Color(newR, newG, newB);
  }

  /**
   * Creates a new Color that is a tint of this Color.
   *
   * @param tintFactor the tint factor from 0-1
   * @return the tinted Color
   */
  public Color tint(float tintFactor) {
    int newR = (int) (r + (255 - r) * tintFactor);
    int newG = (int) (g + (255 - g) * tintFactor);
    int newB = (int) (b + (255 - b) * tintFactor);

    return new Color(newR, newG, newB);
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

  @Override
  public String toString() {
    return String.format("rgba(%s, %s, %s, %s)", r, g, b, alpha);
  }
}
