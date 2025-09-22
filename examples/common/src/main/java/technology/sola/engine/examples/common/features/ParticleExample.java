package technology.sola.engine.examples.common.features;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.World;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaGraphics;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.BlendModeComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.component.ParticleEmitterComponent;
import technology.sola.engine.physics.system.ParticleSystem;
import technology.sola.math.linear.Vector2D;

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
    super(new SolaConfiguration("Particle Example", 800, 600, 30));
  }

  @Override
  protected void onInit() {
    ExampleLauncherSola.addReturnToLauncherKeyEvent(platform(), eventHub);

    solaGraphics = new SolaGraphics.Builder(platform(), solaEcs, mouseInput)
      .buildAndInitialize(assetLoaderProvider);

    solaEcs.setWorld(buildWorld());
    solaEcs.addSystem(new ParticleSystem());
  }

  @Override
  protected void onRender(Renderer renderer) {
    solaGraphics.render(renderer);
  }

  private World buildWorld() {
    World world = new World(5);

    world.createEntity()
      .addComponent(new ParticleEmitterComponent())
      .addComponent(new BlendModeComponent(BlendMode.NORMAL))
      .addComponent(new TransformComponent(100, 500));

    world.createEntity()
      .addComponent(buildFireParticleEmitterComponent())
      .addComponent(new BlendModeComponent(BlendMode.LINEAR_DODGE))
      .addComponent(new TransformComponent(250, 500));

    world.createEntity()
      .addComponent(buildSparksParticleEmitterComponent())
      .addComponent(new BlendModeComponent(BlendMode.DISSOLVE))
      .addComponent(new TransformComponent(400, 500));

    return world;
  }

  private static ParticleEmitterComponent buildSparksParticleEmitterComponent() {
    ParticleEmitterComponent sparksParticleEmitterComponent = new ParticleEmitterComponent();

    sparksParticleEmitterComponent.setParticleColor(new Color(210, 80, 45));
    sparksParticleEmitterComponent.setParticleSizeBounds(6, 12);
    sparksParticleEmitterComponent.setParticleLifeBounds(1, 3);
    sparksParticleEmitterComponent.setParticleVelocityBounds(new Vector2D(-18f, -70f), new Vector2D(18f, 0));
    sparksParticleEmitterComponent.setParticleEmissionDelay(0.01f);
    sparksParticleEmitterComponent.setParticlesPerEmit(1);

    return sparksParticleEmitterComponent;
  }

  private static ParticleEmitterComponent buildFireParticleEmitterComponent() {
    ParticleEmitterComponent fireParticleEmitterComponent = new ParticleEmitterComponent();

    fireParticleEmitterComponent.setParticleColor(new Color(230, 40, 45));
    fireParticleEmitterComponent.setParticleSizeBounds(6, 10);
    fireParticleEmitterComponent.setParticleLifeBounds(1, 1);
    fireParticleEmitterComponent.setParticleVelocityBounds(new Vector2D(-18f, -70f), new Vector2D(18f, 0));
    fireParticleEmitterComponent.setParticleEmissionDelay(0.1f);
    fireParticleEmitterComponent.setParticlesPerEmit(10);

    return fireParticleEmitterComponent;
  }
}
