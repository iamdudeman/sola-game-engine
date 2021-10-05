package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.ecs.World;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.graphics.sprite.SpriteAnimatorComponent;
import technology.sola.engine.graphics.sprite.SpriteAnimatorSystem;
import technology.sola.engine.graphics.sprite.SpriteComponent;
import technology.sola.engine.graphics.sprite.SpriteKeyFrame;
import technology.sola.engine.graphics.sprite.SpriteSheet;
import technology.sola.engine.physics.component.PositionComponent;

public class AnimationExample extends AbstractSola {
  private AssetPool<SpriteSheet> spriteSheetAssetPool;

  @Override
  protected SolaConfiguration buildConfiguration() {
    return new SolaConfiguration(
      "Animation", 200, 200, 30, true
    );
  }

  @Override
  protected void onInit() {
    spriteSheetAssetPool = assetPoolProvider.getAssetPool(SpriteSheet.class);
    spriteSheetAssetPool.addAssetId("test", "assets/test_tiles_spritesheet.json");

    ecsSystemContainer.add(new SpriteAnimatorSystem());

    ecsSystemContainer.setWorld(buildWorld());

    platform.getViewport().setAspectMode(AspectMode.STRETCH);
  }

  @Override
  protected void onRender(Renderer renderer) {
    ecsSystemContainer.getWorld().getEntitiesWithComponents(PositionComponent.class, SpriteComponent.class)
      .forEach(entity -> {
        PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
        SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);

        renderer.drawImage(positionComponent.getX(), positionComponent.getY(), spriteComponent.getSprite(spriteSheetAssetPool));
      });
  }

  private World buildWorld() {
    World world = new World(14);

    for (int i = 0; i < 6; i++) {
      world.createEntity()
        .addComponent(new PositionComponent(5 + (i * 20f), 5))
        .addComponent(new SpriteComponent("test", "blue"))
        .addComponent(new SpriteAnimatorComponent(
          "first",
          new SpriteKeyFrame("test", "blue", 750),
          new SpriteKeyFrame("test", "purple", 750),
          new SpriteKeyFrame("test", "brown", 750),
          new SpriteKeyFrame("test", "lime", 750),
          new SpriteKeyFrame("test", "orange", 750),
          new SpriteKeyFrame("test", "maroon", 750)
        ));
    }

    world.createEntity()
      .addComponent(new PositionComponent(25, 60))
      .addComponent(new SpriteComponent("test", "lime"))
      .addComponent(new SpriteAnimatorComponent(
        "stop_light1",
        new SpriteKeyFrame("test", "lime", 3000),
        new SpriteKeyFrame("test", "orange", 1500),
        new SpriteKeyFrame("test", "maroon", 4500)
      ));

    world.createEntity()
      .addComponent(new PositionComponent(95, 60))
      .addComponent(new SpriteComponent("test", "maroon"))
      .addComponent(new SpriteAnimatorComponent(
        "stop_light2",
        new SpriteKeyFrame("test", "maroon", 4500),
        new SpriteKeyFrame("test", "lime", 3000),
        new SpriteKeyFrame("test", "orange", 1500)
      ));

    for (int i = 0; i < 6; i++) {
      world.createEntity()
        .addComponent(new PositionComponent(5 + (i * 20f), 150))
        .addComponent(new SpriteComponent("test", "blue"))
        .addComponent(new SpriteAnimatorComponent(
          "first",
          new SpriteKeyFrame("test", "blue", 750),
          new SpriteKeyFrame("test", "purple", 750),
          new SpriteKeyFrame("test", "brown", 750),
          new SpriteKeyFrame("test", "lime", 750),
          new SpriteKeyFrame("test", "orange", 750),
          new SpriteKeyFrame("test", "maroon", 750)
        ));
    }

    return world;
  }
}
