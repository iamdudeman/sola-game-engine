package technology.sola.engine.graphics;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.SolaEcs;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.CameraComponent;
import technology.sola.engine.graphics.modules.SolaGraphicsModule;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.system.SpriteAnimatorSystem;
import technology.sola.engine.graphics.system.TransformAnimatorSystem;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SolaGraphics {
  private static final TransformComponent DEFAULT_CAMERA_TRANSFORM = new TransformComponent();
  private SolaEcs solaEcs;
  private List<SolaGraphicsModule> graphicsModuleList = new ArrayList<>();
  private Matrix3D cachedScreenToWorldMatrix = null;
  private float previousCameraX = 0;
  private float previousCameraY = 0;
  private float previousCameraScaleX = 1;
  private float previousCameraScaleY = 1;
  private final SpriteAnimatorSystem spriteAnimatorSystem;
  private final TransformAnimatorSystem transformAnimatorSystem;

  public SolaGraphics(SolaEcs solaEcs) {
    this.solaEcs = solaEcs;
    spriteAnimatorSystem = new SpriteAnimatorSystem();
    transformAnimatorSystem = new TransformAnimatorSystem();
  }

  public List<SolaGraphicsModule> getGraphicsModuleList() {
    return graphicsModuleList;
  }

  public void addGraphicsModules(SolaGraphicsModule... graphicsModules) {
    if (graphicsModules != null) {
      graphicsModuleList.addAll(Arrays.asList(graphicsModules));
    }
  }

  public EcsSystem[] getSystems() {
    return new EcsSystem[] {
      spriteAnimatorSystem,
      transformAnimatorSystem
    };
  }

  public void render(Renderer renderer) {
    // todo renderer clear?

    TransformComponent cameraTransform = getCameraTransform();
    Matrix3D cameraScaleTransform = Matrix3D.scale(cameraTransform.getScaleX(), cameraTransform.getScaleY());
    Matrix3D cameraTranslationTransform = Matrix3D.translate(-cameraTransform.getX(), -cameraTransform.getY())
      .multiply(cameraScaleTransform);
    World world = solaEcs.getWorld();

    for (var graphicsModule : graphicsModuleList) {
      graphicsModule.render(renderer, world, cameraScaleTransform, cameraTranslationTransform);
    }

    // todo render gui document?
  }

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

  private TransformComponent getCameraTransform() {
    var cameraViews = solaEcs.getWorld()
      .createView().of(TransformComponent.class, CameraComponent.class)
      .stream()
      .sorted(Comparator.comparingInt(view -> view.c2().getPriority()))
      .toList();

    return cameraViews.isEmpty()
      ? DEFAULT_CAMERA_TRANSFORM
      : cameraViews.get(0).c1();
  }
}
