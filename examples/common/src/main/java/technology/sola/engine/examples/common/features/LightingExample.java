package technology.sola.engine.examples.common.features;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheet;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.examples.common.ExampleUtils;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.graphics.components.animation.TransformAnimatorComponent;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.SectionGuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.property.Direction;
import technology.sola.engine.graphics.gui.style.theme.DefaultThemeBuilder;
import technology.sola.engine.graphics.modules.ScreenSpaceLightMapGraphicsModule;
import technology.sola.engine.graphics.modules.SpriteEntityGraphicsModule;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.BlendModeComponent;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.LightComponent;
import technology.sola.engine.graphics.components.LightFlicker;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.MouseButton;
import technology.sola.engine.input.TouchPhase;
import technology.sola.engine.physics.SolaPhysics;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.component.collider.ColliderShapeAABB;
import technology.sola.engine.physics.component.particle.ParticleEmitterComponent;
import technology.sola.engine.physics.utils.ColliderUtils;
import technology.sola.engine.utils.SolaRandom;
import technology.sola.math.EasingFunction;
import technology.sola.math.linear.Vector2D;

/**
 * LightingExample is a {@link technology.sola.engine.core.Sola} for demoing lighting for the sola game engine.
 *
 * <ul>
 *   <li>{@link LightComponent}</li>
 *   <li>{@link technology.sola.engine.graphics.system.LightFlickerSystem}</li>
 *   <li>{@link ScreenSpaceLightMapGraphicsModule}</li>
 * </ul>
 */
@NullMarked
public class LightingExample extends Sola {
  private SolaGraphics solaGraphics;
  private SolaPhysics solaPhysics;

  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public LightingExample() {
    super(new SolaConfiguration("Lighting", 256, 240, 30));
  }

  @Override
  protected void onInit() {
    solaPhysics = new SolaPhysics.Builder(solaEcs)
      .buildAndInitialize(eventHub);

    solaPhysics.getGravitySystem().setActive(false);

    solaGraphics = new SolaGraphics.Builder(platform(), solaEcs)
      .withGui(mouseInput)
      .withLighting(new Color(10, 10, 10))
      .withBackgroundColor(Color.WHITE)
      .buildAndInitialize(assetLoaderProvider);

    solaGraphics.guiDocument().setRootElement(buildGui());

    solaEcs.addSystem(new PlayerSystem());
    solaEcs.setWorld(buildWorld());
    platform().getViewport().setAspectMode(AspectMode.MAINTAIN);
    platform().getRenderer().createLayers("objects");
  }

  private GuiElement<?, ?> buildGui() {
    var section = new SectionGuiElement()
      .addStyle(new BaseStyles.Builder<>().setDirection(Direction.ROW).setGap(2).build())
      .appendChildren(
        new ButtonGuiElement()
          .setOnAction(() -> ExampleUtils.returnToLauncher(platform(), eventHub))
          .appendChildren(
            new TextGuiElement()
              .setText("Back")
          ),
        new ButtonGuiElement()
          .setOnAction(() -> {
            ScreenSpaceLightMapGraphicsModule screenSpaceLightMapGraphicsModule = solaGraphics.getGraphicsModule(ScreenSpaceLightMapGraphicsModule.class);

            screenSpaceLightMapGraphicsModule.setActive(!screenSpaceLightMapGraphicsModule.isActive());
          })
          .appendChildren(
            new TextGuiElement()
              .setText("Light")
          ),
        new ButtonGuiElement()
          .setOnAction(() -> {
            SpriteEntityGraphicsModule spriteEntityGraphicsModule = solaGraphics.getGraphicsModule(SpriteEntityGraphicsModule.class);

            spriteEntityGraphicsModule.setActive(!spriteEntityGraphicsModule.isActive());
          })
          .appendChildren(
            new TextGuiElement()
              .setText("Sprites")
          )
      );

    DefaultThemeBuilder.buildDarkTheme().applyToTree(section);

    return section;
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    assetLoaderProvider.get(SpriteSheet.class)
      .getNewAsset("forest", "assets/sprites/forest.sprites.json")
      .executeWhenLoaded(spriteSheet -> {
        ColliderUtils.autoSizeColliderToSprite(solaEcs.getWorld().findEntityByName("player"), spriteSheet);

        completeAsyncInit.run();
      });
  }

