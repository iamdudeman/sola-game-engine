package technology.sola.engine.defaults;

import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocumentAssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.graphics.modules.CircleEntityGraphicsModule;
import technology.sola.engine.defaults.graphics.modules.DebugEntityGraphicsModule;
import technology.sola.engine.defaults.graphics.modules.RectangleEntityGraphicsModule;
import technology.sola.engine.defaults.graphics.modules.ScreenSpaceLightMapGraphicsModule;
import technology.sola.engine.defaults.graphics.modules.SolaEntityGraphicsModule;
import technology.sola.engine.defaults.graphics.modules.SpriteEntityGraphicsModule;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.gui.properties.GuiPropertyDefaults;
import technology.sola.engine.graphics.guiv2.GuiDocument;
import technology.sola.engine.graphics.guiv2.json.GuiJsonDocumentBuilder;
import technology.sola.engine.graphics.guiv2.json.element.GuiElementJsonBlueprint;
import technology.sola.engine.graphics.guiv2.json.element.ImageElementJsonBlueprint;
import technology.sola.engine.graphics.guiv2.json.element.SectionElementJsonBlueprint;
import technology.sola.engine.graphics.guiv2.json.element.TextElementJsonBlueprint;
import technology.sola.engine.graphics.guiv2.json.element.input.ButtonElementJsonBlueprint;
import technology.sola.engine.graphics.guiv2.json.element.input.TextInputElementJsonBlueprint;
import technology.sola.engine.graphics.guiv2.style.theme.GuiTheme;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.system.LightFlickerSystem;
import technology.sola.engine.physics.system.CollisionDetectionSystem;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

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
   * The {@link SolaGraphics} instance for this Sola. This is null until {@link DefaultsConfigurator#useGraphics()} is
   * called.
   */
  protected SolaGraphics solaGraphics;
  /**
   * The {@link SolaPhysics} instance for this Sola. This is null until {@link DefaultsConfigurator#usePhysics()} is
   * called.
   */
  protected SolaPhysics solaPhysics;
  /**
   * The {@link SolaGuiDocument} instance for this Sola. This is null until {@link DefaultsConfigurator#useGui()} is
   * called.
   */
  protected SolaGuiDocument solaGuiDocument;
  protected GuiDocument guiDocument;
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
    private Color ambientColor = null;
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
          solaGraphics.addGraphicsModules(new DebugEntityGraphicsModule(solaEcs.getSystem(CollisionDetectionSystem.class)));
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
          solaGraphics.addGraphicsModules(new DebugEntityGraphicsModule(solaEcs.getSystem(CollisionDetectionSystem.class)));
        }
      }

      return this;
    }

    /**
     * Initializes the {@link SolaWithDefaults#solaGraphics} instance. This adds several
     * {@link SolaEntityGraphicsModule}s to it and adds several
     * {@link technology.sola.ecs.EcsSystem}s to the {@link Sola#solaEcs} instance.
     * <p>
     * Modules added
     * <ul>
     *   <li>{@link CircleEntityGraphicsModule}</li>
     *   <li>{@link RectangleEntityGraphicsModule}</li>
     *   <li>{@link SpriteEntityGraphicsModule}</li>
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

        solaGraphics.addGraphicsModules(
          new CircleEntityGraphicsModule(),
          new RectangleEntityGraphicsModule(),
          new SpriteEntityGraphicsModule(spriteSheetAssetLoader)
        );

        if (ambientColor != null) {
          solaGraphics.addGraphicsModules(new ScreenSpaceLightMapGraphicsModule(ambientColor));
          solaEcs.addSystem(new LightFlickerSystem());
        }

        if (isDebug && solaPhysics != null) {
          solaGraphics.addGraphicsModules(new DebugEntityGraphicsModule(solaEcs.getSystem(CollisionDetectionSystem.class)));
        }

        rebuildRenderFunction();
      }

      return this;
    }

    /**
     * Adds the {@link ScreenSpaceLightMapGraphicsModule}
     * {@link technology.sola.engine.defaults.graphics.modules.SolaGraphicsModule} with ambient color set to black.
     *
     * @return this
     */
    public DefaultsConfigurator useLighting() {
      return useLighting(Color.BLACK);
    }

    /**
     * Adds the {@link ScreenSpaceLightMapGraphicsModule}
     * {@link technology.sola.engine.defaults.graphics.modules.SolaGraphicsModule} with ambient color set.
     *
     * @param ambientColor the ambient color to use
     * @return this
     */
    public DefaultsConfigurator useLighting(Color ambientColor) {
      if (solaGraphics == null) {
        this.ambientColor = ambientColor;
      } else {
        solaGraphics.addGraphicsModules(new ScreenSpaceLightMapGraphicsModule(ambientColor));
        solaEcs.addSystem(new LightFlickerSystem());
      }

      return this;
    }

    /**
     * Initializes the {@link SolaGuiDocument} instance.
     *
     * @param propertyDefaults the {@link GuiPropertyDefaults} to use
     * @return this
     */
    public DefaultsConfigurator useGui(GuiPropertyDefaults propertyDefaults) {
      if (solaGuiDocument == null) {
        solaGuiDocument = new SolaGuiDocument(assetLoaderProvider, eventHub, propertyDefaults);
        solaGuiDocument.registerEventListeners(platform);
        rebuildRenderFunction();

        // Prepare default font
        AssetLoader<Font> fontAssetLoader = assetLoaderProvider.get(Font.class);

        if (!fontAssetLoader.hasAssetMapping(DefaultFont.ASSET_ID)) {
          fontAssetLoader.addAsset(DefaultFont.ASSET_ID, DefaultFont.get());
        }
      }

      return this;
    }

    /**
     * Initializes the {@link SolaGuiDocument} instance with light themed {@link GuiPropertyDefaults}.
     *
     * @return this
     */
    public DefaultsConfigurator useGui() {
      return useGui(new GuiPropertyDefaults());
    }

    // todo javadoc
    public DefaultsConfigurator useGuiV2() {
      return useGuiV2(GuiTheme.getDefaultLightTheme());
    }

    // todo javadoc
    public DefaultsConfigurator useGuiV2(GuiTheme guiTheme) {
      return useGuiV2(guiTheme, List.of());
    }

    // todo javadoc
    public DefaultsConfigurator useGuiV2(GuiTheme guiTheme, List<GuiElementJsonBlueprint<?, ?, ?>> additionalGuiElementJsonBlueprints) {
      if (guiDocument == null) {
        guiDocument = new GuiDocument(platform, assetLoaderProvider);
        rebuildRenderFunction();

        // Prepare default font
        AssetLoader<Font> fontAssetLoader = assetLoaderProvider.get(Font.class);

        if (!fontAssetLoader.hasAssetMapping(DefaultFont.ASSET_ID)) {
          fontAssetLoader.addAsset(DefaultFont.ASSET_ID, DefaultFont.get());
        }

        List<GuiElementJsonBlueprint<?, ?, ?>> guiElementJsonBlueprints = Stream.concat(
          Stream.of(
            new SectionElementJsonBlueprint(),
            new TextElementJsonBlueprint(),
            new ImageElementJsonBlueprint(),
            new ButtonElementJsonBlueprint(),
            new TextInputElementJsonBlueprint()
          ),
          additionalGuiElementJsonBlueprints.stream()
        ).toList();

        assetLoaderProvider.add(new GuiJsonDocumentAssetLoader(
          assetLoaderProvider.get(JsonElementAsset.class),
          new GuiJsonDocumentBuilder(guiTheme, guiElementJsonBlueprints)
        ));
      }

      return this;
    }

    private void rebuildRenderFunction() {
      if (solaGraphics != null && (solaGuiDocument != null || guiDocument != null)) {
        renderFunction = renderer -> {
          renderer.clear(backgroundColor);
          solaGraphics.render(renderer);

          var layers = renderer.getLayers();

          if (layers.isEmpty()) {
            if (solaGuiDocument == null) {
              guiDocument.render(renderer);
            } else {
              solaGuiDocument.render(renderer);
            }
          } else {
            layers.get(layers.size() - 1).add(solaGuiDocument::render, ScreenSpaceLightMapGraphicsModule.ORDER + 1);
          }
        };
      } else if (solaGraphics != null) {
        renderFunction = renderer -> {
          renderer.clear(backgroundColor);
          solaGraphics.render(renderer);
        };
      } else if (solaGuiDocument != null || guiDocument != null) {
        renderFunction = renderer -> {
          renderer.clear(backgroundColor);
          if (solaGuiDocument == null) {
            guiDocument.render(renderer);
          } else {
            solaGuiDocument.render(renderer);
          }
        };
      }
    }
  }
}
