package technology.sola.engine.examples.common.features;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Component;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheet;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaGraphics;
import technology.sola.engine.defaults.graphics.modules.CircleEntityGraphicsModule;
import technology.sola.engine.defaults.graphics.modules.RectangleEntityGraphicsModule;
import technology.sola.engine.defaults.graphics.modules.SpriteEntityGraphicsModule;
import technology.sola.engine.defaults.graphics.modules.TriangleEntityGraphicsModule;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.*;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Layer;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.Key;
import technology.sola.math.geometry.Triangle;
import technology.sola.math.linear.Vector2D;

import java.util.List;

/**
 * RenderingExample is a {@link Sola} for demoing various graphics related things for the sola game engine.
 *
 * <ul>
 *   <li>{@link Renderer}</li>
 *   <li>{@link SolaGraphics}</li>
 *   <li>{@link technology.sola.engine.defaults.graphics.modules.SolaEntityGraphicsModule}</li>
 *   <li>{@link SolaImage}</li>
 *   <li>{@link LayerComponent}</li>
 * </ul>
 */
@NullMarked
public class RenderingExample extends Sola {
  private final SolaGraphics solaGraphics;

  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public RenderingExample() {
    super(new SolaConfiguration("Rendering Example", 800, 600, 30));

    solaGraphics = new SolaGraphics(solaEcs);
  }

  @Override
  protected void onInit() {
    ExampleLauncherSola.addReturnToLauncherKeyEvent(platform(), eventHub);

    solaGraphics.addGraphicsModules(
      new CircleEntityGraphicsModule(),
      new RectangleEntityGraphicsModule(),
      new TriangleEntityGraphicsModule(),
      new SpriteEntityGraphicsModule(assetLoaderProvider.get(SpriteSheet.class))
    );

    solaEcs.addSystem(new TestSystem());
    solaEcs.addSystems(solaGraphics.getSystems());
    solaEcs.setWorld(createWorld());

    platform().getRenderer().createLayers("background", "moving_stuff", "blocks", "ui");

    platform().getViewport().setAspectMode(AspectMode.MAINTAIN);

    assetLoaderProvider.get(SolaImage.class)
      .addAssetMapping("test", "assets/sprites/test_tiles.png");
    assetLoaderProvider.get(SpriteSheet.class)
      .addAssetMapping("test", "assets/sprites/test_tiles.sprites.json");
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    renderer.drawToLayer("background", r -> {
      renderer.setPixel(5, 5, Color.WHITE);
      renderer.setPixel(6, 5, Color.BLUE);
      renderer.setPixel(6, 6, Color.RED);
      renderer.setPixel(6, 7, Color.GREEN);

      renderer.drawLine(20, 50, 20, 100, Color.WHITE);
      renderer.drawLine(50, 20, 100, 20, Color.WHITE);
      renderer.drawLine(0, 220, 100, 400, Color.WHITE);

      renderer.drawLine(0, 0, 800, 600, Color.BLUE);
      renderer.drawLine(750, 0, 20, 500, Color.BLUE);

      assetLoaderProvider.get(SolaImage.class).get("test").executeIfLoaded(solaImage -> {
        renderer.fillRect(350, 320, 200, 200, Color.WHITE);
        renderer.setBlendFunction(BlendMode.MASK);
        renderer.drawImage(solaImage , 350, 320, 200, 200);
        renderer.setBlendFunction(BlendMode.NO_BLENDING);
      });
    });

    solaGraphics.render(renderer);

    renderer.drawToLayer("ui", r -> {
      renderer.setBlendFunction(BlendMode.NORMAL);
      renderer.fillRect(80, 0, 600, 100, new Color(120, 255, 255, 255));
      renderer.setBlendFunction(BlendMode.NO_BLENDING);
      renderer.drawRect(80, 0, 600, 100, Color.YELLOW);

      renderer.fillRect(180, 65, 300, 25, Color.WHITE);

      final String characters1 = "!\"#$%&'()*+,-./0123456789:; <=>?@ABCDEFGHIJKLMN";
      final String characters2 = "OPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

      renderer.drawString(characters1, 85, 5, Color.RED);
      renderer.drawString(characters2, 85, 35, Color.BLACK);
      renderer.drawString("Hello world!", 182, 67, Color.BLUE);
    });
  }

