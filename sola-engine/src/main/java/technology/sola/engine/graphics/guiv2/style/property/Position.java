package technology.sola.engine.graphics.guiv2.style.property;

public record Position(StyleValue top, StyleValue right, StyleValue bottom, StyleValue left) implements MergeableProperty<Position> {
  public static final Position NONE = new Position();

  public Position() {
    this((String) null, null, null, null);
  }

  public Position(String top, String right, String bottom, String left) {
    this(StyleValue.of(top), StyleValue.of(right), StyleValue.of(bottom), StyleValue.of(left));
  }

  public static Position top(String value) {
    return new Position(value, null, null, null);
  }

  public static Position right(String value) {
    return new Position(null, value, null, null);
  }

  public static Position bottom(String value) {
    return new Position(null, null, value, null);
  }

  public static Position left(String value) {
    return new Position(null, null, null, value);
  }

  public static Position topLeft(String top, String left) {
    return new Position(top, null, null, left);
  }

  public static Position topRight(String top, String right) {
    return new Position(top, right, null, null);
  }

  public static Position bottomLeft(String bottom, String left) {
    return new Position(null, null, bottom, left);
  }

  public static Position bottomRight(String bottom, String right) {
    return new Position(null, right, bottom, null);
  }

  public boolean isAbsolute() {
    return top != null || right != null || bottom != null || left != null;
  }

  @Override
  public Position mergeWith(Position otherProperty) {
    if (otherProperty == null) {
      return this;
    }

    StyleValue top = otherProperty.top == null ? this.top : otherProperty.top;
    StyleValue right = otherProperty.right == null ? this.right : otherProperty.right;
    StyleValue bottom = otherProperty.bottom == null ? this.bottom : otherProperty.bottom;
    StyleValue left = otherProperty.left == null ? this.left : otherProperty.left;

    return new Position(top, right, bottom, left);
  }
}
