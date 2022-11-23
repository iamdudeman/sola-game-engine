package technology.sola.engine.core.module.graphics;

import technology.sola.ecs.SolaEcs;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.core.module.SolaModule;
import technology.sola.engine.graphics.components.CameraComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.system.SpriteAnimatorSystem;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

import java.util.Comparator;

/**
 * SolaGraphics is a {@link SolaModule} that adds needed {@link technology.sola.ecs.EcsSystem}s for animating and handles
 * querying {@link SolaEcs} for {@link technology.sola.ecs.Entity} instances that should be rendered. It will handle
 * offsetting rendering if an {@code Entity} with a {@link CameraComponent} is present.
 */
@SolaModule
public class SolaGraphics {
  private static final TransformComponent DEFAULT_CAMERA_TRANSFORM = new TransformComponent();
  private final SolaEcs solaEcs;
  private final Renderer renderer;
  private final AssetLoader<SpriteSheet> spriteSheetAssetLoader;
  private boolean isRenderDebug = false;
  private final SpriteAnimatorSystem spriteAnimatorSystem;
  private Matrix3D cachedScreenToWorldMatrix = null;
  private float previousCameraX = 0;
  private float previousCameraY = 0;
  private float previousCameraScaleX = 1;
  private float previousCameraScaleY = 1;

  /**
   * Creates an instance of {@link SolaGraphics} and adds {@link technology.sola.ecs.EcsSystem}s for animation.
   *
   * @param solaEcs             {@link SolaEcs} instance
   * @param renderer            {@link Renderer} instance
   * @param assetLoaderProvider {@link AssetLoaderProvider} instance
   * @return a new {@code SolaGraphics} instance
   */
  public static SolaGraphics useModule(SolaEcs solaEcs, Renderer renderer, AssetLoaderProvider assetLoaderProvider) {
    SolaGraphics solaGraphics = new SolaGraphics(solaEcs, renderer, assetLoaderProvider.get(SpriteSheet.class));

    solaEcs.addSystem(solaGraphics.spriteAnimatorSystem);

    return solaGraphics;
  }

  /**
   * Renders all {@link technology.sola.ecs.Entity} that have various render components. Also renders debug physics
   * graphics if enabled and physics {@link technology.sola.ecs.EcsSystem}s are present.
   */
  public void render() {
    TransformComponent cameraTransform = getCameraTransform();

    GeometryGraphics.render(renderer, solaEcs, cameraTransform);

    SpriteGraphics.render(renderer, solaEcs, cameraTransform, spriteSheetAssetLoader);

    if (isRenderDebug) {
      if (renderer.getLayers().isEmpty()) {
        DebugGraphics.render(renderer, solaEcs, cameraTransform);
      } else {
        var lastLayer = renderer.getLayers().get(renderer.getLayers().size() - 1);

        lastLayer.add(r -> DebugGraphics.render(renderer, solaEcs, cameraTransform), Integer.MAX_VALUE);
      }
    }
  }

  /**
   * Calculates the corresponding world coordinate based on a screen coordinate. This accounts for camera translate and
   * scaling.
   *
   * @param screenCoordinate the screen coordinate
   * @return the world coordinate
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

    return cachedScreenToWorldMatrix.forward(screenCoordinate.x(), screenCoordinate.y());
  }

  public TransformComponent getCameraTransform() {
    var cameraViews = solaEcs.getWorld()
      .createView().of(TransformComponent.class, CameraComponent.class)
      .stream()
      .sorted(Comparator.comparingInt(view -> view.c2().getPriority()))
      .toList();

    return cameraViews.isEmpty()
      ? DEFAULT_CAMERA_TRANSFORM
      : cameraViews.get(0).c1();
  }

  public boolean isRenderDebug() {
    return isRenderDebug;
  }

  public void setRenderDebug(boolean renderDebug) {
    isRenderDebug = renderDebug;
  }

  private SolaGraphics(SolaEcs solaEcs, Renderer renderer, AssetLoader<SpriteSheet> spriteSheetAssetLoader) {
    this.solaEcs = solaEcs;
    this.renderer = renderer;
    this.spriteSheetAssetLoader = spriteSheetAssetLoader;

    spriteAnimatorSystem = new SpriteAnimatorSystem();
  }
}
