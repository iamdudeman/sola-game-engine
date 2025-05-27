package technology.sola.engine.graphics.gui.style.property;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Padding contains the properties for a {@link technology.sola.engine.graphics.gui.GuiElement}'s padding which is the
 * space between the element's border and content.
 *
 * @param top    the top padding for the element
 * @param left   the left padding for the element
 * @param bottom the bottom padding for the element
 * @param right  the right padding for the element
 */
@NullMarked
public record Padding(
  @Nullable Integer top,
  @Nullable Integer right,
  @Nullable Integer bottom,
  @Nullable Integer left
) implements MergeableProperty<Padding> {
  /**
   * Padding with all values set to 0.
   */
  public static final Padding NONE = new Padding(0, 0, 0, 0);

  /**
   * Creates a new Padding instance with no values set.
   */
  public Padding() {
    this(null, null, null, null);
  }

  /**
   * Creates a new Padding instance with top, horizontal, and bottom values set.
   *
   * @param top          the top padding for the element
   * @param leftAndRight the left and right padding for the element
   * @param bottom       the bottom padding for the element
   */
  public Padding(@Nullable Integer top, @Nullable Integer leftAndRight, @Nullable Integer bottom) {
    this(top, leftAndRight, bottom, leftAndRight);
  }

  /**
   * Creates a new Padding instance with vertical and horizontal values set.
   *
   * @param topAndBottom the top and bottom padding for the element
   * @param leftAndRight the left and right padding for the element
   */
  public Padding(@Nullable Integer topAndBottom, @Nullable Integer leftAndRight) {
    this(topAndBottom, leftAndRight, topAndBottom, leftAndRight);
  }

  /**
   * Creates a new Padding instance with all values set to the same.
   *
   * @param size the value for each side of the padding
   */
  public Padding(@Nullable Integer size) {
    this(size, size);
  }

  @Override
  public Padding mergeWith(@Nullable Padding otherProperty) {
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
