package technology.sola.engine.graphics.guiv2.style.property;

public record Padding(StyleValue top, StyleValue right, StyleValue bottom, StyleValue left) implements MergeableProperty<Padding> {
  public static final Padding NONE = new Padding("0", "0", "0", "0");

  public Padding() {
    this((String) null, null, null, null);
  }

  public Padding(String top, String right, String bottom, String left) {
    this(StyleValue.of(top), StyleValue.of(right), StyleValue.of(bottom), StyleValue.of(left));
  }

  public Padding(StyleValue top, StyleValue leftAndRight, StyleValue bottom) {
    this(top, leftAndRight, bottom, leftAndRight);
  }

  public Padding(String top, String leftAndRight, String bottom) {
    this(StyleValue.of(top), StyleValue.of(leftAndRight), StyleValue.of(bottom), StyleValue.of(leftAndRight));
  }

  public Padding(StyleValue topAndBottom, StyleValue leftAndRight) {
    this(topAndBottom, leftAndRight, topAndBottom, leftAndRight);
  }

  public Padding(String topBottom, String leftAndRight) {
    this(StyleValue.of(topBottom), StyleValue.of(leftAndRight), StyleValue.of(topBottom), StyleValue.of(leftAndRight));
  }

  public Padding(String size) {
    this(size, size);
  }

  @Override
  public Padding mergeWith(Padding otherProperty) {
    if (otherProperty == null) {
      return this;
    }

    StyleValue top = otherProperty.top == null ? this.top : otherProperty.top;
    StyleValue right = otherProperty.right == null ? this.right : otherProperty.right;
    StyleValue bottom = otherProperty.bottom == null ? this.bottom : otherProperty.bottom;
    StyleValue left = otherProperty.left == null ? this.left : otherProperty.left;

    return new Padding(top, right, bottom, left);
  }
}
