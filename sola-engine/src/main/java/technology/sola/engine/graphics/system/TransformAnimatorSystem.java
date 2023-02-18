package technology.sola.engine.graphics.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.animation.TransformAnimatorComponent;

public class TransformAnimatorSystem extends EcsSystem {
  @Override
  public void update(World world, float deltaTime) {
    world.createView().of(TransformComponent.class, TransformAnimatorComponent.class)
      .getEntries()
      .forEach(view -> {
        TransformComponent transformComponent = view.c1();
        TransformAnimatorComponent transformAnimatorComponent = view.c2();

        transformAnimatorComponent.tickAnimation(transformComponent, deltaTime);
      });
  }
}
