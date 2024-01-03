package technology.sola.engine.graphics.guiv2.style.property;

public interface MergeableProperty<P extends MergeableProperty<?>> {
  P mergeWith(P otherProperty);
}
