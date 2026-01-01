package technology.sola.engine.examples.common.features;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.examples.common.ExampleUtils;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.*;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.SectionGuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.property.Direction;
import technology.sola.engine.graphics.gui.style.theme.DefaultThemeBuilder;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.physics.component.particle.ParticleEmitterComponent;
import technology.sola.engine.physics.component.particle.appearance.ParticleShape;
import technology.sola.engine.physics.component.particle.emitter.CircleEmitterShape;
import technology.sola.engine.physics.component.particle.emitter.TrapezoidEmitterShape;
import technology.sola.engine.physics.component.particle.emitter.TriangleEmitterShape;
import technology.sola.engine.physics.component.particle.emitter.RectangleEmitterShape;
import technology.sola.engine.physics.component.particle.movement.ParticleNoise;
import technology.sola.engine.physics.system.ParticleSystem;
import technology.sola.math.geometry.ConvexPolygon;
import technology.sola.math.geometry.Triangle;
import technology.sola.math.linear.Vector2D;

import java.util.function.Consumer;

/**
 * ParticleExample is a {@link technology.sola.engine.core.Sola} for demoing particles for the sola game engine.
 *
 * <ul>
 *   <li>{@link ParticleSystem}</li>
 *   <li>{@link ParticleEmitterComponent}</li>
 * </ul>
 */
@NullMarked
public class ParticleExample extends Sola {
  private SolaGraphics solaGraphics;

  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public ParticleExample() {
    super(new SolaConfiguration("Particle Example", 600, 500, 30));
  }

  @Override
  protected void onInit() {
    solaGraphics = new SolaGraphics.Builder(platform(), solaEcs)
      .withGui(mouseInput)
      .buildAndInitialize(assetLoaderProvider);

    platform().getViewport().setAspectMode(AspectMode.MAINTAIN);

    var world = buildWorld();

    solaEcs.setWorld(world);
    solaEcs.addSystem(new ParticleSystem());

    solaGraphics.guiDocument()
      .setRootElement(buildGui(world));
  }

  @Override
  protected void onRender(Renderer renderer) {
    solaGraphics.render(renderer);
  }

  private World buildWorld() {
    World world = new World(5);

    setEmitterToDefault(
      world.createEntity().setName("emitter")
    );

    return world;
  }

  private GuiElement<?, ?> buildGui(World world) {
    var section = new SectionGuiElement()
      .addStyle(new BaseStyles.Builder<>().setDirection(Direction.COLUMN).setGap(2).setPadding(2).build())
      .appendChildren(
        buildFirstRow(world),
        buildSecondRow(world)
      );

    DefaultThemeBuilder.buildDarkTheme().applyToTree(section);

    return section;
  }

  private GuiElement<?, ?> buildFirstRow(World world) {
    return new SectionGuiElement()
      .addStyle(new BaseStyles.Builder<>().setDirection(Direction.ROW).setGap(2).build())
      .appendChildren(
        new ButtonGuiElement()
          .addStyle(new BaseStyles.Builder<>().setPadding(4).build())
          .setOnAction(() -> ExampleUtils.returnToLauncher(platform(), eventHub))
          .appendChildren(
            new TextGuiElement()
              .setText("Back")
          ),
        buildParticleOptionButton(world, "Default", ParticleExample::setEmitterToDefault),
        buildParticleOptionButton(world, "Shell", ParticleExample::setEmitterToShell),
        buildParticleOptionButton(world, "Triangle", ParticleExample::setEmitterToTriangle),
        buildParticleOptionButton(world, "Circle", ParticleExample::setEmitterToCircle),
        buildParticleOptionButton(world, "Rectangle", ParticleExample::setEmitterToRectangle),
        buildParticleOptionButton(world, "Random", ParticleExample::setEmitterToRandom),
        buildParticleOptionButton(world, "Noise", ParticleExample::setEmitterToNoise)
      );
  }

  private GuiElement<?, ?> buildSecondRow(World world) {
    return new SectionGuiElement()
      .addStyle(new BaseStyles.Builder<>().setDirection(Direction.ROW).setGap(2).build())
      .appendChildren(
        buildParticleOptionButton(world, "Shapes", ParticleExample::setEmitterToShapes),
        buildParticleOptionButton(world, "Burst", ParticleExample::setEmitterToBurst),
        buildParticleOptionButton(world, "Fire", ParticleExample::setEmitterToFire),
        buildParticleOptionButton(world, "Sparks", ParticleExample::setEmitterToSparks),
        buildParticleOptionButton(world, "Snow", ParticleExample::setEmitterToSnow)
      );
  }