  @Override
  protected void onRender(Renderer renderer) {
    solaGraphics.render(renderer);
  }

  private World buildWorld() {
    World world = new World(1500);

    for (int i = 0; i < platform().getRenderer().getWidth(); i += 8) {
      for (int j = 0; j < platform().getRenderer().getHeight(); j += 8) {
        int grassTileIndex = SolaRandom.nextInt(3) + 1;
        String grassSprite = "grass_" + grassTileIndex;

        world.createEntity(
          new TransformComponent(i, j),
          new SpriteComponent("forest", grassSprite)
        );
      }
    }

    for (int i = 0; i < 200; i++) {
      int x = SolaRandom.nextInt(platform().getRenderer().getWidth() - 20) + 10;
      int y = SolaRandom.nextInt(platform().getRenderer().getHeight() - 20) + 10;

      world.createEntity(
        new TransformComponent(x, y),
        new SpriteComponent("forest", "tree"),
        new LayerComponent("objects").setOrderByVerticalPosition(true),
        new ColliderComponent(new ColliderShapeAABB(2, 3), 3, 4),
        new DynamicBodyComponent(true),
        new BlendModeComponent(BlendMode.MASK)
      );
    }

    world.createEntity(
      new TransformComponent(platform().getRenderer().getWidth() / 2f, platform().getRenderer().getHeight() / 2f),
      new SpriteComponent("forest", "player"),
      new BlendModeComponent(BlendMode.MASK),
      new LayerComponent("objects").setOrderByVerticalPosition(true),
      new ColliderComponent(new ColliderShapeAABB()),
      new DynamicBodyComponent(),
      new LightComponent(50, new Color(200, 255, 255, 255)).setOffset(2.5f, 4)
    ).setName("player");

    return world;
  }

  private class PlayerSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      Vector2D target = null;
      var touch = touchInput.getFirstTouch();

      if (touch != null && touch.phase() == TouchPhase.BEGAN) {
        target = new Vector2D(touch.x(), touch.y());
      }
      if (mouseInput.isMousePressed(MouseButton.PRIMARY)) {
        target = mouseInput.getMousePosition();
      }

      if (target != null) {
        Entity playerEntity = world.findEntityByName("player");
        Vector2D coordinate = solaGraphics.screenToWorldCoordinate(target);

        if (coordinate.y() < 20) {
          return;
        }

        float radius = SolaRandom.nextFloat(8f, 32f);
        int intensity = SolaRandom.nextInt(25, 220);

        playerEntity.addComponent(
          new TransformAnimatorComponent(EasingFunction.LINEAR, 3000)
            .setTranslateX(coordinate.x())
            .setTranslateY(coordinate.y())
        );

        var entity = world.createEntity(
          new TransformComponent(coordinate.x(), coordinate.y()),
          new SpriteComponent("forest", "torch"),
          new BlendModeComponent(BlendMode.MASK),
          new LayerComponent("objects", 1),
          new LightComponent(radius, new Color(intensity, 255, 255, 255))
            .setOffset(1.5f, 3)
            .setLightFlicker(new LightFlicker(0.2f, .8f))
        );

        world.createEntity(
          new TransformComponent(entity),
          buildFireParticleEmitterComponent(),
          new BlendModeComponent(BlendMode.LINEAR_DODGE),
          new LayerComponent("objects", 2)
        );
      }
    }
  }

  private static ParticleEmitterComponent buildFireParticleEmitterComponent() {
    ParticleEmitterComponent fireParticleEmitterComponent = new ParticleEmitterComponent();

    fireParticleEmitterComponent
      .configureAppearance().setColor(new Color(230, 40, 45)).setSizeBounds(1, 3).done()
      .configureMovement().setVelocityBounds(new Vector2D(-1.2f, -3f), new Vector2D(1.2f, 0)).done()
      .configureEmission().setCountPerEmit(2).setLife(1);

    fireParticleEmitterComponent.setParticleEmissionDelay(0.1f);

    return fireParticleEmitterComponent;
  }
}
