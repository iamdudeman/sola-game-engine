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
    // todo should this also invalidate layout???
    boolean hasChanged = false;

    for (int i = 0; i < this.conditionalStyles.size(); i++) {
      if (conditionsArray[i] != conditionalStyles.get(i).condition().apply(guiElement)) {
        hasChanged = true;
        break;
      }
    }

    if (hasChanged) {
      computedCache.clear();

      for (int i = 0; i < this.conditionalStyles.size(); i++) {
        conditionsArray[i] = conditionalStyles.get(i).condition().apply(guiElement);
      }
    }
  }

  public final void setStyles(List<ConditionalStyle<Style>> styles) {
    computedCache.clear();

    this.conditionalStyles = styles == null ? new ArrayList<>() : styles;
    conditionsArray = styles == null ? new boolean[0] : new boolean[styles.size()];
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
