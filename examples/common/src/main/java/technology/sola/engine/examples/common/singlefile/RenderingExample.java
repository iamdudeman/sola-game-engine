package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.AbstractEcsSystem;
import technology.sola.engine.ecs.Component;
import technology.sola.engine.ecs.World;
import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Layer;
import technology.sola.engine.graphics.RenderMode;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.Key;
import technology.sola.math.linear.Vector2D;

import java.util.List;

public class RenderingExample extends AbstractSola {
  private SolaImage solaImage;
  private SolaGraphics solaGraphics;
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

    ecsSystemContainer.setWorld(createWorld());
    ecsSystemContainer.add(new TestSystem());

    platform.getRenderer().createLayers("background", "moving_stuff", "blocks", "ui");
    solaGraphics = new SolaGraphics(ecsSystemContainer, platform.getRenderer());
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

    renderer.drawToLayer("background", r -> {
      renderer.setPixel(5, 5, Color.WHITE);
      renderer.setPixel(6, 5, Color.BLUE);
      renderer.setPixel(6, 6, Color.RED);
      renderer.setPixel(6, 7, Color.GREEN);

      renderer.drawLine(20, 50, 20, 100, Color.WHITE);
      renderer.drawLine(50, 20, 100, 20, Color.WHITE);
      renderer.drawLine(0, 220, 100, 400, Color.WHITE);

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

    solaGraphics.render();
  }

  private static class MovingComponent implements Component {
  }

  private class TestSystem extends AbstractEcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      world.getEntitiesWithComponents(TransformComponent.class, MovingComponent.class)
        .forEach(entity -> {
          TransformComponent transform = entity.getComponent(TransformComponent.class);

          transform.setX(transform.getX() + 1);
          transform.setY(transform.getY() + 1);
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

  private World createWorld() {
    World world = new World(100);

    List.of(
      new Vector2D(0, 0),
      new Vector2D(50, 20),
      new Vector2D(100, 20),
      new Vector2D(150, 20),
      new Vector2D(200, 20)
    ).forEach(vector2D -> {
      world.createEntity()
        .addComponent(new MovingComponent())
        .addComponent(new LayerComponent("moving_stuff"))
        .addComponent(new TransformComponent(vector2D.x, vector2D.y, 5, 5))
        .addComponent(new RectangleRendererComponent(Color.RED));
    });

    world.createEntity()
      .addComponent(new LayerComponent("blocks"))
      .addComponent(new TransformComponent(200, 300, 5, 5))
      .addComponent(new RectangleRendererComponent(Color.BLUE));
    world.createEntity()
      .addComponent(new LayerComponent("blocks"))
      .addComponent(new TransformComponent(200, 350, 10, 5))
      .addComponent(new RectangleRendererComponent(Color.BLUE));

    return world;
  }
}
