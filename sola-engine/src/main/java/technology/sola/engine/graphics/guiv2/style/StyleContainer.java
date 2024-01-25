package technology.sola.engine.graphics.guiv2.style;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.style.property.MergeableProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class StyleContainer<Style extends BaseStyles> {
  private final Map<Function<Style, ?>, Object> computedCache = new HashMap<>();
  private final GuiElement<Style> guiElement;
  private List<ConditionalStyle<Style>> conditionalStyles;
  private boolean[] conditionsArray;

  public StyleContainer(GuiElement<Style> guiElement) {
    this.guiElement = guiElement;
    conditionalStyles = new ArrayList<>();
    conditionsArray = new boolean[0];
  }

  public void invalidate() {
    boolean hasChanged = false;

    for (int i = 0; i < this.conditionalStyles.size(); i++) {
      boolean result = conditionalStyles.get(i).condition().apply(guiElement);

      if (conditionsArray[i] != result) {
        conditionsArray[i] = result;
        hasChanged = true;
      }
    }

    if (hasChanged) {
      computedCache.clear();
      // todo keep an eye on this for if this is a performance issue
      guiElement.invalidateLayout();
    }
  }

  public final void setStyles(List<ConditionalStyle<Style>> styles) {
    this.conditionalStyles = styles == null ? new ArrayList<>() : styles;
    conditionsArray = styles == null ? new boolean[0] : new boolean[styles.size()];
    computedCache.clear();
  }

  public void addStyle(ConditionalStyle<Style> style) {
    List<ConditionalStyle<Style>> combined = new ArrayList<>(conditionalStyles);

    combined.add(style);
    setStyles(combined);
  }

  public void removeStyle(ConditionalStyle<Style> style) {
    List<ConditionalStyle<Style>> reduced = new ArrayList<>(conditionalStyles);

    reduced.remove(style);
    setStyles(reduced);
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

    for (var conditionalStyle : conditionalStyles) {
      if (conditionalStyle.condition().apply(guiElement)) {
        R tempValue = propertySupplier.apply(conditionalStyle.style());

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

    for (var conditionalStyle : conditionalStyles) {
      if (conditionalStyle.condition().apply(guiElement)) {
        R tempValue = propertySupplier.apply(conditionalStyle.style());

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
