package technology.sola.engine.graphics.sprite;

import technology.sola.engine.ecs.AbstractEcsSystem;
import technology.sola.engine.ecs.World;

public class SpriteAnimatorSystem extends AbstractEcsSystem {
  @Override
  public void update(World world, float deltaTime) {
    world.getEntitiesWithComponents(SpriteComponent.class, SpriteAnimatorComponent.class)
      .forEach(entity -> {
        SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
        SpriteAnimatorComponent spriteAnimatorComponent = entity.getComponent(SpriteAnimatorComponent.class);

        spriteComponent.setSpriteKeyFrame(spriteAnimatorComponent.getCurrentFrame());
      });
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
