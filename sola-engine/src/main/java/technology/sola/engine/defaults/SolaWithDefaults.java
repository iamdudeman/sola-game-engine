package technology.sola.engine.defaults;

import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.graphics.modules.CircleGraphicsModule;
import technology.sola.engine.defaults.graphics.modules.DebugGraphicsModule;
import technology.sola.engine.defaults.graphics.modules.RectangleGraphicsModule;
import technology.sola.engine.defaults.graphics.modules.SpriteGraphicsModule;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.system.CollisionDetectionSystem;

import java.util.function.Consumer;

/**
 * SolaWithDefaults extends {@link Sola} with some default behaviors that many games will find useful, such as default
 * physics configurations and rendering options. These can be enabled in the
 * {@link SolaWithDefaults#onInit(DefaultsConfigurator)} method and configured further via the member instances
 * <ul>
 *   <li>{@link SolaWithDefaults#solaPhysics}</li>
 *   <li>{@link SolaWithDefaults#solaGraphics}</li>
 *   <li>{@link SolaWithDefaults#solaGuiDocument}</li>
 * </ul>
 */
public abstract class SolaWithDefaults extends Sola {
  /**
   * The {@link SolaGraphics} instance for this Sola.
   */
  protected SolaGraphics solaGraphics;
  /**
   * The {@link SolaPhysics} instance for this Sola.
   */
  protected SolaPhysics solaPhysics;
  /**
   * The {@link SolaGuiDocument} instance for this Sola.
   */
  protected SolaGuiDocument solaGuiDocument;
  private Consumer<Renderer> renderFunction = renderer -> {
  };
  private Color backgroundColor = Color.BLACK;

  /**
   * Creates a SolaWithDefaults instance with desired {@link SolaConfiguration} from a {@link SolaConfiguration.Builder}.
   *
   * @param solaConfigurationBuilder the configuration builder
   */
  protected SolaWithDefaults(SolaConfiguration.Builder solaConfigurationBuilder) {
    super(solaConfigurationBuilder);
  }

  /**
   * Creates a SolaWithDefaults instance with desired {@link SolaConfiguration}.
   *
   * @param configuration the configuration for the Sola
   */
  protected SolaWithDefaults(SolaConfiguration configuration) {
    super(configuration);
  }

  /**
   * Calls {@link Sola#onInit()} while providing access to the {@link DefaultsConfigurator}.
   *
   * @param defaultsConfigurator the {@code DefaultsConfigurator} instance
   */
  protected abstract void onInit(DefaultsConfigurator defaultsConfigurator);

  @Override
  protected void onInit() {
    onInit(new DefaultsConfigurator());
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderFunction.accept(renderer);
  }

  /**
   * DefaultsConfigurator provides methods for enabling various default functionality.
   */
  public class DefaultsConfigurator {
    private boolean isDebug = false;

    /**
     * Enables debug rendering. This includes collider outlines and spacial hashmap boundaries.
     *
     * @return this
     */
    public DefaultsConfigurator useDebug() {
      if (!isDebug) {
        this.isDebug = true;

        if (solaGraphics != null && solaPhysics != null) {
          solaGraphics.getGraphicsModuleList().add(new DebugGraphicsModule(solaEcs.getSystem(CollisionDetectionSystem.class)));
        }
      }

      return this;
    }

    /**
     * Updates the background color (what everything clears to each frame). Default is {@link Color#BLACK}.
     *
     * @param backgroundColor the new color the renderer clears to
     * @return this
     */
    public DefaultsConfigurator useBackgroundColor(Color backgroundColor) {
      SolaWithDefaults.this.backgroundColor = backgroundColor;
      rebuildRenderFunction();

      return this;
    }

    /**
     * Initializes the {@link SolaWithDefaults#solaPhysics} instance. This adds several
     * {@link technology.sola.ecs.EcsSystem}s to the {@link Sola#solaEcs} instance.
     * <p>
     * EcsSystems added
     * <ul>
     *   <li>{@link technology.sola.engine.physics.system.GravitySystem}</li>
     *   <li>{@link technology.sola.engine.physics.system.PhysicsSystem}</li>
     *   <li>{@link technology.sola.engine.physics.system.CollisionDetectionSystem}</li>
     *   <li>{@link technology.sola.engine.physics.system.ImpulseCollisionResolutionSystem}</li>
     *   <li>{@link technology.sola.engine.physics.system.ParticleSystem}</li>
     * </ul>
     *
     * @return this
     */
    public DefaultsConfigurator usePhysics() {
      if (solaPhysics == null) {
        solaPhysics = new SolaPhysics(eventHub);

        solaEcs.addSystems(solaPhysics.getSystems());

        if (isDebug && solaGraphics != null) {
          solaGraphics.getGraphicsModuleList().add(new DebugGraphicsModule(solaEcs.getSystem(CollisionDetectionSystem.class)));
        }
      }

      return this;
    }

    /**
     * Initializes the {@link SolaWithDefaults#solaGraphics} instance. This adds several
     * {@link technology.sola.engine.defaults.graphics.modules.SolaGraphicsModule}s to it and adds several
     * {@link technology.sola.ecs.EcsSystem}s to the {@link Sola#solaEcs} instance.
     * <p>
     * Modules added
     * <ul>
     *   <li>{@link CircleGraphicsModule}</li>
     *   <li>{@link RectangleGraphicsModule}</li>
     *   <li>{@link SpriteGraphicsModule}</li>
     * </ul>
     * <p>
     * EcsSystems added
     * <ul>
     *   <li>{@link technology.sola.engine.graphics.system.SpriteAnimatorSystem}</li>
     *   <li>{@link technology.sola.engine.graphics.system.TransformAnimatorSystem}</li>
     * </ul>
     *
     * @return this
     */
    public DefaultsConfigurator useGraphics() {
      if (solaGraphics == null) {
        solaGraphics = new SolaGraphics(solaEcs);

        solaEcs.addSystems(solaGraphics.getSystems());

        AssetLoader<SpriteSheet> spriteSheetAssetLoader = assetLoaderProvider.get(SpriteSheet.class);

        solaGraphics.getGraphicsModuleList().add(new CircleGraphicsModule());
        solaGraphics.getGraphicsModuleList().add(new RectangleGraphicsModule());
        solaGraphics.getGraphicsModuleList().add(new SpriteGraphicsModule(spriteSheetAssetLoader));

        if (isDebug && solaPhysics != null) {
          solaGraphics.getGraphicsModuleList().add(new DebugGraphicsModule(solaEcs.getSystem(CollisionDetectionSystem.class)));
        }

        rebuildRenderFunction();
      }

      return this;
    }

    /**
     * Initializes the {@link SolaGuiDocument} instance.
     *
     * @return this
     */
    public DefaultsConfigurator useGui() {
      if (solaGuiDocument == null) {
        solaGuiDocument = new SolaGuiDocument(platform, assetLoaderProvider, eventHub);
        rebuildRenderFunction();
      }

      return this;
    }

    private void rebuildRenderFunction() {
      if (solaGraphics != null && solaGuiDocument != null) {
        renderFunction = renderer -> {
          renderer.clear(backgroundColor);
          solaGraphics.render(renderer);
          solaGuiDocument.render(renderer);
        };
      } else if (solaGraphics != null) {
        renderFunction = renderer -> {
          renderer.clear(backgroundColor);
          solaGraphics.render(renderer);
        };
      } else if (solaGuiDocument != null) {
        renderFunction = renderer -> {
          renderer.clear(backgroundColor);
          solaGuiDocument.render(renderer);
        };
      }
    }
  }
}
