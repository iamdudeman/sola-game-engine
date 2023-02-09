package technology.sola.engine.examples.common.singlefile;

import technology.sola.ecs.World;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.defaults.SolaWithDefaults;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.system.PhysicsSystem;
import technology.sola.math.linear.Vector2D;

import java.util.Random;

public class StressTestRenderingExample extends SolaWithDefaults {
  public StressTestRenderingExample() {
    super(SolaConfiguration.build("Stress Test - Rendering", 800, 600).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGraphics();

    solaEcs.addSystems(new PhysicsSystem());
    solaEcs.setWorld(buildWorld());
  }

  private World buildWorld() {
    Random random = new Random();
    World world = new World(10000);

    for (int i = 0; i < world.getMaxEntityCount(); i++) {
      int x = random.nextInt(0, 800);
      int y = random.nextInt(0, 600);
      int scale = random.nextInt(3, 20);

      int red = random.nextInt(0, 256);
      int green = random.nextInt(0, 256);
      int blue = random.nextInt(0, 256);
      boolean filled = random.nextBoolean();

      float velX = random.nextFloat(-50, 50);
      float velY = random.nextFloat(-50, 50);

      DynamicBodyComponent dynamicBodyComponent = new DynamicBodyComponent(true);

      dynamicBodyComponent.setVelocity(new Vector2D(velX, velY));

      world.createEntity(
        new TransformComponent(x, y, scale),
        dynamicBodyComponent,
        new CircleRendererComponent(new Color(red, green, blue), filled)
      );
    }

    return world;
  }
}
