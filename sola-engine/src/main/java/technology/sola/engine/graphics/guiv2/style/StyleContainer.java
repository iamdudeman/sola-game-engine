package technology.sola.engine.graphics.guiv2.style;

import technology.sola.engine.graphics.guiv2.style.property.MergeableProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class StyleContainer<Style extends BaseStyles> {
  private final Map<Function<Style, ?>, Object> computedCache = new HashMap<>();
  private List<Style> styles;

  public StyleContainer() {
    styles = new ArrayList<>();
  }

  @SafeVarargs
  public StyleContainer(Style... styles) {
    setStyles(styles);
  }

  @SafeVarargs
  public final void setStyles(Style... styles) {
    computedCache.clear();

    if (styles == null) {
      this.styles = new ArrayList<>();
    } else {
      this.styles = List.of(styles);
    }
  }

  @SafeVarargs
  @SuppressWarnings("unchecked")
  public final <StyleBuilder extends BaseStyles.Builder<?>> void setStyles(StyleBuilder... styleBuilders) {
    computedCache.clear();

    if (styles == null) {
      this.styles = new ArrayList<>();
    } else {
      this.styles = (List<Style>) Stream.of(styleBuilders).map(builder -> builder.build()).toList();
    }
  }

  public <R> R getPropertyValue(Function<Style, R> propertySupplier) {
    return getPropertyValue(propertySupplier, null);
  }

  @SuppressWarnings("unchecked")
  public <R> R getPropertyValue(Function<Style, R> propertySupplier, R defaultValue) {
    if (computedCache.containsKey(propertySupplier)) {
      return (R) computedCache.get(propertySupplier);
    }

    R value = defaultValue;

    for (Style style : styles) {
      R tempValue = propertySupplier.apply(style);

      if (tempValue != null) {
        value = tempValue;
      }
    }

    computedCache.put(propertySupplier, value);

    return value;
  }

  @SuppressWarnings("unchecked")
  public <R extends MergeableProperty<R>> R getPropertyValue(Function<Style, R> propertySupplier, R defaultValue) {
    if (computedCache.containsKey(propertySupplier)) {
      return (R) computedCache.get(propertySupplier);
    }

    R value = defaultValue;

    for (Style style : styles) {
      R tempValue = propertySupplier.apply(style);

      if (value == null) {
        value = tempValue;
      } else {
        value = value.mergeWith(tempValue);
      }
    }

    computedCache.put(propertySupplier, value);

    return value;
  }
}
