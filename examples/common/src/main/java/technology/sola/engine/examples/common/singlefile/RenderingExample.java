package technology.sola.engine.examples.common.singlefile;

import technology.sola.ecs.Entity;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Component;
import technology.sola.ecs.World;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Layer;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.core.graphics.SolaGraphics;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.graphics.gui.components.GuiPanelComponent;
import technology.sola.engine.graphics.gui.components.GuiTextComponent;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.graphics.sprite.SpriteSheet;
import technology.sola.engine.input.Key;
import technology.sola.math.linear.Vector2D;

import java.io.Serial;
import java.util.List;

public class RenderingExample extends Sola {
  private SolaGraphics solaGraphics;

  @Override
  protected SolaConfiguration getConfiguration() {
    return new SolaConfiguration("Rendering Example", 800, 600, 30, true);
  }

  @Override
  protected void onInit() {
    solaGraphics = SolaGraphics.use(solaEcs, platform.getRenderer(), assetPoolProvider);

    assetPoolProvider.getAssetPool(SpriteSheet.class)
      .addAssetId("test", "assets/test_tiles_spritesheet.json");
    assetPoolProvider.getAssetPool(Font.class)
      .addAssetId("default", "assets/monospaced_NORMAL_18.json");

    solaEcs.addSystem(new TestSystem());
    solaEcs.setWorld(createWorld());

    platform.getRenderer().createLayers("background", "moving_stuff", "blocks", "ui");
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
    });

    solaGraphics.render();
  }

  private record MovingComponent() implements Component {
    @Serial
    private static final long serialVersionUID = 8048288443738661480L;
  }

  private class TestSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      world.createView().of(TransformComponent.class, MovingComponent.class)
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
        .orElseThrow()
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
      new TransformComponent(vector2D.x, vector2D.y, movingEntity),
      new RectangleRendererComponent(Color.RED))
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

    // ui
    Entity uiParentEntity = world.createEntity()
      .addComponent(new LayerComponent("ui"))
      .addComponent(new TransformComponent(0, 0, 600, 100))
      .addComponent(new GuiPanelComponent(new Color(120, 255, 255, 255), Color.YELLOW));

    final String characters1 = "!\"#$%&'()*+,-./0123456789:; <=>?@ABCDEFGHIJKLMN";
    final String characters2 = "OPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

    world.createEntity()
      .addComponent(new LayerComponent("ui"))
      .addComponent(new TransformComponent(5, 5, uiParentEntity))
      .addComponent(new GuiTextComponent("default", characters1, Color.RED));

    world.createEntity()
      .addComponent(new LayerComponent("ui"))
      .addComponent(new TransformComponent(5, 35, uiParentEntity))
      .addComponent(new GuiTextComponent("default", characters2, Color.BLACK));

    Entity subPanel = world.createEntity(
      new LayerComponent("ui"),
      new TransformComponent(100, 65, 0.5f, 0.25f, uiParentEntity),
      new GuiPanelComponent(Color.WHITE)
    );

    world.createEntity()
      .addComponent(new LayerComponent("ui"))
      .addComponent(new TransformComponent(2, 2, subPanel))
      .addComponent(new GuiTextComponent("default", "Hello world!", Color.BLUE));

    return world;
  }
}
