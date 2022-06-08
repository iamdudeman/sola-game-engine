package technology.sola.engine.graphics.sprite;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.graphics.components.SpriteAnimatorComponent;
import technology.sola.engine.graphics.components.SpriteComponent;

public class SpriteAnimatorSystem extends EcsSystem {
  @Override
  public void update(World world, float deltaTime) {
    world.createView().of(SpriteComponent.class, SpriteAnimatorComponent.class)
      .forEach(view -> {
        SpriteComponent spriteComponent = view.c1();
        SpriteAnimatorComponent spriteAnimatorComponent = view.c2();

        spriteComponent.setSpriteKeyFrame(spriteAnimatorComponent.getCurrentFrame());
      });
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
