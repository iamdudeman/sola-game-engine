package technology.sola.engine.examples.common.features;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.World;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.examples.common.ExampleUtils;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.BlendModeComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.physics.component.particle.ParticleEmitterComponent;
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
    solaGraphics = new SolaGraphics.Builder(platform(), solaEcs)
      .withGui(mouseInput)
      .buildAndInitialize(assetLoaderProvider);

    solaGraphics.guiDocument().setRootElement(
      ExampleUtils.createReturnToLauncherButton(platform(), eventHub, "0", "0")
    );

    platform().getViewport().setAspectMode(AspectMode.MAINTAIN);

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

    sparksParticleEmitterComponent
      .configureAppearance().setColor(new Color(210, 80, 45)).setSizeBounds(6, 12).done()
      .configureMovement().setVelocityBounds(new Vector2D(-18f, -70f), new Vector2D(18f, 0)).done()
      .configureEmission().setCountPerEmit(1).setLifeBounds(1, 3);

    sparksParticleEmitterComponent.setParticleEmissionDelay(0.01f);

    return sparksParticleEmitterComponent;
  }

  private static ParticleEmitterComponent buildFireParticleEmitterComponent() {
    ParticleEmitterComponent fireParticleEmitterComponent = new ParticleEmitterComponent();

    fireParticleEmitterComponent
      .configureAppearance().setColor(new Color(230, 40, 45)).setSizeBounds(6, 10).done()
      .configureMovement().setVelocityBounds(new Vector2D(-18f, -70f), new Vector2D(18f, 0)).done()
      .configureEmission().setCountPerEmit(10).setLifeBounds(1, 1);

    fireParticleEmitterComponent.setParticleEmissionDelay(0.1f);

    return fireParticleEmitterComponent;
  }
}
