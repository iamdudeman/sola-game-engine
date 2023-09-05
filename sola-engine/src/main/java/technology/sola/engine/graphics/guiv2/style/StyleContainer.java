package technology.sola.engine.graphics.guiv2.style;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class StyleContainer {
  private final Style[] styles;
  private final Map<Function<?, ?>, Object> computedCache = new HashMap<>();

  public StyleContainer(Style... styles) {
    if (styles == null) {
      this.styles = new Style[]{};
    } else {
      this.styles = styles;
    }
  }

  public void setStyleActive(String id, boolean isActive) {
    for (Style style : styles) {
      if (style.styleId.equals(id)) {
        if (style.isActive != isActive) {
          style.isActive = isActive;
          computedCache.clear();
        }

        break;
      }
    }
  }

  // todo need a way to combine style methods of like types
  // todo verify if this works for two different types (ie. Style#getBackgroundColor and TextStyle#getBackgroundColor)
  public <T extends Style, R> R getPropertyValue(Class<T> styleClass, Function<T, R> propertySupplier) {
    if (computedCache.containsKey(propertySupplier)) {
      return (R) computedCache.get(propertySupplier);
    }

    R value = null;

    for (Style style : styles) {
      if (!style.isActive()) {
        continue;
      }

      if (styleClass.isInstance(style)) {
        value = propertySupplier.apply((T) style);
      }
    }

    computedCache.put(propertySupplier, value);

    return value;
  }
}
