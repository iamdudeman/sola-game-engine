package technology.sola.engine.core.graphics;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.EcsSystemContainer;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.components.CameraComponent;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.graphics.sprite.SpriteAnimatorSystem;
import technology.sola.engine.graphics.sprite.SpriteSheet;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

public class SolaGraphics {
  private static final TransformComponent DEFAULT_CAMERA_TRANSFORM = new TransformComponent();
  private final EcsSystemContainer ecsSystemContainer;
  private final Renderer renderer;
  private final AssetPool<SpriteSheet> spriteSheetAssetPool;
  private final AssetPool<Font> fontAssetPool;
  private boolean isRenderDebug = false;
  private final SpriteAnimatorSystem spriteAnimatorSystem;

  public static SolaGraphics use(EcsSystemContainer ecsSystemContainer, Renderer renderer, AssetPoolProvider assetPoolProvider) {
    SolaGraphics solaGraphics = new SolaGraphics(
      ecsSystemContainer, renderer,
      assetPoolProvider.getAssetPool(SpriteSheet.class), assetPoolProvider.getAssetPool(Font.class)
    );

    ecsSystemContainer.add(solaGraphics.spriteAnimatorSystem);

    return solaGraphics;
  }

  // TODO improve performance of this method
  public Vector2D screenToWorldCoordinate(Vector2D screenCoordinate) {
    var cameraEntities = ecsSystemContainer.getWorld().getEntitiesWithComponents(TransformComponent.class, CameraComponent.class);
    var cameraTransform = cameraEntities.isEmpty()
      ? DEFAULT_CAMERA_TRANSFORM
      : cameraEntities.get(0).getComponent(TransformComponent.class);
    var transform = Matrix3D.translate(-cameraTransform.getX(), -cameraTransform.getY())
      .multiply(Matrix3D.scale(cameraTransform.getScaleX(), cameraTransform.getScaleY()))
      .invert(); // TODO this invert is costly so clean this up later

    return transform.forward(screenCoordinate.x, screenCoordinate.y);
  }

  public void render() {
    var cameraEntities = ecsSystemContainer.getWorld().getEntitiesWithComponents(TransformComponent.class, CameraComponent.class);

    TransformComponent cameraTransform = cameraEntities.isEmpty()
      ? DEFAULT_CAMERA_TRANSFORM
      : cameraEntities.get(0).getComponent(TransformComponent.class);

    GeometryGraphics.render(renderer, ecsSystemContainer, cameraTransform);

    SpriteGraphics.render(renderer, ecsSystemContainer, cameraTransform, spriteSheetAssetPool);

    GuiGraphics.render(renderer, ecsSystemContainer, fontAssetPool);

    // TODO need to render this to back most layer at some point
    if (isRenderDebug) {
      DebugGraphics.render(renderer, ecsSystemContainer, cameraTransform);
    }
  }

  public boolean isRenderDebug() {
    return isRenderDebug;
  }

  public void setRenderDebug(boolean renderDebug) {
    isRenderDebug = renderDebug;
  }

  private SolaGraphics(EcsSystemContainer ecsSystemContainer, Renderer renderer,
                       AssetPool<SpriteSheet> spriteSheetAssetPool, AssetPool<Font> fontAssetPool) {
    this.ecsSystemContainer = ecsSystemContainer;
    this.renderer = renderer;
    this.spriteSheetAssetPool = spriteSheetAssetPool;
    this.fontAssetPool = fontAssetPool;

    spriteAnimatorSystem = new SpriteAnimatorSystem();
  }
}
