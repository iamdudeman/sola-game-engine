package technology.sola.engine.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Annotates something purely for documenting that it should not be null.
 */
@Documented
@Retention(value = CLASS)
@Target(value = {FIELD, METHOD, PARAMETER, LOCAL_VARIABLE})
public @interface NotNull {
}
