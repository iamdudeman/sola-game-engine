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
import technology.sola.engine.graphics.components.BlendModeComponent;
import technology.sola.engine.graphics.components.CircleRendererComponent;
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
import technology.sola.engine.physics.system.ParticleSystem;

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
      .addStyle(new BaseStyles.Builder<>().setDirection(Direction.ROW).setGap(2).setPadding(2).build())
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
        buildParticleOptionButton(world, "Random", ParticleExample::setEmitterToRandom),
        buildParticleOptionButton(world, "Mixed", ParticleExample::setEmitterToMixed),
        buildParticleOptionButton(world, "Burst", ParticleExample::setEmitterToBurst),
        buildParticleOptionButton(world, "Fire", ParticleExample::setEmitterToFire),
        buildParticleOptionButton(world, "Sparks", ParticleExample::setEmitterToSparks)
      );

    DefaultThemeBuilder.buildDarkTheme().applyToTree(section);

    return section;
  }

  private ButtonGuiElement buildParticleOptionButton(World world, String label, Consumer<Entity> configure) {
    return new ButtonGuiElement()
      .addStyle(new BaseStyles.Builder<>().setPadding(4).build())
      .setOnAction(() -> configure.accept(world.findEntityByName("emitter")))
      .appendChildren(new TextGuiElement().setText(label));
  }

  private static void setEmitterToDefault(Entity entity) {
    var defaultEmitter = new ParticleEmitterComponent();

    entity.addComponent(defaultEmitter)
      .addComponent(new CircleRendererComponent(Color.YELLOW, false))
      .addComponent(new BlendModeComponent(BlendMode.NORMAL))
      .addComponent(new TransformComponent(250, 300, 100));
  }

  private static void setEmitterToShell(Entity entity) {
    var shellEmitter = new ParticleEmitterComponent();

    shellEmitter.emissionConfig().shape().setEmitFromShell(true);

    entity.addComponent(shellEmitter)
      .addComponent(new CircleRendererComponent(Color.YELLOW, false))
      .addComponent(new BlendModeComponent(BlendMode.NORMAL))
      .addComponent(new TransformComponent(250, 300, 100));
  }

  private static void setEmitterToRandom(Entity entity) {
    var shellEmitter = new ParticleEmitterComponent();

    shellEmitter.emissionConfig().shape().setEmitFromShell(true).setRandomDirection(true);

    entity.addComponent(shellEmitter)
      .addComponent(new CircleRendererComponent(Color.YELLOW, false))
      .addComponent(new BlendModeComponent(BlendMode.NORMAL))
      .addComponent(new TransformComponent(250, 300, 100));
  }

  private static void setEmitterToMixed(Entity entity) {
    var mixedEmitter = new ParticleEmitterComponent()
      .emissionConfig().setLifeBounds(2, 4).done()
      .appearanceConfig().setPercentCircle(0.5f).setSizeBounds(5, 15).done();

    entity.addComponent(mixedEmitter)
      .addComponent(new BlendModeComponent(BlendMode.NORMAL))
      .addComponent(new TransformComponent(250, 300, 100));
  }

  private static void setEmitterToBurst(Entity entity) {
    var burstEmitter = new ParticleEmitterComponent()
      .emissionConfig().setCycles(4).setInterval(2f).setCountPerEmit(35).done();

    entity.addComponent(burstEmitter)
      .addComponent(new BlendModeComponent(BlendMode.NORMAL))
      .addComponent(new TransformComponent(250, 300, 100));
  }

  private static void setEmitterToFire(Entity entity) {
    var fireEmitter = new ParticleEmitterComponent()
      .appearanceConfig().setColor(new Color(230, 40, 45)).setSizeBounds(6, 10).done()
      .movementConfig()
      .setSpeed(18)
      // todo make it look like fire again (cone/triangle emitter)
//      .setVelocityBounds(new Vector2D(-18f, -70f), new Vector2D(18f, 0))
      .done()
      .emissionConfig().setCountPerEmit(10).setLifeBounds(1, 1).setInterval(0.1f).done();

    entity.addComponent(fireEmitter)
      .addComponent(new BlendModeComponent(BlendMode.LINEAR_DODGE))
      .addComponent(new TransformComponent(250, 300, 100));
  }

  private static void setEmitterToSparks(Entity entity) {
    var sparksEmitter = new ParticleEmitterComponent()
      .appearanceConfig().setColor(new Color(210, 80, 45)).setSizeBounds(6, 12).done()
      .movementConfig()
      .setSpeed(18)
      // todo make it look like sparks again (cone/triangle emitter)
//      .setVelocityBounds(new Vector2D(-18f, -70f), new Vector2D(18f, 0))
      .done()
      .emissionConfig().setCountPerEmit(1).setLifeBounds(1, 3).setInterval(0.1f).done();

    entity.addComponent(sparksEmitter)
      .addComponent(new BlendModeComponent(BlendMode.DISSOLVE))
      .addComponent(new TransformComponent(250, 300, 100));
  }
}
