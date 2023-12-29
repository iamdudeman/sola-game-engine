package technology.sola.engine.graphics.guiv2.style;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.style.property.MergeableProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

// todo consider instead of array of styles an array of conditional styles where function is determiner of it being active
//   or not, guiElement -> guiElement.isHover() for example, styles merge from top to bottom
//   can probably cache the result until invalidated by the owning element

public class StyleContainer<Style extends BaseStyles> {
  private final Map<Function<Style, ?>, Object> computedCache = new HashMap<>();
  private final GuiElement<Style> guiElement;
  private List<ConditionalStyle<Style>> styles;

  public StyleContainer(GuiElement<Style> guiElement) {
    this.guiElement = guiElement;
    styles = new ArrayList<>();
  }

  @SafeVarargs
  public final void setStyles(Style... styles) {
    computedCache.clear();

    if (styles == null) {
      this.styles = new ArrayList<>();
    } else {
      this.styles = Stream.of(styles).map(ConditionalStyle::always).toList();
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

    for (var style : styles) {
      if (style.condition().apply(guiElement)) {
        R tempValue = propertySupplier.apply(style.style());

        if (tempValue != null) {
          value = tempValue;
        }
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

    for (var style : styles) {
      if (style.condition().apply(guiElement)) {
        R tempValue = propertySupplier.apply(style.style());

        if (value == null) {
          value = tempValue;
        } else {
          value = value.mergeWith(tempValue);
        }
      }
    }

    computedCache.put(propertySupplier, value);

    return value;
  }
}