  private ButtonGuiElement buildParticleOptionButton(World world, String label, Consumer<Entity> configure) {
    return new ButtonGuiElement()
      .addStyle(new BaseStyles.Builder<>().setPadding(4).build())
      .setOnAction(() -> configure.accept(world.findEntityByName("emitter")))
      .appendChildren(new TextGuiElement().setText(label));
  }

  private static void setEmitterToDefault(Entity entity) {
    var defaultEmitter = new ParticleEmitterComponent();

    cleanupEntity(entity);

    entity.addComponent(defaultEmitter)
      .addComponent(new ConvexPolygonRendererComponent(Color.YELLOW, false, new ConvexPolygon(
        new Vector2D[] {
          new Vector2D(0, 0), new Vector2D(-20, 0),
          new Vector2D(-50, -100), new Vector2D(30, -100),
        }
      )))
      .addComponent(new BlendModeComponent(BlendMode.NORMAL))
      .addComponent(new TransformComponent(250, 300, 1));
  }

  private static void setEmitterToShell(Entity entity) {
    var shellEmitter = new ParticleEmitterComponent();

    shellEmitter.emissionConfig().shape().setEmitFromShell(true);

    cleanupEntity(entity);

    entity.addComponent(shellEmitter)
      .addComponent(new ConvexPolygonRendererComponent(Color.YELLOW, false, new ConvexPolygon(
        new Vector2D[] {
          new Vector2D(0, 0), new Vector2D(-20, 0),
          new Vector2D(-50, -100), new Vector2D(30, -100),
        }
      )))
      .addComponent(new BlendModeComponent(BlendMode.NORMAL))
      .addComponent(new TransformComponent(250, 300, 1));
  }

  private static void setEmitterToTriangle(Entity entity) {
    var shellEmitter = new ParticleEmitterComponent();

    shellEmitter.emissionConfig().setShape(new TriangleEmitterShape(
      new Vector2D(0, -1f), 100, 40
    ));

    cleanupEntity(entity);

    entity.addComponent(shellEmitter)
      .addComponent(new TriangleRendererComponent(Color.YELLOW, false, new Triangle(
        new Vector2D(0, 0), new Vector2D(-20, -100), new Vector2D(20, -100)
      )))
      .addComponent(new BlendModeComponent(BlendMode.NORMAL))
      .addComponent(new TransformComponent(250, 300, 1));
  }

  private static void setEmitterToCircle(Entity entity) {
    var shellEmitter = new ParticleEmitterComponent();

    shellEmitter.emissionConfig().setShape(new CircleEmitterShape(50));

    cleanupEntity(entity);

    entity.addComponent(shellEmitter)
      .addComponent(new CircleRendererComponent(Color.YELLOW, false))
      .addComponent(new BlendModeComponent(BlendMode.NORMAL))
      .addComponent(new TransformComponent(250, 300, 100));
  }

  private static void setEmitterToRectangle(Entity entity) {
    var emitter = new ParticleEmitterComponent();

    emitter.emissionConfig().setShape(new RectangleEmitterShape(50, 100));

    cleanupEntity(entity);

    entity.addComponent(emitter)
      .addComponent(new RectangleRendererComponent(Color.YELLOW, false))
      .addComponent(new BlendModeComponent(BlendMode.NORMAL))
      .addComponent(new TransformComponent(250, 300, 50, 100));
  }

  private static void setEmitterToRandom(Entity entity) {
    var shellEmitter = new ParticleEmitterComponent();

    shellEmitter.emissionConfig().setShape(new CircleEmitterShape(50));
    shellEmitter.emissionConfig().shape().setRandomDirection(true);

    cleanupEntity(entity);

    entity.addComponent(shellEmitter)
      .addComponent(new CircleRendererComponent(Color.YELLOW, false))
      .addComponent(new BlendModeComponent(BlendMode.NORMAL))
      .addComponent(new TransformComponent(250, 300, 100));
  }

