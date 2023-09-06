package technology.sola.engine.graphics.guiv2.style;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class StyleContainer<Style extends BaseStyles> {
  private final Map<Function<Style, ?>, Object> computedCache = new HashMap<>();
  private List<Style> styles;

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

  @SuppressWarnings("unchecked")
  public <R> R getPropertyValue(Function<Style, R> propertySupplier) {
    if (computedCache.containsKey(propertySupplier)) {
      return (R) computedCache.get(propertySupplier);
    }

    R value = null;

    for (Style style : styles) {
      R tempValue = propertySupplier.apply(style);

      if (tempValue != null) {
        value = tempValue;
      }
    }

    computedCache.put(propertySupplier, value);

    return value;
  }
}
