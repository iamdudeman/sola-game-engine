package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.AbstractEcsSystem;
import technology.sola.engine.ecs.World;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Layer;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.Key;
import technology.sola.engine.physics.component.PositionComponent;

public class TempExample extends AbstractSola {
  private SolaGraphics solaGraphics;

  @Override
  protected SolaConfiguration buildConfiguration() {
    return new SolaConfiguration("Temp", 800, 600, 30, true);
  }

  @Override
  protected void onInit() {
    ecsSystemContainer.add(new TestSystem());
    ecsSystemContainer.setWorld(createWorld());

    platform.getViewport().setAspectMode(AspectMode.STRETCH);
    platform.getRenderer().createLayers("background", "moving_stuff", "blocks", "ui");

    solaGraphics = new SolaGraphics(ecsSystemContainer, platform.getRenderer());
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGraphics.render();
  }

  private World createWorld() {
    World world = new World(50);

    world.createEntity()
      .addComponent(new LayerComponent("moving_stuff"))
      .addComponent(new TransformComponent(0, 0, 5, 5))
      .addComponent(new RectangleRendererComponent(Color.RED));
    world.createEntity()
      .addComponent(new LayerComponent("moving_stuff"))
      .addComponent(new TransformComponent(50, 20, 5, 5))
      .addComponent(new RectangleRendererComponent(Color.RED));
    world.createEntity()
      .addComponent(new LayerComponent("moving_stuff"))
      .addComponent(new TransformComponent(100, 20, 5, 5))
      .addComponent(new RectangleRendererComponent(Color.RED));
    world.createEntity()
      .addComponent(new LayerComponent("moving_stuff"))
      .addComponent(new TransformComponent(150, 20, 5, 5))
      .addComponent(new RectangleRendererComponent(Color.RED));
    world.createEntity()
      .addComponent(new LayerComponent("moving_stuff"))
      .addComponent(new TransformComponent(200, 20, 5, 5))
      .addComponent(new RectangleRendererComponent(Color.RED));

    world.createEntity()
        .addComponent(new LayerComponent("blocks"))
        .addComponent(new TransformComponent(200, 300, 5, 5))
        .addComponent(new RectangleRendererComponent(Color.BLUE));
    world.createEntity()
      .addComponent(new LayerComponent("blocks"))
      .addComponent(new TransformComponent(200, 350, 10, 5))
      .addComponent(new RectangleRendererComponent(Color.BLUE));
   /*
   renderer.drawToLayer("blocks", r -> {
      renderer.fillRect(200, 300, 50, 50, Color.BLUE);
      renderer.fillRect(200, 350, 100, 50, Color.BLUE);
    });
    */






    world.createEntity()
      .addComponent(new TransformComponent())
      .addComponent(new RectangleRendererComponent(Color.GREEN, false));

    for (int i = 0; i < 40; i++) {
      world.createEntity()
        .addComponent(new TransformComponent(50 + i, 50 + i, 1.5f, 1))
        .addComponent(new RectangleRendererComponent(Color.WHITE));
    }

    world.createEntity()
      .addComponent(new TransformComponent(20, 100, 5, 8))
      .addComponent(new RectangleRendererComponent(Color.BLUE));

    world.createEntity()
      .addComponent(new TransformComponent(200, 50, 5, 8))
      .addComponent(new CircleRendererComponent(Color.BLUE));

    return world;
  }


  private class TestSystem extends AbstractEcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      world.getEntitiesWithComponents(TransformComponent.class, LayerComponent.class)
        .forEach(entity -> {
          if (entity.getComponent(LayerComponent.class).getLayer().equals("moving_stuff")) {
            TransformComponent transformComponent = entity.getComponent(TransformComponent.class);


            transformComponent.setX(transformComponent.getX() + 1);
            transformComponent.setY(transformComponent.getY() + 1);
          }
        });

      if (keyboardInput.isKeyPressed(Key.ONE)) {
        platform.getViewport().setAspectMode(AspectMode.IGNORE_RESIZING);
      } else if (keyboardInput.isKeyPressed(Key.TWO)) {
        platform.getViewport().setAspectMode(AspectMode.MAINTAIN);
      } else if (keyboardInput.isKeyPressed(Key.THREE)) {
        platform.getViewport().setAspectMode(AspectMode.STRETCH);
      }

//      if (keyboardInput.isKeyHeld(Key.SPACE)) {
//        rotation = rotation + 0.1f;
//      }

      if (keyboardInput.isKeyPressed(Key.A)) {
        Layer blockLayer = platform.getRenderer().getLayer("blocks");
        blockLayer.setEnabled(!blockLayer.isEnabled());
      }
    }

    @Override
    public int getOrder() {
      return 0;
    }
  }
}
