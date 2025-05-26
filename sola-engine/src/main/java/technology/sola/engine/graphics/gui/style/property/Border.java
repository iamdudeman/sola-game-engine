package technology.sola.engine.graphics.gui.style.property;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.graphics.Color;

/**
 * Border contains the properties for a {@link technology.sola.engine.graphics.gui.GuiElement}'s border.
 *
 * @param color  the {@link Color} of the border
 * @param top    the width of the top part of the border
 * @param left   the width of the left part of the border
 * @param bottom the width of the bottom part of the border
 * @param right  the width of the right part of the border
 */
@NullMarked
public record Border(
  @Nullable Color color,
  @Nullable Integer top,
  @Nullable Integer left,
  @Nullable Integer bottom,
  @Nullable Integer right
) implements MergeableProperty<Border> {
  /**
   * Border with 0 width.
   */
  public static final Border NONE = new Border(0, Color.BLACK);

  /**
   * Creates a border with all sides the same size.
   *
   * @param size  the size of the border on all sides
   * @param color the {@link Color} of the border
   */
  public Border(int size, Color color) {
    this(color, size, size, size, size);
  }

  @Override
  public Border mergeWith(@Nullable Border otherProperty) {
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
