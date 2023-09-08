package technology.sola.engine.graphics.guiv2.style.property;

public record Spacing(StyleValue top, StyleValue right, StyleValue bottom, StyleValue left) implements MergeableProperty<Spacing> {
  public static final Spacing NONE = new Spacing("0", "0", "0", "0");

  public Spacing() {
    this((String) null, null, null, null);
  }

  public Spacing(String top, String left, String bottom, String right) {
    this(StyleValue.of(top), StyleValue.of(left), StyleValue.of(bottom), StyleValue.of(right));
  }

  public Spacing(StyleValue top, StyleValue leftAndRight, StyleValue bottom) {
    this(top, leftAndRight, bottom, leftAndRight);
  }

  public Spacing(String top, String leftAndRight, String bottom) {
    this(StyleValue.of(top), StyleValue.of(leftAndRight), StyleValue.of(bottom), StyleValue.of(leftAndRight));
  }

  public Spacing(StyleValue topAndBottom, StyleValue leftAndRight) {
    this(topAndBottom, leftAndRight, topAndBottom, leftAndRight);
  }

  public Spacing(String topBottom, String leftAndRight) {
    this(StyleValue.of(topBottom), StyleValue.of(leftAndRight), StyleValue.of(topBottom), StyleValue.of(leftAndRight));
  }

  @Override
  public Spacing mergeWith(Spacing otherProperty) {
    if (otherProperty == null) {
      return this;
    }

    StyleValue top = otherProperty.top == null ? this.top : otherProperty.top;
    StyleValue right = otherProperty.right == null ? this.right : otherProperty.right;
    StyleValue bottom = otherProperty.bottom == null ? this.bottom : otherProperty.bottom;
    StyleValue left = otherProperty.left == null ? this.left : otherProperty.left;

    return new Spacing(top, right, bottom, left);
  }
}
