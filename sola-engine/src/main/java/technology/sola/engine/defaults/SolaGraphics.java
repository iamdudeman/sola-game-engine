package technology.sola.engine.defaults;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.SolaEcs;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.graphics.modules.SolaGraphicsModule;
import technology.sola.engine.graphics.components.CameraComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.system.SpriteAnimatorSystem;
import technology.sola.engine.graphics.system.TransformAnimatorSystem;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * SolaGraphics provides default rendering capabilities while also allowing for adding new rendering
 * capabilities via {@link SolaGraphicsModule}s.
 */
public class SolaGraphics {
  private static final TransformComponent DEFAULT_CAMERA_TRANSFORM = new TransformComponent();
  private final SolaEcs solaEcs;
  private final List<SolaGraphicsModule> graphicsModuleList = new ArrayList<>();
  private Matrix3D cachedScreenToWorldMatrix = null;
  private float previousCameraX = 0;
  private float previousCameraY = 0;
  private float previousCameraScaleX = 1;
  private float previousCameraScaleY = 1;
  private final SpriteAnimatorSystem spriteAnimatorSystem;
  private final TransformAnimatorSystem transformAnimatorSystem;

  /**
   * Creates a SolaGraphics instance.
   *
   * @param solaEcs the {@link SolaEcs} instance
   */
  public SolaGraphics(SolaEcs solaEcs) {
    this.solaEcs = solaEcs;
    spriteAnimatorSystem = new SpriteAnimatorSystem();
    transformAnimatorSystem = new TransformAnimatorSystem();
  }

  /**
   * Adds {@link SolaGraphicsModule}s to be rendered.
   *
   * @param graphicsModules the modules to add
   */
  public void addGraphicsModules(SolaGraphicsModule... graphicsModules) {
    if (graphicsModules != null) {
      graphicsModuleList.addAll(Arrays.asList(graphicsModules));
      graphicsModuleList.sort(SolaGraphicsModule::compareTo);
    }
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
   * @return an array of all graphics related {@link EcsSystem}s
   */
  public EcsSystem[] getSystems() {
    return new EcsSystem[]{
      spriteAnimatorSystem,
      transformAnimatorSystem,
    };
  }

  /**
   * Renders all {@link SolaGraphicsModule}s that have been added. Passes each the current camera's translation and
   * scale.
   *
   * @param renderer the {@link Renderer} instance
   */
  public void render(Renderer renderer) {
    TransformComponent cameraTransform = getCameraTransform();
    Matrix3D cameraScaleTransform = Matrix3D.scale(cameraTransform.getScaleX(), cameraTransform.getScaleY());
    Matrix3D cameraTranslationTransform = Matrix3D.translate(-cameraTransform.getX(), -cameraTransform.getY())
      .multiply(cameraScaleTransform);
    World world = solaEcs.getWorld();

    for (var graphicsModule : graphicsModuleList) {
      graphicsModule.render(renderer, world, cameraScaleTransform, cameraTranslationTransform);
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
}
