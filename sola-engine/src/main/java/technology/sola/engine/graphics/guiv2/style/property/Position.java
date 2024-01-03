package technology.sola.engine.graphics.guiv2.style.property;

public record Position(StyleValue x, StyleValue y) implements MergeableProperty<Position> {
  public static final Position NONE = new Position();

  public Position() {
    this((String) null, null);
  }

  public Position(String x, String y) {
    this(StyleValue.of(x), StyleValue.of(y));
  }

  public static Position x(String value) {
    return new Position(value, null);
  }

  public static Position y(String value) {
    return new Position(null, value);
  }

  public boolean isAbsolute() {
    return x != null || y != null;
  }

  @Override
  public Position mergeWith(Position otherProperty) {
    if (otherProperty == null) {
      return this;
    }

    StyleValue x = otherProperty.x == null ? this.x : otherProperty.x;
    StyleValue y = otherProperty.y == null ? this.y : otherProperty.y;

    return new Position(x, y);
  }
}
