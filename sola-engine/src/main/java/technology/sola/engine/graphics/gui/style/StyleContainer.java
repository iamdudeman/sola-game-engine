package technology.sola.engine.graphics.gui.style;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.style.property.MergeableProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * StyleContainer is a container for {@link ConditionalStyle}s that apply to a {@link GuiElement}.
 *
 * @param <Style> the {@link BaseStyles} type
 */
@NullMarked
public class StyleContainer<Style extends BaseStyles> {
  private final Map<Function<Style, ?>, @Nullable Object> computedCache = new HashMap<>();
  private final GuiElement<Style, ?> guiElement;
  private List<ConditionalStyle<Style>> conditionalStyles;
  private boolean[] conditionsArray;
  private boolean hasHoverCondition = false;
  private boolean hasActiveCondition = false;

  /**
   * Creates a StyleContainer instance for a {@link GuiElement}.
   *
   * @param guiElement the gui element
   */
  public StyleContainer(GuiElement<Style, ?> guiElement) {
    this.guiElement = guiElement;
    conditionalStyles = new ArrayList<>();
    conditionsArray = new boolean[0];
  }

  /**
   * Invalidates the cached condition evaluations if a change has happened.
   */
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

  /**
   * Replaces the current internal {@link List} of {@link ConditionalStyle}s with a new one.
   *
   * @param styles the new list of styles
   */
  public final void setStyles(List<ConditionalStyle<Style>> styles) {
    this.conditionalStyles = styles;
    conditionsArray = new boolean[styles.size()];
    computedCache.clear();
    recalculateConditions();
    guiElement.invalidateLayout();
  }

  /**
   * Appends another {@link ConditionalStyle} to the current list of styles.
   *
   * @param style the new style to add
   */
  public void addStyle(ConditionalStyle<Style> style) {
    List<ConditionalStyle<Style>> combined = new ArrayList<>(conditionalStyles);

    combined.add(style);
    setStyles(combined);
  }

  /**
   * Appends a {@link List} of {@link ConditionalStyle}s to the current list of styles.
   *
   * @param styles the styles to add
   */
  public void addStyles(List<ConditionalStyle<Style>> styles) {
    addStyles(styles, false);
  }

  /**
   * Appends or prepends a {@link List} of {@link ConditionalStyle}s to the current list of styles.
   *
   * @param styles  the styles to add
   * @param prepend whether to prepend the styles or not
   */
  public void addStyles(List<ConditionalStyle<Style>> styles, boolean prepend) {
    var firstList = prepend ? styles : conditionalStyles;
    var secondList = prepend ? conditionalStyles : styles;

    List<ConditionalStyle<Style>> combined = new ArrayList<>(firstList);

    combined.addAll(secondList);
    setStyles(combined);
  }

  /**
   * Removes all instances of {@link ConditionalStyle} from the current list of styles.
   *
   * @param style the style to remove
   */
  public void removeStyle(ConditionalStyle<Style> style) {
    List<ConditionalStyle<Style>> reduced = conditionalStyles.stream()
      .filter(existing -> !existing.equals(style))
      .toList();

    setStyles(reduced);
  }

  /**
   * Gets the computer property value based on the {@link ConditionalStyle}s currently applied to the {@link GuiElement}.
   * If no value is present in any of the styles then null is returned.
   *
   * @param propertySupplier the method that supplies the property from {@link BaseStyles} classes
   * @param <R>              the return value type
   * @return the calculated style or null
   */
  public <R> @Nullable R getPropertyValue(Function<Style, @Nullable R> propertySupplier) {
    return getPropertyValue(propertySupplier, null);
  }

  /**
   * Gets the computer property value based on the {@link ConditionalStyle}s currently applied to the {@link GuiElement}.
   * If no value is present in any of the styles then the default value is returned.
   *
   * @param propertySupplier the method that supplies the property from {@link BaseStyles} classes
   * @param defaultValue     the default value to return if no style provides a value
   * @param <R>              the return value type
   * @return the calculated style or default value
   */
  @SuppressWarnings("unchecked")
  public <R> @Nullable R getPropertyValue(Function<Style, @Nullable R> propertySupplier, @Nullable R defaultValue) {
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

  /**
   * Gets the computer property value based on the {@link ConditionalStyle}s currently applied to the {@link GuiElement}.
   * If no value is present in any of the styles then the default value is returned. Properties defined in multiple
   * styles will be merged based on their {@link MergeableProperty#mergeWith(MergeableProperty)} definition.
   *
   * @param propertySupplier the method that supplies the property from {@link BaseStyles} classes
   * @param defaultValue     the default value to return if no style provides a value
   * @param <R>              the return value type
   * @return the calculated style or default value
   */
  @SuppressWarnings("unchecked")
  public <R extends MergeableProperty<R>> @Nullable R getPropertyValue(Function<Style, @Nullable R> propertySupplier, @Nullable R defaultValue) {
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

  /**
   * @return true if any of the styles contain a {@link GuiElement#isHovered()} condition
   */
  public boolean hasHoverCondition() {
    return hasHoverCondition;
  }

  /**
   * @return true if any of the styles contain a {@link GuiElement#isActive()} condition
   */
  public boolean hasActiveCondition() {
    return hasActiveCondition;
  }

  private void recalculateConditions() {
    for (var conditionalStyle : conditionalStyles) {
      if (conditionalStyle.isHoverStyle()) {
        hasHoverCondition = true;
      }
      if (conditionalStyle.isActiveStyle()) {
        hasActiveCondition = true;
      }
    }
  }
}
