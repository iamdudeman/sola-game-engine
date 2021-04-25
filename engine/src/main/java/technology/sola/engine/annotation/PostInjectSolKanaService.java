package technology.sola.engine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a method of needing to be called after {@link technology.sola.solkana.core.engine.service.SolKanaService} injection completes.
 * <p>
 * Note: This method will only be called if the class has a field annotated with {@link InjectSolKanaService}.
 * <p>
 * Currently applies to methods inside of classes implementing these interfaces
 * <ul>
 *   <li>{@link technology.sola.solkana.core.engine.service.gui.SolKanaGuiController}
 *   <li>{@link technology.sola.solkana.core.engine.service.SolKanaService}
 *   <li>{@link technology.sola.solkana.core.ecs.AbstractUpdateSystem}
 *   <li>{@link technology.sola.solkana.core.ecs.AbstractRenderSystem}
 *   <li>{@link technology.sola.solkana.core.engine.service.scene.AbstractSolKanaScene}</li>
 * </ul>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostInjectSolKanaService {
}
