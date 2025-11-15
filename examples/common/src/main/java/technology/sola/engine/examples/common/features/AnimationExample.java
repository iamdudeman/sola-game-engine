package technology.sola.engine.examples.common.features;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheet;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.examples.common.ExampleUtils;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.graphics.components.SpriteKeyFrame;
import technology.sola.engine.graphics.components.animation.SpriteAnimatorComponent;
import technology.sola.engine.graphics.components.animation.TransformAnimatorComponent;
import technology.sola.engine.graphics.renderer.Renderer;
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
@NullMarked
public class AnimationExample extends Sola {
  private SolaGraphics solaGraphics;

  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public AnimationExample() {
    super(new SolaConfiguration("Animation Example", 210, 200, 30));
  }

  @Override
  protected void onInit() {
    solaGraphics = new SolaGraphics.Builder(platform(), solaEcs)
      .withBackgroundColor(Color.WHITE)
      .withGui(mouseInput)
      .buildAndInitialize(assetLoaderProvider);

    solaEcs.setWorld(buildWorld());
    solaGraphics.guiDocument().setRootElement(
      ExampleUtils.createReturnToLauncherButton(platform(), eventHub, "0", "0")
    );
    platform().getViewport().setAspectMode(AspectMode.STRETCH);
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    assetLoaderProvider.get(SpriteSheet.class)
      .getNewAsset("test", "assets/sprites/test_tiles.sprites.json")
      .executeWhenLoaded(spriteSheet -> completeAsyncInit.run());
  }

  @Override
  protected void onRender(Renderer renderer) {
    solaGraphics.render(renderer);
  }

  private World buildWorld() {
    World world = new World(50);

    addSpriteAnimatorExamples(world);

    addTransformAnimatorExamples(world);

    return world;
  }

  private void addSpriteAnimatorExamples(World world) {
    for (int i = 0; i < 10; i++) {
      final boolean showMessage = i == 0;

      world.createEntity()
        .addComponent(new TransformComponent(5 + (i * 20f), 30))
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
      .addComponent(new TransformComponent(25, 50))
      .addComponent(new SpriteComponent("test", "lime"))
      .addComponent(new SpriteAnimatorComponent(
        "stop_light1",
        new SpriteKeyFrame("test", "lime", 3000),
        new SpriteKeyFrame("test", "orange", 1500),
        new SpriteKeyFrame("test", "maroon", 4500)
      ));

    world.createEntity()
      .addComponent(new TransformComponent(95, 50))
      .addComponent(new SpriteComponent("test", "maroon"))
      .addComponent(new SpriteAnimatorComponent(
        "stop_light2",
        new SpriteKeyFrame("test", "maroon", 4500),
        new SpriteKeyFrame("test", "lime", 3000),
        new SpriteKeyFrame("test", "orange", 1500)
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
  }

  private void addTransformAnimatorExamples(World world) {
    world.createEntity(
      new TransformComponent(5, 70, 15),
      new CircleRendererComponent(Color.RED, true),
      new TransformAnimatorComponent(EasingFunction.LINEAR, 4000)
        .setTranslateX(180)
    );

    world.createEntity(
      new TransformComponent(5, 90, 15),
      new CircleRendererComponent(Color.GREEN, true),
      new TransformAnimatorComponent(EasingFunction.EASE_IN, 4000)
        .setTranslateX(180)
    );

    world.createEntity(
      new TransformComponent(5, 110, 15),
      new CircleRendererComponent(Color.BLUE, true),
      new TransformAnimatorComponent(EasingFunction.EASE_OUT, 4000)
        .setTranslateX(180)
    );

    TransformAnimatorComponent smoothTransformAnimator = new TransformAnimatorComponent(EasingFunction.SMOOTH_STEP, 4000)
      .setTranslateX(180);

    final Entity smoothStepAnimationEntity = world.createEntity(
      new TransformComponent(5, 130, 15),
      new CircleRendererComponent(Color.BLACK, true),
      smoothTransformAnimator
    );

    smoothTransformAnimator.setAnimationCompleteCallback((transformAnimatorComponent) -> smoothStepAnimationEntity.addComponent(
      new TransformAnimatorComponent(EasingFunction.SMOOTH_STEP, 4000)
        .setTranslateX(0)
        .setAnimationCompleteCallback((transformAnimatorComponent2) -> smoothStepAnimationEntity.addComponent(
          new TransformAnimatorComponent(EasingFunction.SMOOTH_STEP, 3000)
            .setTranslateX(90)
            .setScale(5)
            .setAnimationCompleteCallback(TransformAnimatorComponent::reset))
        )
    ));
  }
}
