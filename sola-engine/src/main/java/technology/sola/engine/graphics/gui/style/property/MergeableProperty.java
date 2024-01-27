package technology.sola.engine.graphics.gui.style.property;

/**
 * MergeableProperty defines the contract for a style property that can be merged with another like type property.
 *
 * @param <P> the type of data to merge with
 */
public interface MergeableProperty<P extends MergeableProperty<?>> {
  /**
   * Returns a new instance of the property with the resulting values being a merge of the right on top of the left.
   *
   * @param otherProperty the other property object to merge values on top of this one
   * @return a new instance with the merged properties
   */
  P mergeWith(P otherProperty);
}
