package technology.sola.engine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a field for exclusion from serialization when the {@link technology.sola.solkana.core.asset.GsonExclusionStrategy}
 * is used.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonExclude {
}
