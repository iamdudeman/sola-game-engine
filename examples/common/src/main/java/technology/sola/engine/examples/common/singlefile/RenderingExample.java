package technology.sola.engine.examples.common.singlefile;

import technology.sola.ecs.Component;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaGraphics;
import technology.sola.engine.defaults.graphics.modules.CircleGraphicsModule;
import technology.sola.engine.defaults.graphics.modules.RectangleGraphicsModule;
import technology.sola.engine.defaults.graphics.modules.SpriteGraphicsModule;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.BlendModeComponent;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Layer;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.Key;
import technology.sola.math.linear.Vector2D;

import java.io.Serial;
import java.util.List;

public class RenderingExample extends Sola {
  private final SolaGraphics solaGraphics;

  public RenderingExample() {
    super(SolaConfiguration.build("Rendering Example", 800, 600).withTargetUpdatesPerSecond(30));

    solaGraphics = new SolaGraphics(solaEcs);
  }

  @Override
  protected void onInit() {
    solaGraphics.addGraphicsModules(
      new CircleGraphicsModule(),
      new RectangleGraphicsModule(),
      new SpriteGraphicsModule(assetLoaderProvider.get(SpriteSheet.class))
    );

    solaEcs.addSystem(new TestSystem());
    solaEcs.addSystems(solaGraphics.getSystems());
    solaEcs.setWorld(createWorld());

    platform.getRenderer().createLayers("background", "moving_stuff", "blocks", "ui");

    assetLoaderProvider.get(SolaImage.class)
      .addAssetMapping("test", "assets/test_tiles.png");
    assetLoaderProvider.get(SpriteSheet.class)
      .addAssetMapping("test", "assets/test_tiles_spritesheet.json");
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
        renderer.setBlendMode(BlendMode.MASK);
        renderer.drawImage(solaImage , 350, 320, 200, 200);
        renderer.setBlendMode(BlendMode.NO_BLENDING);
      });
    });

    solaGraphics.render(renderer);

    renderer.drawToLayer("ui", r -> {
      renderer.setBlendMode(BlendMode.NORMAL);
      renderer.fillRect(80, 0, 600, 100, new Color(120, 255, 255, 255));
      renderer.setBlendMode(BlendMode.NO_BLENDING);
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
    @Serial
    private static final long serialVersionUID = 8048288443738661480L;
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
        platform.getViewport().setAspectMode(AspectMode.IGNORE_RESIZING);
      } else if (keyboardInput.isKeyPressed(Key.TWO)) {
        platform.getViewport().setAspectMode(AspectMode.MAINTAIN);
      } else if (keyboardInput.isKeyPressed(Key.THREE)) {
        platform.getViewport().setAspectMode(AspectMode.STRETCH);
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
        Layer blockLayer = platform.getRenderer().getLayer("blocks");
        blockLayer.setEnabled(!blockLayer.isEnabled());

        Layer backgroundLayer = platform.getRenderer().getLayer("background");
        backgroundLayer.setEnabled(!backgroundLayer.isEnabled());
      }
    }

    @Override
    public int getOrder() {
      return 0;
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
      new LayerComponent("moving_stuff"),
      new TransformComponent(0, 0),
      new SpriteComponent("test", "stick_figure"),
      new BlendModeComponent(BlendMode.MASK),
      new MovingComponent()
    );

    world.createEntity()
      .addComponent(new LayerComponent("moving_stuff"))
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
