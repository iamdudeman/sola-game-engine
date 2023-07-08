package technology.sola.engine.graphics.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.graphics.components.animation.SpriteAnimatorComponent;
import technology.sola.engine.graphics.components.SpriteComponent;

/**
 * SpriteAnimatorSystem handles updating the animation state of {@link technology.sola.ecs.Entity} that have a
 * {@link SpriteComponent} and {@link SpriteAnimatorComponent}.
 */
public class SpriteAnimatorSystem extends EcsSystem {
  @Override
  public void update(World world, float deltaTime) {
    for (var entry : world.createView().of(SpriteComponent.class, SpriteAnimatorComponent.class).getEntries()) {
      SpriteComponent spriteComponent = entry.c1();
      SpriteAnimatorComponent spriteAnimatorComponent = entry.c2();

      spriteAnimatorComponent.tickAnimation(deltaTime);
      spriteComponent.setSpriteKeyFrame(spriteAnimatorComponent.getCurrentFrame());
    }
  }
}
