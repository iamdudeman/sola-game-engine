package technology.sola.engine.graphics.sprite;

import technology.sola.engine.ecs.EcsSystem;
import technology.sola.engine.ecs.World;
import technology.sola.engine.graphics.components.SpriteAnimatorComponent;
import technology.sola.engine.graphics.components.SpriteComponent;

public class SpriteAnimatorSystem extends EcsSystem {
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
