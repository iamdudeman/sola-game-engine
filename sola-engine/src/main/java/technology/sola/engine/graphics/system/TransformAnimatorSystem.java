package technology.sola.engine.graphics.system;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.animation.TransformAnimatorComponent;

/**
 * TransformAnimatorSystem handles updating the animation state of {@link technology.sola.ecs.Entity} that have a
 * {@link TransformComponent} and {@link TransformAnimatorComponent}.
 */
@NullMarked
public class TransformAnimatorSystem extends EcsSystem {
  @Override
  public void update(World world, float deltaTime) {
    for (var entry : world.createView().of(TransformComponent.class, TransformAnimatorComponent.class).getEntries()) {
      TransformComponent transformComponent = entry.c1();
      TransformAnimatorComponent transformAnimatorComponent = entry.c2();

      transformAnimatorComponent.tickAnimation(transformComponent, deltaTime);
    }
  }
}
