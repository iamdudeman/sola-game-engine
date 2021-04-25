package technology.sola.engine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a field of needing a {@link technology.sola.solkana.core.engine.service.SolKanaService} injection.
 * <p>
 * Currently applies to fields inside of classes implementing these interfaces
 * <ul>
 *   <li>{@link technology.sola.solkana.core.engine.service.gui.SolKanaGuiController}
 *   <li>{@link technology.sola.solkana.core.engine.service.SolKanaService}
 *   <li>{@link technology.sola.solkana.core.ecs.AbstractUpdateSystem}
 *   <li>{@link technology.sola.solkana.core.ecs.AbstractRenderSystem}
 *   <li>{@link technology.sola.solkana.core.engine.service.scene.AbstractSolKanaScene}</li>
 * </ul>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectSolKanaService {
}
