package technology.sola.engine.graphics;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.SolaEcs;
import technology.sola.ecs.World;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocumentAssetLoader;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheet;
import technology.sola.engine.assets.json.JsonElementAsset;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.debug.DebugGraphicsModule;
import technology.sola.engine.debug.DebugControlSystem;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.components.CameraComponent;
import technology.sola.engine.graphics.gui.GuiDocument;
import technology.sola.engine.graphics.gui.GuiDocumentSystem;
import technology.sola.engine.graphics.gui.json.GuiJsonDocumentBuilder;
import technology.sola.engine.graphics.gui.json.element.*;
import technology.sola.engine.graphics.gui.style.theme.DefaultThemeBuilder;
import technology.sola.engine.graphics.gui.style.theme.GuiTheme;
import technology.sola.engine.graphics.modules.*;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.system.LightFlickerSystem;
import technology.sola.engine.graphics.system.SpriteAnimatorSystem;
import technology.sola.engine.graphics.system.TransformAnimatorSystem;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseInput;
import technology.sola.engine.physics.SolaPhysics;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * SolaGraphics provides default rendering capabilities while also allowing for adding new rendering
 * capabilities via {@link SolaGraphicsModule}s.
 */
@NullMarked
public class SolaGraphics {
  private static final TransformComponent DEFAULT_CAMERA_TRANSFORM = new TransformComponent();
  private final SolaEcs solaEcs;
  private final List<SolaGraphicsModule> graphicsModuleList = new ArrayList<>();
  private final SpriteAnimatorSystem spriteAnimatorSystem;
  private final TransformAnimatorSystem transformAnimatorSystem;
  @Nullable
  private Matrix3D cachedScreenToWorldMatrix = null;
  private float previousCameraX = 0;
  private float previousCameraY = 0;
  private float previousCameraScaleX = 1;
  private float previousCameraScaleY = 1;
  private Color backgroundColor = Color.BLACK;
  @Nullable
  private GuiDocument guiDocument;

  /**
   * Adds {@link SolaGraphicsModule}s to be rendered.
   *
   * @param graphicsModules the modules to add
   */
  public void addGraphicsModules(SolaGraphicsModule... graphicsModules) {
    graphicsModuleList.addAll(Arrays.asList(graphicsModules));
    graphicsModuleList.sort(SolaGraphicsModule::compareTo);
  }

  /**
   * Gets a {@link SolaGraphicsModule} that has been added or else null.
   *
   * @param solaGraphicsModuleClass the class of the SolaGraphicsModule
   * @param <T>                     the type of graphics module
   * @return the graphics module
   */
  @SuppressWarnings("unchecked")
  public <T extends SolaGraphicsModule> T getGraphicsModule(Class<T> solaGraphicsModuleClass) {
    for (var graphicsModule : graphicsModuleList) {
      if (graphicsModule.getClass() == solaGraphicsModuleClass) {
        return (T) graphicsModule;
      }
    }

    throw new IllegalStateException("No graphicsModule found for " + solaGraphicsModuleClass);
  }

  /**
   * Renders all {@link SolaGraphicsModule}s that have been added. Passes each the current camera's translation and
   * scale.
   *
   * @param renderer the {@link Renderer} instance
   */
  public void render(Renderer renderer) {
    // clear to background
    renderer.clear(backgroundColor);

    // render graphics modules
    TransformComponent cameraTransform = getCameraTransform();
    Matrix3D cameraScaleTransform = Matrix3D.scale(cameraTransform.getScaleX(), cameraTransform.getScaleY());
    Matrix3D cameraTranslationTransform = Matrix3D.translate(-cameraTransform.getX(), -cameraTransform.getY())
      .multiply(cameraScaleTransform);
    World world = solaEcs.getWorld();

    for (var graphicsModule : graphicsModuleList) {
      if (graphicsModule.isActive()) {
        graphicsModule.render(renderer, world, cameraScaleTransform, cameraTranslationTransform);
      }
    }
  }

  /**
   * Using the camera's transform, this method calculates the {@link World} coordinate based on the screen's coordinate
   * system.
   *
   * @param screenCoordinate the coordinate of the screen to map
   * @return the coordinate mapped to the {@code World}'s coordinate system
   */
  public Vector2D screenToWorldCoordinate(Vector2D screenCoordinate) {
    var cameraTransform = getCameraTransform();

    if (previousCameraX != cameraTransform.getX() || previousCameraY != cameraTransform.getY()
      || previousCameraScaleX != cameraTransform.getScaleX() || previousCameraScaleY != cameraTransform.getScaleY()
      || cachedScreenToWorldMatrix == null
    ) {
      previousCameraX = cameraTransform.getX();
      previousCameraY = cameraTransform.getY();
      previousCameraScaleX = cameraTransform.getScaleX();
      previousCameraScaleY = cameraTransform.getScaleY();
      cachedScreenToWorldMatrix = Matrix3D.translate(-previousCameraX, -previousCameraY)
        .multiply(Matrix3D.scale(previousCameraScaleX, previousCameraScaleY))
        .invert();
    }

    return cachedScreenToWorldMatrix.multiply(screenCoordinate);
  }

