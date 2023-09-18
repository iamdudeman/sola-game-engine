package technology.sola.engine.graphics.guiv2.style.property.layout;

import technology.sola.engine.graphics.guiv2.style.property.MergeableProperty;

public interface LayoutItem<Info> extends MergeableProperty<LayoutItem<Info>> {
  Info info();
}
