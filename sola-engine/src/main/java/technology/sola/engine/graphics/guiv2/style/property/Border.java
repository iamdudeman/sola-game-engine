package technology.sola.engine.graphics.guiv2.style.property;

import technology.sola.engine.graphics.Color;

public record Border(Color color, Integer top, Integer left, Integer bottom, Integer right) implements MergeableProperty<Border> {
  public static final Border NONE = new Border(0, Color.BLACK);

  public Border(int size, Color color) {
    this(color, size, size, size, size);
  }

  @Override
  public Border mergeWith(Border otherProperty) {
    if (otherProperty == null) {
      return this;
    }

    Integer top = otherProperty.top == null ? this.top : otherProperty.top;
    Integer left = otherProperty.left == null ? this.left : otherProperty.left;
    Integer bottom = otherProperty.bottom == null ? this.bottom : otherProperty.bottom;
    Integer right = otherProperty.right == null ? this.right : otherProperty.right;
    Color color = otherProperty.color == null ? this.color : otherProperty.color;

    return new Border(color, top, left, bottom, right);
  }
}
