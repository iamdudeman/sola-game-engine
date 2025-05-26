package technology.sola.engine.graphics.gui.style.property;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.graphics.Color;

/**
 * Background contains the properties for a {@link technology.sola.engine.graphics.gui.GuiElement}'s background.
 *
 * @param color the {@link Color} of the element's background
 */
@NullMarked
public record Background(@Nullable Color color) implements MergeableProperty<Background> {
  @Override
  public Background mergeWith(@Nullable Background otherProperty) {
    if (otherProperty == null) {
      return this;
    }

    if (otherProperty.color != null) {
      return otherProperty;
    }

    return this;
  }
}
