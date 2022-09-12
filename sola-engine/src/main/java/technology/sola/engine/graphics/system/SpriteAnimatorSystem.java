package technology.sola.engine.graphics.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.graphics.components.sprite.SpriteAnimatorComponent;
import technology.sola.engine.graphics.components.sprite.SpriteComponent;

public class SpriteAnimatorSystem extends EcsSystem {
  @Override
  public void update(World world, float deltaTime) {
    world.createView().of(SpriteComponent.class, SpriteAnimatorComponent.class)
      .forEach(view -> {
        SpriteComponent spriteComponent = view.c1();
        SpriteAnimatorComponent spriteAnimatorComponent = view.c2();

        spriteAnimatorComponent.tickAnimation(deltaTime);
        spriteComponent.setSpriteKeyFrame(spriteAnimatorComponent.getCurrentFrame());
      });
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
