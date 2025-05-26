package technology.sola.engine.graphics.gui.style.property;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * StyleValue holds a numeric value that can be represented as an absolute value or a relative percentage value.
 */
@NullMarked
public class StyleValue {
  /**
   * StyleValue constant for the value of 0.
   */
  public static final StyleValue ZERO = new StyleValue(0);
  /**
   * StyleValue constant for the value of 100%.
   */
  public static final StyleValue FULL = new StyleValue("100%");

  @Nullable
  private Integer pixelValue;
  @Nullable
  private Float percentageValue;

  /**
   * Creates a StyleValue that is an absolute pixel value.
   *
   * @param pixelValue the pixel value
   */
  public StyleValue(int pixelValue) {
    this.pixelValue = pixelValue;
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
    } else {
      this.pixelValue = Integer.parseInt(value.replace("px", ""));
    }
  }

  /**
   * Creates a StyleValue from the parsed String. Returns null if value is null.
   *
   * @param value the String to parse
   * @return the StyleValue or null
   */
  public static @Nullable StyleValue of(@Nullable String value) {
    return value == null ? null : new StyleValue(value);
  }

  /**
   * Gets the resolved value based on a parent's dimension.
   *
   * @param parentValue the parent's value to be relative to if percentage
   * @return the resolved value
   */
  public int getValue(int parentValue) {
    return isPercentage() ? (int) (percentageValue * parentValue) : pixelValue;
  }

  /**
   * @return true if this StyleValue is a relative percentage value
   */
  public boolean isPercentage() {
    return percentageValue != null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    StyleValue that = (StyleValue) o;

    if (isPercentage() == that.isPercentage()) {
      return isPercentage() ? Objects.equals(percentageValue, that.percentageValue) : Objects.equals(pixelValue, that.pixelValue);
    }

    return false;
  }

  @Override
  public int hashCode() {
    if (isPercentage()) {
      return (percentageValue != null ? percentageValue.hashCode() : 0);
    } else {
      return (pixelValue != null ? pixelValue.hashCode() : 0);
    }
  }
}
