package technology.sola.engine.graphics.guiv2.style.property;

import technology.sola.engine.graphics.Color;

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