  private record MovingComponent() implements Component {
  }

  private class TestSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      world.createView().of(TransformComponent.class, MovingComponent.class)
        .getEntries()
        .forEach(view -> {
          TransformComponent transform = view.c1();

          transform.setX(transform.getX() + 1);
          transform.setY(transform.getY() + 1);
        });

      if (keyboardInput.isKeyPressed(Key.ONE)) {
        platform().getViewport().setAspectMode(AspectMode.IGNORE_RESIZING);
      } else if (keyboardInput.isKeyPressed(Key.TWO)) {
        platform().getViewport().setAspectMode(AspectMode.MAINTAIN);
      } else if (keyboardInput.isKeyPressed(Key.THREE)) {
        platform().getViewport().setAspectMode(AspectMode.STRETCH);
      }

      final float scalingSpeed = 1f;
      final float minSize = 0.1f;
      final float maxSize = 50;

      TransformComponent dynamicScalingEntityTransformComponent = world.findEntityByName("dynamicScaling")
        .getComponent(TransformComponent.class);

      if (keyboardInput.isKeyHeld(Key.A)) {
        float dynamicScale = dynamicScalingEntityTransformComponent.getScaleX() - scalingSpeed;
        if (dynamicScale < minSize) dynamicScale = minSize;
        dynamicScalingEntityTransformComponent.setScaleX(dynamicScale);
      }
      if (keyboardInput.isKeyHeld(Key.D)) {
        float dynamicScale = dynamicScalingEntityTransformComponent.getScaleX() + scalingSpeed;
        if (dynamicScale > maxSize) dynamicScale = maxSize;
        dynamicScalingEntityTransformComponent.setScaleX(dynamicScale);
      }
      if (keyboardInput.isKeyHeld(Key.W)) {
        float dynamicScale = dynamicScalingEntityTransformComponent.getScaleY() - scalingSpeed;
        if (dynamicScale < minSize) dynamicScale = minSize;
        dynamicScalingEntityTransformComponent.setScaleY(dynamicScale);
      }
      if (keyboardInput.isKeyHeld(Key.S)) {
        float dynamicScale = dynamicScalingEntityTransformComponent.getScaleY() + scalingSpeed;
        if (dynamicScale > maxSize) dynamicScale = maxSize;
        dynamicScalingEntityTransformComponent.setScaleY(dynamicScale);
      }

