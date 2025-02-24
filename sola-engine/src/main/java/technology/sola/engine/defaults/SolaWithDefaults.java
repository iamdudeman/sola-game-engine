package technology.sola.engine.defaults;

import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocumentAssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.graphics.modules.*;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiDocument;
import technology.sola.engine.graphics.gui.json.GuiJsonDocumentBuilder;
import technology.sola.engine.graphics.gui.json.element.*;
import technology.sola.engine.graphics.gui.style.theme.DefaultThemeBuilder;
import technology.sola.engine.graphics.gui.style.theme.GuiTheme;
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
 *   <li>{@link SolaWithDefaults#guiDocument}</li>
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
   * The {@link GuiDocument} instance for this Sola. This is null until {@link DefaultsConfigurator#useGui()} is
   * called.
   */
  protected GuiDocument guiDocument;
  /**
   * The {@link SolaControls} instance for this Sola. It can be used in place of other input related objects for
   * convenience.
   */
  protected final SolaControls solaControls;
  private Consumer<Renderer> renderFunction = renderer -> {
  };
  private Color backgroundColor = Color.BLACK;

  /**
   * Creates a SolaWithDefaults instance with desired {@link SolaConfiguration}.
   *
   * @param configuration the configuration for the Sola
   */
  protected SolaWithDefaults(SolaConfiguration configuration) {
    super(configuration);
    this.solaControls = new SolaControls(keyboardInput, mouseInput);
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
     * Enables debug rendering. This includes collider outlines and broad phase debug information.
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
          new TriangleEntityGraphicsModule(),
          new SpriteEntityGraphicsModule(spriteSheetAssetLoader),
          new ParticleEmitterEntityGraphicsModule()
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
     * Initializes the {@link GuiDocument} instance with a light {@link GuiTheme}. The theme will only be applied to
     * {@link technology.sola.engine.graphics.gui.GuiElement}s that are loaded via JSON files.
     *
     * @return this
     */
    public DefaultsConfigurator useGui() {
      return useGui(DefaultThemeBuilder.buildLightTheme());
    }

    /**
     * Initializes the {@link GuiDocument} instance with a desired {@link GuiTheme}.The theme will only be applied to
     * {@link technology.sola.engine.graphics.gui.GuiElement}s that are loaded via JSON files.
     *
     * @param guiTheme the theme to use
     * @return this
     */
    public DefaultsConfigurator useGui(GuiTheme guiTheme) {
      return useGui(guiTheme, List.of());
    }

    /**
     * Initializes the {@link GuiDocument} instance with a desired {@link GuiTheme}. Also registers additional
     * {@link GuiElementJsonBlueprint}s for parsing different kinds of elements as JSON. The theme will only be applied
     * to {@link technology.sola.engine.graphics.gui.GuiElement}s that are loaded via JSON files.
     *
     * @param guiTheme                           the theme to use
     * @param additionalGuiElementJsonBlueprints addition gui element json blueprints to register for parsing
     * @return this
     */
    public DefaultsConfigurator useGui(GuiTheme guiTheme, List<GuiElementJsonBlueprint<?, ?, ?>> additionalGuiElementJsonBlueprints) {
      if (guiDocument == null) {
        guiDocument = new GuiDocument(platform, assetLoaderProvider, mouseInput);
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
            new SpriteElementJsonBlueprint(),
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
      if (solaGraphics != null && guiDocument != null) {
        renderFunction = renderer -> {
          renderer.clear(backgroundColor);
          solaGraphics.render(renderer);

          var layers = renderer.getLayers();

          if (layers.isEmpty()) {
            guiDocument.render(renderer);
          } else {
            layers.get(layers.size() - 1).add(guiDocument::render, ScreenSpaceLightMapGraphicsModule.ORDER + 1);
          }
        };
      } else if (solaGraphics != null) {
        renderFunction = renderer -> {
          renderer.clear(backgroundColor);
          solaGraphics.render(renderer);
        };
      } else if (guiDocument != null) {
        renderFunction = renderer -> {
          renderer.clear(backgroundColor);
          guiDocument.render(renderer);
        };
      }
    }
  }
}
