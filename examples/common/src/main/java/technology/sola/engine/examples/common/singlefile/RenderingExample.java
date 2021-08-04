package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.ecs.AbstractEcsSystem;
import technology.sola.engine.ecs.World;
import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.RenderGroup;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.RenderMode;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.input.Key;
import technology.sola.engine.physics.component.PositionComponent;

public class RenderingExample extends AbstractSola {
  private SolaImage solaImage;
  private float rotation = 0.1f;

  public RenderingExample() {
    config(800, 600, 30, true);
  }

  @Override
  protected void onInit() {
    AssetPool<SolaImage> solaImageAssetPool = assetPoolProvider.getAssetPool(SolaImage.class);
    AssetPool<Font> fontAssetPool = assetPoolProvider.getAssetPool(Font.class);

    Font font = fontAssetPool.addAndGetAsset("default", "assets/monospaced_NORMAL_18.json");
    renderer.setFont(font);
    solaImage = solaImageAssetPool.addAndGetAsset("test_tiles", "assets/test_tiles.png");

    World world = new World(5);

    world.createEntity()
      .addComponent(new PositionComponent());
    world.createEntity().addComponent(new PositionComponent(50, 20));
    world.createEntity().addComponent(new PositionComponent(50, 20));
    world.createEntity().addComponent(new PositionComponent(50, 20));
    world.createEntity().addComponent(new PositionComponent(50, 20));

    ecsSystemContainer.setWorld(world);
    ecsSystemContainer.add(new TestSystem());

    renderer.createRenderGroup("background");
    renderer.createRenderGroup("moving_stuff");
    renderer.createRenderGroup("blocks");
    renderer.createRenderGroup("ui");
  }

  @Override
  protected void onRender() {
    renderer.clear();

    renderer.getRenderGroup("ui").render(renderer -> {
      renderer.setRenderMode(RenderMode.ALPHA);
      renderer.fillRect(0, 10, 600, 100, new Color(120, 255, 255, 255));
      renderer.setRenderMode(RenderMode.NORMAL);

      final String characters1 = "!\"#$%&'()*+,-./0123456789:; <=>?@ABCDEFGHIJKLMN";
      final String characters2 = "OPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

      renderer.setRenderMode(RenderMode.MASK);
      renderer.drawString(characters1, 5, 5, Color.RED);
      renderer.drawString(characters2, 5, 35, Color.BLACK);
      renderer.drawString("Hello World!", 5, 65, Color.BLUE);
      renderer.setRenderMode(RenderMode.NORMAL);
    });

    renderer.getRenderGroup("moving_stuff").render(renderer -> {
      renderer.drawImage(400, 400, solaImage);
      AffineTransform affineTransform = new AffineTransform()
        .rotate(rotation)
        .scale(.5f, .5f)
        .translate(400, 400)
        .translate(-100, -100)
        ;
      renderer.setRenderMode(RenderMode.MASK);
      renderer.drawImage(solaImage, affineTransform);
      renderer.setRenderMode(RenderMode.NORMAL);
    });

    renderer.getRenderGroup("moving_stuff").render(renderer -> {
      ecsSystemContainer.getWorld().getEntitiesWithComponents(PositionComponent.class)
        .forEach(entity -> {
          PositionComponent position = entity.getComponent(PositionComponent.class);

          renderer.fillRect(position.getX(), position.getY(), 50, 50, Color.RED);
        });
    }, RenderGroup.DEFAULT_PRIORITY - 10);

    renderer.getRenderGroup("blocks").render(renderer -> {
      renderer.fillRect(200, 300, 50, 50, Color.BLUE);
      renderer.fillRect(200, 350, 100, 50, Color.BLUE);
    });

    renderer.getRenderGroup("background").render(renderer -> {
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

      renderer.drawImage(400, 530, solaImage.getSubImage(1, 1, 16, 16));

      renderer.fillRect(180, 530, 50, 50, new Color(255, 0, 0, 255));
      renderer.setRenderMode(RenderMode.ALPHA);
      renderer.fillRect(210, 530, 50, 50, new Color(150, 255, 0, 0));
      renderer.setRenderMode(RenderMode.NORMAL);
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

      if (keyboardInput.isKeyPressed(Key.ONE)) {
        viewport.setAspectMode(AspectMode.IGNORE_RESIZING);
      } else if (keyboardInput.isKeyPressed(Key.TWO)) {
        viewport.setAspectMode(AspectMode.MAINTAIN);
      } else if (keyboardInput.isKeyPressed(Key.THREE)) {
        viewport.setAspectMode(AspectMode.STRETCH);
      }

      if (keyboardInput.isKeyHeld(Key.SPACE)) {
        rotation = rotation + 0.1f;
      }
    }

    @Override
    public int getOrder() {
      return 0;
    }
  }
}
