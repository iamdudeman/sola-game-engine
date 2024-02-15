package technology.sola.engine.examples.common.singlefile;

import technology.sola.ecs.World;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CameraComponent;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.SectionGuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.elements.input.TextInputGuiElement;
import technology.sola.engine.graphics.gui.elements.input.TextInputStyles;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.engine.graphics.gui.style.property.Direction;
import technology.sola.engine.graphics.gui.style.theme.GuiTheme;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.physics.Material;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.system.collision.QuadTreeCollisionDetectionBroadPhase;
import technology.sola.engine.physics.system.collision.SpacialHashMapCollisionDetectionBroadPhase;

import java.util.List;
import java.util.Random;

/**
 * StressTestPhysicsExample is a {@link technology.sola.engine.core.Sola} for demoing and stress testing physics for the
 * sola game engine.
 *
 * <ul>
 *   <li>{@link technology.sola.engine.defaults.SolaPhysics}</li>
 *   <li>{@link technology.sola.engine.physics.system.PhysicsSystem}</li>
 *   <li>{@link technology.sola.engine.physics.system.GravitySystem}</li>
 *   <li>{@link technology.sola.engine.physics.system.CollisionDetectionSystem}</li>
 *   <li>{@link technology.sola.engine.physics.system.ImpulseCollisionResolutionSystem}</li>
 *   <li>{@link DynamicBodyComponent}</li>
 * </ul>
 */
public class StressTestPhysicsExample extends SolaWithDefaults {
  private static final float CAMERA_SCALE = 1.2f;
  private static final float CIRCLE_RADIUS = 10f;
  private final int objectCount;
  private Random random;

  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   *
   * @param objectCount  the count of physics objects to create
   */
  public StressTestPhysicsExample(int objectCount) {
    super(new SolaConfiguration("Stress Test - Physics", 1200, 800));
    this.objectCount = objectCount;

    resetRandom("123456789");
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGraphics().usePhysics().useDebug().useGui();

    platform.getViewport().setAspectMode(AspectMode.MAINTAIN);

    solaEcs.setWorld(buildSpacialHashMapOptimizedWorld());

    guiDocument.setRootElement(buildGui());
  }

  private GuiElement<?> buildGui() {
    SectionGuiElement sectionGuiElement = new SectionGuiElement();

    sectionGuiElement.setStyle(List.of(
      ConditionalStyle.always(BaseStyles.create().setDirection(Direction.ROW).setPadding(5).setGap(5).build())
    ));

    TextInputGuiElement randomSeedInput = new TextInputGuiElement().setValue("123456789").setPlaceholder("Random");

    randomSeedInput.setStyle(List.of(ConditionalStyle.always(TextInputStyles.create().setPadding(5).build())));

    sectionGuiElement.appendChildren(
      new ButtonGuiElement()
        .setOnAction(() -> {
          resetRandom(randomSeedInput.getValue());
          solaPhysics.getCollisionDetectionSystem().setCollisionDetectionBroadPhase(
            new SpacialHashMapCollisionDetectionBroadPhase()
          );
          solaEcs.setWorld(buildSpacialHashMapOptimizedWorld());
        })
        .setStyle(List.of(ConditionalStyle.always(BaseStyles.create().setPadding(5).build())))
        .appendChildren(new TextGuiElement().setText("SpacialHashMap")),
      new ButtonGuiElement()
        .setOnAction(() -> {
          resetRandom(randomSeedInput.getValue());
          solaPhysics.getCollisionDetectionSystem().setCollisionDetectionBroadPhase(
            new QuadTreeCollisionDetectionBroadPhase()
          );
          solaEcs.setWorld(buildQuadTreeOptimizedWorld());
        })
        .setStyle(List.of(ConditionalStyle.always(BaseStyles.create().setPadding(5).build())))
        .appendChildren(new TextGuiElement().setText("QuadTree")),
      new ButtonGuiElement()
        .setOnAction(() -> {
          randomSeedInput.setValue("" + new Random().nextLong());
        })
        .setStyle(List.of(ConditionalStyle.always(BaseStyles.create().setPadding(5).build())))
        .appendChildren(new TextGuiElement().setText("New seed")),
      randomSeedInput
    );

    GuiTheme.getDefaultLightTheme().applyToTree(sectionGuiElement);

    return sectionGuiElement;
  }

