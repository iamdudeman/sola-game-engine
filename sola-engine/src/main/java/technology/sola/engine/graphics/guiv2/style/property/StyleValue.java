package technology.sola.engine.graphics.guiv2.style.property;

import java.util.Objects;

/**
 * StyleValue holds a numeric value that can be represented as an absolute value or a relative percentage value.
 */
public class StyleValue {
  /**
   * StyleValue constant for the value of 0.
   */
  public static final StyleValue ZERO = new StyleValue(0);
  /**
   * StyleValue constant for the value of 100%.
   */
  public static final StyleValue FULL = new StyleValue("100%");
  private final boolean isPercentage;
  private Integer pixelValue;
  private Float percentageValue;

  /**
   * Creates a StyleValue that is an absolute pixel value.
   *
   * @param pixelValue the pixel value
   */
  public StyleValue(int pixelValue) {
    this.pixelValue = pixelValue;
    isPercentage = false;
  }

  /**
   * Creates a StyleValue from a string that can be a pixel value or percentage.
   * <ul>
   *   <li>ends with '%' - percentage</li>
   *   <li>ends with 'px' - pixel</li>
   *   <li>default - pixel</li>
   * </ul>
   *
   * @param value the value to parse into a StyleValue
   */
  public StyleValue(String value) {
    if (value.endsWith("%")) {
      this.percentageValue = Float.parseFloat(value.replace("%", "")) / 100f;
      this.isPercentage = true;
    } else {
      this.pixelValue = Integer.parseInt(value.replace("px", ""));
      this.isPercentage = false;
    }
  }

  /**
   * Creates a StyleValue from the parsed String. Returns null if value is null
   *
   * @param value the String to parse
   * @return the StyleValue or null
   */
  public static StyleValue of(String value) {
    return value == null ? null : new StyleValue(value);
  }

  /**
   * Gets the resolved value based on a parent's dimension.
   *
   * @param parentValue the parent's value to be relative to if percentage
   * @return the resolved value
   */
  public int getValue(int parentValue) {
    return isPercentage ? (int) (percentageValue * parentValue) : pixelValue;
  }

  /**
   * @return true if this StyleValue is a relative percentage value
   */
  public boolean isPercentage() {
    return isPercentage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    StyleValue that = (StyleValue) o;

    if (isPercentage == that.isPercentage) {
      return isPercentage ? Objects.equals(percentageValue, that.percentageValue) : Objects.equals(pixelValue, that.pixelValue);
    }

    return false;
  }

  @Override
  public int hashCode() {
    if (isPercentage) {
      return (percentageValue != null ? percentageValue.hashCode() : 0);
    } else {
      return (pixelValue != null ? pixelValue.hashCode() : 0);
    }
  }
}
