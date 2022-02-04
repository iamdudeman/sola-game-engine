package technology.sola.engine.graphics.sprite;

import technology.sola.engine.ecs.EcsSystem;
import technology.sola.engine.ecs.World;
import technology.sola.engine.graphics.components.SpriteAnimatorComponent;
import technology.sola.engine.graphics.components.SpriteComponent;

public class SpriteAnimatorSystem extends EcsSystem {
  @Override
  public void update(World world, float deltaTime) {
    world.getView().of(SpriteComponent.class, SpriteAnimatorComponent.class)
      .forEach(view -> {
        SpriteComponent spriteComponent = view.getC1();
        SpriteAnimatorComponent spriteAnimatorComponent = view.getC2();

        spriteComponent.setSpriteKeyFrame(spriteAnimatorComponent.getCurrentFrame());
      });
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
