package technology.sola.engine.core.module;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * SolaModule annotation is used to mark a class as a Sola Module which are responsible for configuring larger chunks
 * of functionality with default values. This can include defaults physics settings, rendering behavior and more.
 */
@Documented
@Target(ElementType.TYPE)
public @interface SolaModule {
}
