package technology.sola.engine.graphics.guiv2.style.property;

public record Padding(Integer top, Integer right, Integer bottom, Integer left) implements MergeableProperty<Padding> {
  public static final Padding NONE = new Padding(0, 0, 0, 0);

  public Padding() {
    this(null, null, null, null);
  }

  public Padding(Integer top, Integer leftAndRight, Integer bottom) {
    this(top, leftAndRight, bottom, leftAndRight);
  }

  public Padding(Integer topAndBottom, Integer leftAndRight) {
    this(topAndBottom, leftAndRight, topAndBottom, leftAndRight);
  }

  public Padding(Integer size) {
    this(size, size);
  }

  @Override
  public Padding mergeWith(Padding otherProperty) {
    if (otherProperty == null) {
      return this;
    }

    Integer top = otherProperty.top == null ? this.top : otherProperty.top;
    Integer right = otherProperty.right == null ? this.right : otherProperty.right;
    Integer bottom = otherProperty.bottom == null ? this.bottom : otherProperty.bottom;
    Integer left = otherProperty.left == null ? this.left : otherProperty.left;

    return new Padding(top, right, bottom, left);
  }
}
