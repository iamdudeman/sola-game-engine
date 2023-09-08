package technology.sola.engine.graphics.guiv2.style.property;

import java.util.Objects;

public class StyleValue {
  public static final StyleValue AUTO = new StyleValue(-1);
  public static final StyleValue ZERO = new StyleValue(0);
  private final boolean isPercentage;
  private Integer pixelValue;
  private Float percentageValue;

  public StyleValue(int pixelValue) {
    this.pixelValue = pixelValue;
    isPercentage = false;
  }

  public StyleValue(String value) {
    if (value.endsWith("%")) {
      this.percentageValue = Float.parseFloat(value.replace("%", "")) / 100f;
      this.isPercentage = true;
    } else {
      this.pixelValue = Integer.parseInt(value.replace("px", ""));
      this.isPercentage = false;
    }
  }

  public static StyleValue of(String value) {
    return value == null ? null : new StyleValue(value);
  }

  public int getValue(int parentValue) {
    return isPercentage ? (int) (percentageValue * parentValue) : pixelValue;
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
