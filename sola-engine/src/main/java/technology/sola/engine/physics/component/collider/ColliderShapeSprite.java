package technology.sola.engine.physics.component.collider;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Entity;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheet;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.geometry.Shape;
import technology.sola.math.linear.Vector2D;

/**
 * ColliderShapeSprite is a collider shape that sizes the collider based on the dimensions of the sprite.
 *
 * @param spriteSheetLoader the loader used to load the sprite sheet
 */
@NullMarked
public record ColliderShapeSprite(
  AssetLoader<SpriteSheet> spriteSheetLoader
) implements ColliderShape<Shape> {
  @Override
  public Rectangle getBoundingBox(Entity entity, TransformComponent transformComponent, float offsetX, float offsetY) {
    var dimensions = getSpriteDimensions(entity);
    var width = dimensions.width;
    var height = dimensions.height;
    Vector2D min = transformComponent.getTranslate().add(new Vector2D(offsetX, offsetY));

    return new Rectangle(
      min,
      min.add(new Vector2D(
        width * transformComponent.getScaleX(),
        height * transformComponent.getScaleY()
      ))
    );
  }

  @Override
  public Shape getShape(Entity entity, TransformComponent transformComponent, float offsetX, float offsetY) {
    var dimensions = getSpriteDimensions(entity);
    var width = dimensions.width;
    var height = dimensions.height;
    Vector2D min = new Vector2D(
      transformComponent.getX() + offsetX,
      transformComponent.getY() + offsetY
    );
    Vector2D max = new Vector2D(
      min.x() + transformComponent.getScaleX() * width,
      min.y() + transformComponent.getScaleY() * height
    );

    return new Rectangle(min, max);
  }

  @Override
  public void debugRender(Renderer renderer, Entity entity, TransformComponent transformComponent, float offsetX, float offsetY) {
    Rectangle rectangle = (Rectangle) getShape(entity, transformComponent, offsetX, offsetY);

    renderer.drawRect(rectangle.min().x(), rectangle.min().y(), rectangle.getWidth(), rectangle.getHeight(), Color.RED);
  }

  private WidthHeight getSpriteDimensions(Entity entity) {
    var spriteComponent = entity.getComponent(SpriteComponent.class);

    if (spriteComponent == null) {
      throw new IllegalStateException("ColliderShapeSprite cannot be used if there is no SpriteComponent");
    }

    var assetHandle = spriteComponent.getSprite(spriteSheetLoader);

    if (assetHandle.isLoading()) {
      return WidthHeight.NULL;
    } else {
      var asset = assetHandle.getAsset();

      if (asset == null) {
        return WidthHeight.NULL;
      }

      return new WidthHeight(asset.getWidth(), asset.getHeight());
    }
  }

  private record WidthHeight(int width, int height) {
    private static final WidthHeight NULL = new WidthHeight(1, 1);
  }
}
