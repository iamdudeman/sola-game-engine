package technology.sola.engine.examples.common.singlefile;

import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.graphics.components.SpriteKeyFrame;
import technology.sola.engine.graphics.components.animation.SpriteAnimatorComponent;
import technology.sola.engine.graphics.components.animation.TransformAnimatorComponent;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.math.EasingFunction;

/**
 * AnimationExample is a {@link technology.sola.engine.core.Sola} for demoing animations for the sola game engine.
 *
 * <ul>
 *   <li>{@link SpriteAnimatorComponent}</li>
 *   <li>{@link TransformAnimatorComponent}</li>
 * </ul>
 */
public class AnimationExample extends SolaWithDefaults {
  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public AnimationExample() {
    super(
      SolaConfiguration.build("Animation Example", 210, 200).withTargetUpdatesPerSecond(30)
    );
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGraphics().useBackgroundColor(Color.WHITE);

    solaEcs.setWorld(buildWorld());
    platform.getViewport().setAspectMode(AspectMode.STRETCH);
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    assetLoaderProvider.get(SpriteSheet.class)
      .getNewAsset("test", "assets/test_tiles.sprites.json")
      .executeWhenLoaded(spriteSheet -> completeAsyncInit.run());
  }

  private World buildWorld() {
    World world = new World(50);

    for (int i = 0; i < 10; i++) {
      final boolean showMessage = i == 0;

      world.createEntity()
        .addComponent(new TransformComponent(5 + (i * 20f), 5))
        .addComponent(new SpriteComponent("test", "blue"))
        .addComponent(new SpriteAnimatorComponent(
          "first",
          2,
          new SpriteKeyFrame("test", "blue", 750),
          new SpriteKeyFrame("test", "purple", 750),
          new SpriteKeyFrame("test", "brown", 750),
          new SpriteKeyFrame("test", "lime", 750),
          new SpriteKeyFrame("test", "orange", 750),
          new SpriteKeyFrame("test", "maroon", 750)
        ).setAnimationCompleteCallback(animationId -> {
          if (showMessage) {
            System.out.println("Completed " + animationId);
          }
        }));
    }

    world.createEntity()
      .addComponent(new TransformComponent(25, 45))
      .addComponent(new SpriteComponent("test", "lime"))
      .addComponent(new SpriteAnimatorComponent(
        "stop_light1",
        new SpriteKeyFrame("test", "lime", 3000),
        new SpriteKeyFrame("test", "orange", 1500),
        new SpriteKeyFrame("test", "maroon", 4500)
      ));

    world.createEntity()
      .addComponent(new TransformComponent(95, 45))
      .addComponent(new SpriteComponent("test", "maroon"))
      .addComponent(new SpriteAnimatorComponent(
        "stop_light2",
        new SpriteKeyFrame("test", "maroon", 4500),
        new SpriteKeyFrame("test", "lime", 3000),
        new SpriteKeyFrame("test", "orange", 1500)
      ));

    world.createEntity(
      new TransformComponent(5, 70, 15),
      new CircleRendererComponent(Color.RED, true),
      new TransformAnimatorComponent.Builder(EasingFunction.LINEAR, 4000).withTranslateX(180).build()
    );

    world.createEntity(
      new TransformComponent(5, 90, 15),
      new CircleRendererComponent(Color.GREEN, true),
      new TransformAnimatorComponent.Builder(EasingFunction.EASE_IN, 4000).withTranslateX(180).build()
    );

    world.createEntity(
      new TransformComponent(5, 110, 15),
      new CircleRendererComponent(Color.BLUE, true),
      new TransformAnimatorComponent.Builder(EasingFunction.EASE_OUT, 4000).withTranslateX(180).build()
    );

    TransformAnimatorComponent smoothTransformAnimator = new TransformAnimatorComponent.Builder(EasingFunction.SMOOTH_STEP, 4000).withTranslateX(180).build();

    final Entity smoothStepAnimationEntity = world.createEntity(
      new TransformComponent(5, 130, 15),
      new CircleRendererComponent(Color.BLACK, true),
      smoothTransformAnimator
    );

    smoothTransformAnimator.setAnimationCompleteCallback(() -> smoothStepAnimationEntity.addComponent(
      new TransformAnimatorComponent.Builder(EasingFunction.SMOOTH_STEP, 4000).withTranslateX(0).build()
        .setAnimationCompleteCallback(() -> smoothStepAnimationEntity.addComponent(
          new TransformAnimatorComponent.Builder(EasingFunction.SMOOTH_STEP, 3000).withTranslateX(90).withScale(5).build())
        )
    ));

    for (int i = 0; i < 10; i++) {
      world.createEntity()
        .addComponent(new TransformComponent(5 + (i * 20f), 180))
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
