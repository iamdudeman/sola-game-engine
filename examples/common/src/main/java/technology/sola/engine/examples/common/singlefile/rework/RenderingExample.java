package technology.sola.engine.examples.common.singlefile.rework;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.core.rework.AbstractSolaRework;
import technology.sola.engine.core.rework.SolaConfiguration;
import technology.sola.engine.ecs.AbstractEcsSystem;
import technology.sola.engine.ecs.World;
import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Layer;
import technology.sola.engine.graphics.RenderMode;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.Key;
import technology.sola.engine.physics.component.PositionComponent;

public class RenderingExample extends AbstractSolaRework {
  private SolaImage solaImage;
  private float rotation = 0.1f;

  @Override
  protected SolaConfiguration buildConfiguration() {
    return new SolaConfiguration("Rendering Example", 800, 600, 30, true);
  }

  @Override
  protected void onInit() {
    AssetPool<SolaImage> solaImageAssetPool = assetPoolProvider.getAssetPool(SolaImage.class);
    AssetPool<Font> fontAssetPool = assetPoolProvider.getAssetPool(Font.class);

    Font font = fontAssetPool.addAndGetAsset("default", "assets/monospaced_NORMAL_18.json");
    platform.getRenderer().setFont(font);
    solaImage = solaImageAssetPool.addAndGetAsset("test_tiles", "assets/test_tiles.png");

    World world = new World(5);

    world.createEntity()
      .addComponent(new PositionComponent());
    world.createEntity().addComponent(new PositionComponent(50, 20));
    world.createEntity().addComponent(new PositionComponent(100, 20));
    world.createEntity().addComponent(new PositionComponent(150, 20));
    world.createEntity().addComponent(new PositionComponent(200, 20));

    ecsSystemContainer.setWorld(world);
    ecsSystemContainer.add(new TestSystem());

    platform.getRenderer().createLayers("background", "moving_stuff", "blocks", "ui");
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    renderer.drawToLayer("ui", r -> {
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

    renderer.drawToLayer("moving_stuff", r -> {
      renderer.drawImage(400, 400, solaImage);
      AffineTransform affineTransform = new AffineTransform()
        .translate(400, 400)
        .translate(-100, -100)
        .rotate(rotation)
        .scale(1.5f, 2f)
        ;
      renderer.setRenderMode(RenderMode.MASK);
      renderer.drawImage(solaImage, affineTransform);
      renderer.setRenderMode(RenderMode.NORMAL);
    });

    renderer.drawToLayer("moving_stuff", Layer.DEFAULT_PRIORITY - 10, r -> {
      ecsSystemContainer.getWorld().getEntitiesWithComponents(PositionComponent.class)
        .forEach(entity -> {
          PositionComponent position = entity.getComponent(PositionComponent.class);

          renderer.fillRect(position.getX(), position.getY(), 50, 50, Color.RED);
        });
    });

    renderer.drawToLayer("blocks", r -> {
      renderer.fillRect(200, 300, 50, 50, Color.BLUE);
      renderer.fillRect(200, 350, 100, 50, Color.BLUE);
    });

    renderer.drawToLayer("background", r -> {
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

      renderer.drawLine(0, 0, 800, 600, Color.BLUE);
      renderer.drawLine(750, 0, 20, 500, Color.BLUE);
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
        platform.getViewport().setAspectMode(AspectMode.IGNORE_RESIZING);
      } else if (keyboardInput.isKeyPressed(Key.TWO)) {
        platform.getViewport().setAspectMode(AspectMode.MAINTAIN);
      } else if (keyboardInput.isKeyPressed(Key.THREE)) {
        platform.getViewport().setAspectMode(AspectMode.STRETCH);
      }

      if (keyboardInput.isKeyHeld(Key.SPACE)) {
        rotation = rotation + 0.1f;
      }

      if (keyboardInput.isKeyPressed(Key.A)) {
        Layer blockLayer = platform.getRenderer().getLayer("blocks");
        blockLayer.setEnabled(!blockLayer.isEnabled());
      }
    }

    @Override
    public int getOrder() {
      return 0;
    }
  }
}