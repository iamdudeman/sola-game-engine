package technology.sola.engine.core.graphics;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.ecs.SolaEcs;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.components.CameraComponent;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.graphics.sprite.SpriteAnimatorSystem;
import technology.sola.engine.graphics.sprite.SpriteSheet;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

import java.util.Comparator;
import java.util.stream.Collectors;

public class SolaGraphics {
  private static final TransformComponent DEFAULT_CAMERA_TRANSFORM = new TransformComponent();
  private final SolaEcs solaEcs;
  private final Renderer renderer;
  private final AssetPool<SpriteSheet> spriteSheetAssetPool;
  private final AssetPool<Font> fontAssetPool;
  private boolean isRenderDebug = false;
  private final SpriteAnimatorSystem spriteAnimatorSystem;

  public static SolaGraphics use(SolaEcs solaEcs, Renderer renderer, AssetPoolProvider assetPoolProvider) {
    SolaGraphics solaGraphics = new SolaGraphics(
            solaEcs, renderer,
      assetPoolProvider.getAssetPool(SpriteSheet.class), assetPoolProvider.getAssetPool(Font.class)
    );

    solaEcs.addSystem(solaGraphics.spriteAnimatorSystem);

    return solaGraphics;
  }

  // TODO improve performance of this method
  public Vector2D screenToWorldCoordinate(Vector2D screenCoordinate) {
    var cameraTransform = getCameraTransform();
    var transform = Matrix3D.translate(-cameraTransform.getX(), -cameraTransform.getY())
      .multiply(Matrix3D.scale(cameraTransform.getScaleX(), cameraTransform.getScaleY()))
      .invert(); // TODO this invert is costly so clean this up later if possible

    return transform.forward(screenCoordinate.x, screenCoordinate.y);
  }

  public void render() {
    TransformComponent cameraTransform = getCameraTransform();

    GeometryGraphics.render(renderer, solaEcs, cameraTransform);

    SpriteGraphics.render(renderer, solaEcs, cameraTransform, spriteSheetAssetPool);

    GuiGraphics.render(renderer, solaEcs, fontAssetPool);

    // TODO need to render this to back most layer at some point probably
    if (isRenderDebug) {
      DebugGraphics.render(renderer, solaEcs, cameraTransform);
    }
  }

  public TransformComponent getCameraTransform() {
    var cameraEntities = solaEcs.getWorld()
      .getEntitiesWithComponents(TransformComponent.class, CameraComponent.class)
      .stream()
      .sorted(Comparator.comparingInt(entity -> entity.getComponent(CameraComponent.class).getPriority()))
      .collect(Collectors.toList());

    return cameraEntities.isEmpty()
      ? DEFAULT_CAMERA_TRANSFORM
      : cameraEntities.get(0).getComponent(TransformComponent.class);
  }

  public boolean isRenderDebug() {
    return isRenderDebug;
  }

  public void setRenderDebug(boolean renderDebug) {
    isRenderDebug = renderDebug;
  }

  private SolaGraphics(SolaEcs solaEcs, Renderer renderer,
                       AssetPool<SpriteSheet> spriteSheetAssetPool, AssetPool<Font> fontAssetPool) {
    this.solaEcs = solaEcs;
    this.renderer = renderer;
    this.spriteSheetAssetPool = spriteSheetAssetPool;
    this.fontAssetPool = fontAssetPool;

    spriteAnimatorSystem = new SpriteAnimatorSystem();
  }
}
