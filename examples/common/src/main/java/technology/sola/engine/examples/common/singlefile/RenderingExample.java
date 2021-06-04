package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.ecs.AbstractEcsSystem;
import technology.sola.engine.ecs.World;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.RenderMode;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.physics.component.PositionComponent;

public class RenderingExample extends AbstractSola {
  private SolaImage solaImage;

  public RenderingExample() {
    config(800, 600, 30, true);
  }

  @Override
  protected void onInit() {
    AssetPool<SolaImage> solaImageAssetPool = assetPoolProvider.getAssetPool(SolaImage.class);

    solaImage = solaImageAssetPool.addAndGetAsset("test_tiles", "assets/test_tiles.png");

//    assetLoader.addAsset("test_tiles", "test_tiles.png");

//    solaImage = assetLoader.getAsset("test_tiles", SolaImage.class);

    World world = new World(1);

    world.createEntity()
      .addComponent(new PositionComponent());

    ecsSystemContainer.setWorld(world);
    ecsSystemContainer.add(new TestSystem());
  }

  @Override
  protected void onRender() {
    renderer.clear();
    renderer.setPixel(5, 5, Color.WHITE);
    renderer.setPixel(6, 5, Color.BLUE);
    renderer.setPixel(6, 6, Color.RED);
    renderer.setPixel(6, 7, Color.GREEN);

    renderer.drawLine(20, 50, 20, 100, Color.WHITE);
    renderer.drawLine(50, 20, 100, 20, Color.WHITE);

    renderer.fillRect(100, 100, 60, 80, Color.GREEN);
    renderer.drawRect(100, 100, 60, 80, Color.RED);

    renderer.drawRect(300, 150, 5, 5, Color.GREEN);
    renderer.fillCircle(300, 150, 100.5f, Color.BLUE);
    renderer.drawCircle(300, 150, 100.5f, Color.RED);

    renderer.drawImage(400, 400, solaImage);
    renderer.drawImage(400, 530, solaImage.getSubImage(1, 1, 16, 16));

    renderer.fillRect(180, 530, 50, 50, new Color(255, 0, 0, 255));
    renderer.setRenderMode(RenderMode.ALPHA);
    renderer.fillRect(210, 530, 50, 50, new Color(150, 255, 0, 0));
    renderer.setRenderMode(RenderMode.NORMAL);

    ecsSystemContainer.getWorld().getEntitiesWithComponents(PositionComponent.class)
      .forEach(entity -> {
        PositionComponent position = entity.getComponent(PositionComponent.class);

        renderer.fillRect(position.getX(), position.getY(), 50, 50, Color.RED);
      });
  }

  private class TestSystem extends AbstractEcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      world.getEntitiesWithComponents(PositionComponent.class)
        .forEach(entity -> {
          PositionComponent position = entity.getComponent(PositionComponent.class);

          position.setX(position.getX() + 1);
          position.setY(position.getY() + 1);
        });
    }

    @Override
    public int getOrder() {
      return 0;
    }
  }
}
