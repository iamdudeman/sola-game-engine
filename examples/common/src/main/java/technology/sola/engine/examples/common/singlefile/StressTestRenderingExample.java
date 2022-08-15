package technology.sola.engine.examples.common.singlefile;

import technology.sola.ecs.World;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.core.module.graphics.SolaGraphics;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.system.PhysicsSystem;
import technology.sola.math.linear.Vector2D;

import java.util.Random;

public class StressTestRenderingExample extends Sola {
  private SolaGraphics solaGraphics;

  @Override
  protected SolaConfiguration getConfiguration() {
    return new SolaConfiguration("Stress Test - Rendering", 800, 600, 30, true);
  }

  @Override
  protected void onInit() {
    solaGraphics = SolaGraphics.createInstance(solaEcs, platform.getRenderer(), assetLoaderProvider);

    solaEcs.addSystems(new PhysicsSystem());
    solaEcs.setWorld(buildWorld());
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGraphics.render();
  }

  private World buildWorld() {
    Random random = new Random();
    World world = new World(10000);

    for (int i = 0; i < world.getMaxEntityCount(); i++) {
      int x = random.nextInt(0, 800);
      int y = random.nextInt(0, 600);
      int scale = random.nextInt(3, 20);

      int r = random.nextInt(0, 256);
      int g = random.nextInt(0, 256);
      int b = random.nextInt(0, 256);
      boolean filled = random.nextBoolean();

      float velX = random.nextFloat(-50, 50);
      float velY = random.nextFloat(-50, 50);

      DynamicBodyComponent dynamicBodyComponent = new DynamicBodyComponent(true);

      dynamicBodyComponent.setVelocity(new Vector2D(velX, velY));

      world.createEntity(
        new TransformComponent(x, y, scale),
        dynamicBodyComponent,
        new CircleRendererComponent(new Color(r, g, b), filled)
      );
    }

    return world;
  }
}