  /**
   * The {@link GuiDocument} instance for this Sola. Call {@link Builder#withGui(MouseInput)} to configure.
   *
   * @return {@link GuiDocument} instance
   */
  public GuiDocument guiDocument() {
    if (guiDocument == null) {
      throw new IllegalStateException("GuiDocument not initialized. Call Builder.withGui first.");
    }

    return guiDocument;
  }

  /**
   * @return the {@link SpriteAnimatorSystem}
   */
  public SpriteAnimatorSystem getSpriteAnimatorSystem() {
    return spriteAnimatorSystem;
  }

  /**
   * @return the {@link TransformAnimatorSystem}
   */
  public TransformAnimatorSystem getTransformAnimatorSystem() {
    return transformAnimatorSystem;
  }

  private SolaGraphics(SolaEcs solaEcs) {
    this.solaEcs = solaEcs;
    spriteAnimatorSystem = new SpriteAnimatorSystem();
    transformAnimatorSystem = new TransformAnimatorSystem();
  }

  private TransformComponent getCameraTransform() {
    var cameraViews = solaEcs.getWorld()
      .createView().of(TransformComponent.class, CameraComponent.class)
      .getEntries()
      .stream()
      .sorted(Comparator.comparingInt(view -> view.c2().getPriority()))
      .toList();

    return cameraViews.isEmpty()
      ? DEFAULT_CAMERA_TRANSFORM
      : cameraViews.get(0).c1();
  }

  /**
   * Builder for {@link SolaGraphics}.
   */
  public static class Builder {
    private final SolaEcs solaEcs;
    private final SolaPlatform platform;
    private Color backgroundColor = Color.BLACK;
    private boolean withDefaultGraphicsModules = true;
    @Nullable
    private Consumer<SolaGraphics> applyDebug;
    @Nullable
    private Color ambientLightColor;
    @Nullable
    private MouseInput mouseInput;
    @Nullable
    private GuiTheme guiTheme = null;
    private List<GuiElementJsonBlueprint<?, ?, ?>> additionalGuiElementJsonBlueprints = new ArrayList<>();

    /**
     * Creates a new {@link Builder} instance.
     *
     * @param platform the {@link SolaPlatform} instance
     * @param solaEcs  the {@link SolaEcs} instance to attach systems to
     */
    public Builder(SolaPlatform platform, SolaEcs solaEcs) {
      this.platform = platform;
      this.solaEcs = solaEcs;
    }

    /**
     * Sets the background {@link Color} cleared to each frame.
     *
     * @param backgroundColor the {@link Color} to clear to (defaults to {@link Color#BLACK}
     * @return this
     */
    public Builder withBackgroundColor(Color backgroundColor) {
      this.backgroundColor = backgroundColor;
      return this;
    }

    /**
     * Sets the builder to not add the default {@link SolaGraphicsModule}s when initializing the {@link SolaGraphics}
     * instance.
     *
     * <ul>
     *   <li>{@link CircleEntityGraphicsModule}</li>
     *   <li>{@link RectangleEntityGraphicsModule}</li>
     *   <li>{@link TriangleEntityGraphicsModule}</li>
     *   <li>{@link SpriteEntityGraphicsModule}</li>
     *   <li>{@link ParticleEmitterEntityGraphicsModule}</li>
     * </ul>
     *
     * @return this
     */
    public Builder withoutDefaultGraphicsModules() {
      this.withDefaultGraphicsModules = false;
      return this;
    }

    /**
     * Sets the builder to add lighting related graphics modules and systems. Sets the ambient light color
     * to {@link Color#BLACK}.
     *
     * @return this
     */
    public Builder withLighting() {
      return withLighting(Color.BLACK);
    }

    /**
     * Sets the builder to add lighting related graphics modules and systems. Sets the ambient light color
     * to the desired color.
     *
     * @param ambientLightColor the color to set the ambient light to
     * @return this
     */
    public Builder withLighting(Color ambientLightColor) {
      this.ambientLightColor = ambientLightColor;
      return this;
    }

    /**
     * Sets the builder to add a {@link GuiDocument} and related systems.
     * Uses {@link DefaultThemeBuilder#buildLightTheme()} for the theme.
     *
     * @param mouseInput the {@link MouseInput} instance to use for the {@link GuiDocument}
     * @return this
     */
    public Builder withGui(MouseInput mouseInput) {
      return withGui(mouseInput, DefaultThemeBuilder.buildLightTheme());
    }

