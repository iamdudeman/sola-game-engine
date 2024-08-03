package technology.sola.engine.examples.common;

import technology.sola.ecs.World;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.graphics.components.TriangleRendererComponent;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.component.collider.ColliderShapeAABB;
import technology.sola.engine.physics.component.collider.ColliderShapeCircle;
import technology.sola.engine.physics.component.collider.ColliderShapeTriangle;
import technology.sola.math.geometry.Triangle;
import technology.sola.math.linear.Vector2D;

/**
 * SmallPhysicsTestSola is a {@link Sola} for isolated testing of various physics things.
 */
public class SmallPhysicsTestSola extends SolaWithDefaults {
  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public SmallPhysicsTestSola() {
    super(SolaConfiguration.build("Small Physics Test", 800, 600).build());
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGraphics().usePhysics().useDebug();

    World world = new World(10);

    world.createEntity()
      .addComponent(new TransformComponent(180, 100, 50))
      .addComponent(new DynamicBodyComponent())
      .addComponent(new CircleRendererComponent(Color.GREEN))
      .addComponent(new ColliderComponent(new ColliderShapeCircle()));

    world.createEntity()
      .addComponent(new TransformComponent(250, 100, 50))
      .addComponent(new DynamicBodyComponent())
      .addComponent(new RectangleRendererComponent(Color.GREEN))
      .addComponent(new ColliderComponent(new ColliderShapeAABB()));

    world.createEntity()
      .addComponent(new TransformComponent(230, 270, 10))
      .addComponent(new DynamicBodyComponent())
      .addComponent(new RectangleRendererComponent(Color.GREEN))
      .addComponent(new ColliderComponent(new ColliderShapeAABB()));

    world.createEntity()
      .addComponent(new TransformComponent(210, 250, 50))
      .addComponent(new TriangleRendererComponent(Color.WHITE))
      .addComponent(new ColliderComponent(new ColliderShapeTriangle()));


    Triangle staticTriangle = new Triangle(new Vector2D(0, -30), new Vector2D(60, -80), new Vector2D(120, 0));

    world.createEntity()
      .addComponent(new TransformComponent(490, 100, 50))
      .addComponent(new DynamicBodyComponent())
      .addComponent(new CircleRendererComponent(Color.GREEN))
      .addComponent(new ColliderComponent(new ColliderShapeCircle()));

    world.createEntity()
      .addComponent(new TransformComponent(580, 100, 50))
      .addComponent(new DynamicBodyComponent())
      .addComponent(new RectangleRendererComponent(Color.GREEN))
      .addComponent(new ColliderComponent(new ColliderShapeAABB()));

    world.createEntity()
      .addComponent(new TransformComponent(550, 210, 10))
      .addComponent(new DynamicBodyComponent())
      .addComponent(new CircleRendererComponent(Color.GREEN))
      .addComponent(new ColliderComponent(new ColliderShapeCircle()));

    world.createEntity()
      .addComponent(new TransformComponent(510, 250, 1))
      .addComponent(new TriangleRendererComponent(Color.WHITE, staticTriangle))
      .addComponent(new ColliderComponent(new ColliderShapeTriangle(staticTriangle)));


    solaEcs.setWorld(world);
  }
}
