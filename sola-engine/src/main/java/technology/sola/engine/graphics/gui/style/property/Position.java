package technology.sola.engine.graphics.gui.style.property;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Position contains the properties for absolutely positioning a {@link technology.sola.engine.graphics.gui.GuiElement}
 * within its container. If x and y are both null then the element is not considered absolutely positioned.
 *
 * @param x the x position of the element
 * @param y the y position of the element
 */
@NullMarked
public record Position(
  @Nullable StyleValue x,
  @Nullable StyleValue y
) implements MergeableProperty<Position> {
  /**
   * Position with x and y not set. Element will be relatively positioned in the flow.
   */
  public static final Position NONE = new Position();

  /**
   * Creates a new Position instance with x and y not set. Element will be relatively positioned in the flow.
   */
  public Position() {
    this((String) null, null);
  }

  /**
   * Creates a new Position instance with x and y set.
   *
   * @param x the x position of the element
   * @param y the y position of the element
   */
  public Position(@Nullable String x, @Nullable String y) {
    this(StyleValue.of(x), StyleValue.of(y));
  }

  /**
   * Checks if this Position is absolute or not. Position is considered absolute if x or y is not null.
   *
   * @return true if absolute
   */
  public boolean isAbsolute() {
    return x != null || y != null;
  }

  @Override
  public Position mergeWith(@Nullable Position otherProperty) {
    if (otherProperty == null) {
      return this;
    }

    StyleValue x = otherProperty.x == null ? this.x : otherProperty.x;
    StyleValue y = otherProperty.y == null ? this.y : otherProperty.y;

    return new Position(x, y);
  }
}