    /**
     * Sets the builder to add a {@link GuiDocument} and related systems. Uses desired {@link GuiTheme}.
     *
     * @param mouseInput the {@link MouseInput} instance to use for the {@link GuiDocument}
     * @param guiTheme   the {@link GuiTheme} to use for the {@link GuiDocument}
     * @return this
     */
    public Builder withGui(MouseInput mouseInput, GuiTheme guiTheme) {
      return withGui(mouseInput, guiTheme, List.of());
    }

    /**
     * Sets the builder to add a {@link GuiDocument} and related systems. Uses desired {@link GuiTheme}. Also registers
     * additional {@link GuiElementJsonBlueprint}s.
     *
     * @param mouseInput                         the {@link MouseInput} instance to use for the {@link GuiDocument}
     * @param guiTheme                           the {@link GuiTheme} to use for the {@link GuiDocument}
     * @param additionalGuiElementJsonBlueprints additional {@link GuiElementJsonBlueprint}s to add to the {@link GuiDocument}
     * @return this
     */
    public Builder withGui(
      MouseInput mouseInput,
      GuiTheme guiTheme,
      List<GuiElementJsonBlueprint<?, ?, ?>> additionalGuiElementJsonBlueprints
    ) {
      this.mouseInput = mouseInput;
      this.guiTheme = guiTheme;
      this.additionalGuiElementJsonBlueprints = additionalGuiElementJsonBlueprints;
      return this;
    }

    /**
     * Enables debug rendering. If {@link SolaPhysics} is provided then collision debug rendering will also be present.
     *
     * @param solaPhysics   the optional {@link SolaPhysics} instance to attach to
     * @param eventHub      the {@link EventHub} to add listeners to
     * @param keyboardInput the {@link KeyboardInput} to control debug rendering state
     * @return this
     */
    public Builder withDebug(@Nullable SolaPhysics solaPhysics, EventHub eventHub, KeyboardInput keyboardInput) {
      applyDebug = (solaGraphics) -> {
        var debugGraphicsModule = new DebugGraphicsModule(
          solaPhysics == null ? null : solaPhysics.getCollisionDetectionSystem(),
          eventHub
        );

        solaGraphics.addGraphicsModules(debugGraphicsModule);
        solaEcs.addSystem(new DebugControlSystem(keyboardInput, debugGraphicsModule));
      };

      return this;
    }

    /**
     * Builds and initializes the {@link SolaGraphics} instance. This will add required {@link EcsSystem}s,
     * {@link AssetLoader}s, a {@link DefaultFont} and {@link SolaGraphicsModule}s.
     *
     * @param assetLoaderProvider the {@link AssetLoaderProvider}
     * @return the initialized {@link SolaGraphics} instance
     */
    public SolaGraphics buildAndInitialize(AssetLoaderProvider assetLoaderProvider) {
      SolaGraphics solaGraphics = new SolaGraphics(solaEcs);

      // set background color
      solaGraphics.backgroundColor = backgroundColor;

      // add graphics systems
      solaEcs.addSystems(
        solaGraphics.spriteAnimatorSystem,
        solaGraphics.transformAnimatorSystem
      );

      // add default graphics modules
      if (withDefaultGraphicsModules) {
        AssetLoader<SpriteSheet> spriteSheetAssetLoader = assetLoaderProvider.get(SpriteSheet.class);

        solaGraphics.addGraphicsModules(
          new CircleEntityGraphicsModule(),
          new RectangleEntityGraphicsModule(),
          new TriangleEntityGraphicsModule(),
          new SpriteEntityGraphicsModule(spriteSheetAssetLoader),
          new ParticleEmitterEntityGraphicsModule()
        );
      }

      // set up lighting stuff
      if (ambientLightColor != null) {
        solaGraphics.addGraphicsModules(new ScreenSpaceLightMapGraphicsModule(ambientLightColor));
        solaEcs.addSystem(new LightFlickerSystem());
      }

      // set up gui stuff
      if (guiTheme != null && mouseInput != null) {
        solaGraphics.guiDocument = new GuiDocument(platform, assetLoaderProvider, mouseInput);

        // Prepare default font
        AssetLoader<Font> fontAssetLoader = assetLoaderProvider.get(Font.class);

        if (!fontAssetLoader.hasAssetMapping(DefaultFont.ASSET_ID)) {
          fontAssetLoader.addAsset(DefaultFont.ASSET_ID, DefaultFont.get());
        }

        solaGraphics.addGraphicsModules(new GuiDocumentGraphicsModule(solaGraphics.guiDocument));

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

        solaEcs.addSystem(new GuiDocumentSystem(solaGraphics.guiDocument));
      }

      // apply debug stuff
      if (applyDebug != null) {
        applyDebug.accept(solaGraphics);
      }

      return solaGraphics;
    }
  }
}
