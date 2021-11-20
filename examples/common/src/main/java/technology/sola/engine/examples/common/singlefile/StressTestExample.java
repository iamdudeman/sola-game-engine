package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.World;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.physics.Material;
import technology.sola.engine.physics.SolaPhysics;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;

import java.util.Random;

public class StressTestExample extends AbstractSola {
  private static final float CAMERA_SCALE = 1f;
  private static final float CIRCLE_RADIUS = 5f;
  private final Random random = new Random();
  private final int objectCount;
  private SolaPhysics solaPhysics;
  private SolaGraphics solaGraphics;

  public StressTestExample(int objectCount) {
    this.objectCount = objectCount;
  }

  @Override
  protected SolaConfiguration buildConfiguration() {
    return new SolaConfiguration("Stress Test", 800, 600, 60, false);
  }

  @Override
  protected void onInit() {
    solaPhysics = new SolaPhysics(eventHub);
    solaGraphics = new SolaGraphics(ecsSystemContainer, platform.getRenderer(), null);

    solaPhysics.addEcsSystems(ecsSystemContainer);

    ecsSystemContainer.setWorld(buildWorld());
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGraphics.render();
    solaPhysics.renderDebug(renderer, ecsSystemContainer.getWorld(), Color.RED, Color.GREEN);
  }

  private World buildWorld() {
    Material circleMaterial = new Material(1, 0.8f);
    float zoomedWidth = configuration.getCanvasWidth() / CAMERA_SCALE;
    float zoomedHeight = configuration.getCanvasHeight() / CAMERA_SCALE;
    float squareSide = CIRCLE_RADIUS * 2;
    int bottomPlatformEntityCount = Math.round(zoomedWidth / squareSide) + 1;
    int sidePlatformEntityCount = Math.round(zoomedHeight / squareSide) * 2 + 2;

    World world = new World(objectCount + bottomPlatformEntityCount + sidePlatformEntityCount);

    for (int i = 0; i < zoomedHeight; i += squareSide) {
      world.createEntity()
        .addComponent(new TransformComponent(0, i))
        .addComponent(ColliderComponent.rectangle(squareSide, squareSide));
    }

    for (int i = 0; i < zoomedHeight; i += squareSide) {
      world.createEntity()
        .addComponent(new TransformComponent(zoomedWidth - squareSide, i))
        .addComponent(ColliderComponent.rectangle(squareSide, squareSide));
    }

    for (int i = 0; i < zoomedWidth; i += squareSide) {
      world.createEntity()
        .addComponent(new TransformComponent(i, zoomedHeight - squareSide))
        .addComponent(ColliderComponent.rectangle(squareSide, squareSide));
    }

    for (int i = 0; i < objectCount; i++) {
      float x = random.nextFloat() * (zoomedWidth - CIRCLE_RADIUS) + CIRCLE_RADIUS;
      float y = random.nextFloat() * (zoomedHeight - CIRCLE_RADIUS) + CIRCLE_RADIUS;
      boolean isKinematic = random.nextFloat() > 0.9f;

      world.createEntity()
        .addComponent(new TransformComponent(x, y))
        .addComponent(new DynamicBodyComponent(circleMaterial, isKinematic))
        .addComponent(new CircleRendererComponent(isKinematic ? Color.WHITE : Color.BLUE, true))
        .addComponent(ColliderComponent.circle(CIRCLE_RADIUS));
    }

    return world;
  }
}
