package technology.sola.engine.graphics.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.animation.TransformAnimatorComponent;

public class TransformAnimatorSystem extends EcsSystem {
  @Override
  public void update(World world, float deltaTime) {
    world.createView().of(TransformComponent.class, TransformAnimatorComponent.class)
      .forEach(view -> {
        TransformComponent transformComponent = view.c1();
        TransformAnimatorComponent transformAnimatorComponent = view.c2();


        // todo need easing function to set this
        //  linear - (x)
        //  ease-in - (x^2)
        //  ease-out - (1 - (x - 1)^2)
        //  smooth - (x^2)(3 - 2x)
        transformComponent.setTranslate(0, 0);
        transformComponent.setScale(1, 1);
      });
  }
}