  private World buildQuadTreeOptimizedWorld() {
    Material circleMaterial = new Material(1, 0.8f);
    float zoomedWidth = configuration.rendererWidth() / CAMERA_SCALE;
    float zoomedHeight = configuration.rendererHeight() / CAMERA_SCALE;
    float squareSide = CIRCLE_RADIUS;

    World world = new World(objectCount + 4);

    world.createEntity()
      .addComponent(new TransformComponent(0, 0, CAMERA_SCALE, CAMERA_SCALE))
      .addComponent(new CameraComponent());

    for (int i = 0; i < objectCount; i++) {
      float x = random.nextFloat() * (zoomedWidth - 4 * CIRCLE_RADIUS) + 2 * CIRCLE_RADIUS;
      float y = random.nextFloat() * (zoomedHeight - 4 * CIRCLE_RADIUS) + 2 * CIRCLE_RADIUS;
      boolean isKinematic = random.nextFloat() > 0.9f;

      world.createEntity()
        .addComponent(new TransformComponent(x, y, CIRCLE_RADIUS))
        .addComponent(new DynamicBodyComponent(circleMaterial, isKinematic))
        .addComponent(new CircleRendererComponent(isKinematic ? Color.WHITE : Color.BLUE, true))
        .addComponent(ColliderComponent.circle());
    }

    world.createEntity(
      new TransformComponent(0, 0, squareSide, zoomedHeight),
      ColliderComponent.aabb()
    );
    world.createEntity(
      new TransformComponent(zoomedWidth - squareSide, 0, squareSide, zoomedHeight),
      ColliderComponent.aabb()
    );
    world.createEntity(
      new TransformComponent(0, zoomedHeight - squareSide, zoomedWidth, squareSide),
      ColliderComponent.aabb()
    );

    return world;
  }

  private World buildSpacialHashMapOptimizedWorld() {
    Material circleMaterial = new Material(1, 0.8f);
    float zoomedWidth = configuration.rendererWidth() / CAMERA_SCALE;
    float zoomedHeight = configuration.rendererHeight() / CAMERA_SCALE;
    float squareSide = CIRCLE_RADIUS;
    int bottomPlatformEntityCount = Math.round(zoomedWidth / squareSide) + 1;
    int sidePlatformEntityCount = Math.round(zoomedHeight / squareSide) * 2 + 2;

    World world = new World(objectCount + bottomPlatformEntityCount + sidePlatformEntityCount + 1);

    world.createEntity()
      .addComponent(new TransformComponent(0, 0, CAMERA_SCALE, CAMERA_SCALE))
      .addComponent(new CameraComponent());

    for (int i = 0; i < objectCount; i++) {
      float x = random.nextFloat() * (zoomedWidth - 4 * CIRCLE_RADIUS) + 2 * CIRCLE_RADIUS;
      float y = random.nextFloat() * (zoomedHeight - 4 * CIRCLE_RADIUS) + 2 * CIRCLE_RADIUS;
      boolean isKinematic = random.nextFloat() > 0.9f;

      world.createEntity()
        .addComponent(new TransformComponent(x, y, CIRCLE_RADIUS))
        .addComponent(new DynamicBodyComponent(circleMaterial, isKinematic))
        .addComponent(new CircleRendererComponent(isKinematic ? Color.WHITE : Color.BLUE, true))
        .addComponent(ColliderComponent.circle());
    }

    for (int i = 0; i < zoomedHeight; i += squareSide) {
      world.createEntity()
        .addComponent(new TransformComponent(0, i, squareSide, squareSide))
        .addComponent(ColliderComponent.aabb());
    }

    for (int i = 0; i < zoomedHeight; i += squareSide) {
      world.createEntity()
        .addComponent(new TransformComponent(zoomedWidth - squareSide, i, squareSide, squareSide))
        .addComponent(ColliderComponent.aabb());
    }

    for (int i = 0; i < zoomedWidth; i += squareSide) {
      world.createEntity()
        .addComponent(new TransformComponent(i, zoomedHeight - squareSide, squareSide, squareSide))
        .addComponent(ColliderComponent.aabb());
    }

    return world;
  }

  private void resetRandom(String randomSeed) {
    this.random = randomSeed.isEmpty() ? new Random() : new Random(Long.parseLong(randomSeed));
  }
}