      if (keyboardInput.isKeyPressed(Key.H)) {
        Layer blockLayer = platform().getRenderer().getLayer("blocks");
        blockLayer.setActive(!blockLayer.isActive());

        Layer backgroundLayer = platform().getRenderer().getLayer("background");
        backgroundLayer.setActive(!backgroundLayer.isActive());
      }
    }
  }

  private World createWorld() {
    World world = new World(100);

    // moving stuff
    Entity movingEntity = world.createEntity(
      new MovingComponent(),
      new LayerComponent("moving_stuff"),
      new TransformComponent(0, 0, 50, 50)
    );
    List.of(
      new Vector2D(0, 0),
      new Vector2D(50, 20),
      new Vector2D(100, 20),
      new Vector2D(150, 20),
      new Vector2D(200, 20)
    ).forEach(vector2D -> world.createEntity(
      new LayerComponent("moving_stuff"),
      new TransformComponent(vector2D.x(), vector2D.y(), movingEntity),
      new RectangleRendererComponent(Color.RED))
    );

    world.createEntity(
      new LayerComponent("moving_stuff", 10),
      new TransformComponent(0, 0),
      new SpriteComponent("test", "stick_figure"),
      new BlendModeComponent(BlendMode.MASK),
      new MovingComponent()
    );

    world.createEntity()
      .addComponent(new TransformComponent(400, 530, 1, 1))
      .addComponent(new SpriteComponent("test", "blue"));
    world.createEntity()
      .addComponent(new LayerComponent("moving_stuff"))
      .addComponent(new TransformComponent(5, 5, 1, 2))
      .addComponent(new SpriteComponent("test", "blue"))
      .setName("dynamicScaling");


    // static blocks
    world.createEntity()
      .addComponent(new LayerComponent("blocks"))
      .addComponent(new TransformComponent(200, 300, 50, 50))
      .addComponent(new RectangleRendererComponent(Color.BLUE));
    world.createEntity()
      .addComponent(new LayerComponent("blocks"))
      .addComponent(new TransformComponent(200, 350, 100, 50))
      .addComponent(new RectangleRendererComponent(Color.BLUE));

    // background elements
    world.createEntity()
      .addComponent(new LayerComponent("background"))
      .addComponent(new TransformComponent(100, 100, 60, 80))
      .addComponent(new RectangleRendererComponent(Color.GREEN));
    world.createEntity()
      .addComponent(new LayerComponent("background"))
      .addComponent(new TransformComponent(100, 100, 60, 80))
      .addComponent(new RectangleRendererComponent(Color.RED, false));

    Triangle triangle1 = new Triangle(new Vector2D(0, 0), new Vector2D(5, 10), new Vector2D(10, 0));
    Triangle triangle2 = new Triangle(new Vector2D(0, 0), new Vector2D(0, 10), new Vector2D(10, 0));
    Triangle triangle3 = new Triangle(new Vector2D(0, 0), new Vector2D(10, 0), new Vector2D(10, 10));

    world.createEntity()
      .addComponent(new LayerComponent("background"))
      .addComponent(new TransformComponent(640, 200, 6, 8))
      .addComponent(new TriangleRendererComponent(Color.GREEN, true, triangle1));
    world.createEntity()
      .addComponent(new LayerComponent("background"))
      .addComponent(new TransformComponent(640, 200, 6, 8))
      .addComponent(new TriangleRendererComponent(Color.RED, false, triangle1));

    world.createEntity()
      .addComponent(new LayerComponent("background"))
      .addComponent(new TransformComponent(600, 290, 6, 8))
      .addComponent(new TriangleRendererComponent(Color.RED, false, triangle2));
    world.createEntity()
      .addComponent(new LayerComponent("background"))
      .addComponent(new TransformComponent(600, 290, 6, 8))
      .addComponent(new TriangleRendererComponent(Color.GREEN, true, triangle2));

    world.createEntity()
      .addComponent(new LayerComponent("background"))
      .addComponent(new TransformComponent(680, 290, 6, 8))
      .addComponent(new TriangleRendererComponent(Color.GREEN, true, triangle3));
    world.createEntity()
      .addComponent(new LayerComponent("background"))
      .addComponent(new TransformComponent(680, 290, 6, 8))
      .addComponent(new TriangleRendererComponent(Color.RED, false, triangle3));

    world.createEntity()
      .addComponent(new LayerComponent("background"))
      .addComponent(new TransformComponent(300, 150, 5f, 5f))
      .addComponent(new RectangleRendererComponent(Color.GREEN, false));

    world.createEntity()
      .addComponent(new LayerComponent("background"))
      .addComponent(new TransformComponent(180, 430, 50, 50))
      .addComponent(new CircleRendererComponent(Color.BLUE));
    world.createEntity()
      .addComponent(new LayerComponent("background"))
      .addComponent(new TransformComponent(210, 430, 50, 50))
      .addComponent(new CircleRendererComponent(new Color(150, 255, 0, 0)));

    world.createEntity()
      .addComponent(new LayerComponent("background"))
      .addComponent(new TransformComponent(180, 530, 50, 50))
      .addComponent(new RectangleRendererComponent(Color.BLUE));
    world.createEntity()
      .addComponent(new LayerComponent("background"))
      .addComponent(new TransformComponent(210, 530, 50, 50))
      .addComponent(new RectangleRendererComponent(new Color(150, 255, 0, 0)));

    world.createEntity()
      .addComponent(new LayerComponent("background"))
      .addComponent(new TransformComponent(300, 150, 105f, 105f))
      .addComponent(new CircleRendererComponent(Color.BLUE));
    world.createEntity()
      .addComponent(new LayerComponent("background"))
      .addComponent(new TransformComponent(300, 150, 105f, 105f))
      .addComponent(new CircleRendererComponent(Color.RED, false));

    return world;
  }
}