  private static void setEmitterToNoise(Entity entity) {
    var emitter = new ParticleEmitterComponent();

    emitter.emissionConfig().setShape(new CircleEmitterShape(50)).setLifeBounds(2, 5);
    emitter.movementConfig().setNoise(new ParticleNoise(50f, 50f, 0.5f));

    cleanupEntity(entity);

    entity.addComponent(emitter)
      .addComponent(new CircleRendererComponent(Color.YELLOW, false))
      .addComponent(new BlendModeComponent(BlendMode.NORMAL))
      .addComponent(new TransformComponent(250, 300, 100));
  }

  private static void setEmitterToShapes(Entity entity) {
    var emitter = new ParticleEmitterComponent()
      .emissionConfig().setLifeBounds(2, 4).done()
      .appearanceConfig()
        .setShapeFunction(roll -> roll < 0.5f ? ParticleShape.CIRCLE : ParticleShape.SQUARE)
        .setSizeBounds(5, 15)
        .done();

    cleanupEntity(entity);

    entity.addComponent(emitter)
      .addComponent(new BlendModeComponent(BlendMode.NORMAL))
      .addComponent(new TransformComponent(250, 300, 100));
  }

  private static void setEmitterToBurst(Entity entity) {
    var burstEmitter = new ParticleEmitterComponent()
      .emissionConfig().setCycles(4).setInterval(2f).setCountPerEmit(35).done();

    burstEmitter.emissionConfig().setShape(new CircleEmitterShape(50));
    burstEmitter.emissionConfig().shape().setEmitFromShell(true);

    cleanupEntity(entity);

    entity.addComponent(burstEmitter)
      .addComponent(new BlendModeComponent(BlendMode.NORMAL))
      .addComponent(new TransformComponent(250, 300, 100));
  }

  private static void setEmitterToFire(Entity entity) {
    var fireEmitter = new ParticleEmitterComponent()
      .appearanceConfig().setColorFunction(roll -> new Color(230, 40, 45)).setSizeBounds(6, 10).done()
      .movementConfig().setSpeedBounds(34, 60).setNoise(new ParticleNoise(
        15.55f,
        0,
        0.5f
      )).done()
      .emissionConfig().setCountPerEmit(4).setLifeBounds(1, 1.5f).setInterval(0.01f).done();

    cleanupEntity(entity);

    entity.addComponent(fireEmitter)
      .addComponent(new BlendModeComponent(BlendMode.LINEAR_DODGE))
      .addComponent(new TransformComponent(250, 300, 1));
  }

  private static void setEmitterToSparks(Entity entity) {
    var sparksEmitter = new ParticleEmitterComponent()
      .appearanceConfig().setColorFunction(roll -> new Color(210, 80, 45)).setSizeBounds(6, 12).done()
      .movementConfig()
      .setSpeedBounds(20, 48).done()
      .emissionConfig().setCountPerEmit(3).setLifeBounds(1, 2f).setInterval(0.1f).done();

    sparksEmitter.emissionConfig().setShape(new TriangleEmitterShape(
      new Vector2D(0, 1), 40, 20
    ));

    cleanupEntity(entity);

    entity.addComponent(sparksEmitter)
      .addComponent(new BlendModeComponent(BlendMode.DISSOLVE))
      .addComponent(new TransformComponent(250, 300, 1));
  }

  private static void setEmitterToSnow(Entity entity) {
    var sparksEmitter = new ParticleEmitterComponent()
      .appearanceConfig().setSizeBounds(3, 9).done()
      .movementConfig().setSpeedBounds(40, 88).setNoise(new ParticleNoise(
        25.55f,
        0,
        0.5f
      )).done()
      .emissionConfig().setCountPerEmit(3).setLifeBounds(4, 20f).setInterval(0.05f).done();

    sparksEmitter.emissionConfig().setShape(new TrapezoidEmitterShape(
      new Vector2D(0, 1), 500, 200, 500
    ));

    cleanupEntity(entity);

    entity.addComponent(sparksEmitter)
      .addComponent(new BlendModeComponent(BlendMode.NORMAL))
      .addComponent(new TransformComponent(50, 100, 1));
  }

  private static void cleanupEntity(Entity entity) {
    entity.removeComponent(RectangleRendererComponent.class);
    entity.removeComponent(CircleRendererComponent.class);
    entity.removeComponent(TriangleRendererComponent.class);
    entity.removeComponent(ConvexPolygonRendererComponent.class);
  }
}
