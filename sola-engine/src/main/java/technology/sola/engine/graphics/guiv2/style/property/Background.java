package technology.sola.engine.graphics.guiv2.style.property;

import technology.sola.engine.graphics.Color;

/**
 * Background contains the properties for a {@link technology.sola.engine.graphics.guiv2.GuiElement}'s background.
 *
 * @param color the {@link Color} of the element's background
 */
public record Background(Color color) implements MergeableProperty<Background> {
  @Override
  public Background mergeWith(Background otherProperty) {
    if (otherProperty == null) {
      return this;
    }

    if (otherProperty.color != null) {
      return otherProperty;
    }

    return this;
  }
}
