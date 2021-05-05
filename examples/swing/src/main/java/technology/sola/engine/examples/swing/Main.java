package technology.sola.engine.examples.swing;

import technology.sola.engine.ecs.World;
import technology.sola.engine.examples.common.components.Position;
import technology.sola.engine.examples.common.game.TestGame;
import technology.sola.engine.examples.common.systems.TestSystem;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.RenderMode;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.platform.swing.SolaImageAssetMapper;
import technology.sola.engine.platform.swing.SolaSwing;
import technology.sola.engine.platform.swing.SolaSwingPlatform;

public class Main {
  public static void main(String[] args) {
//    ExampleGame exampleGame = new ExampleGame("Swing Test", 800, 600, 30);

//    exampleGame.show();

    TestGame testGame = new TestGame();
    SolaSwingPlatform solaSwingPlatform = new SolaSwingPlatform("Swing Test", 800, 600);

    solaSwingPlatform.launch(testGame);
//    testGame.setSolaPlatform(solaSwingPlatform);
//    testGame.start();
  }

  private static class ExampleGame extends SolaSwing {
    private SolaImage solaImage;

    public ExampleGame(String title, int width, int height, int updatesPerSecond) {
      super(title, width, height, updatesPerSecond);
    }

    @Override
    protected void onInit() {
      assetLoader.addAssetMapper(new SolaImageAssetMapper());
      assetLoader.addAsset("test_tiles", "test_tiles.png");

      solaImage = assetLoader.getAsset("test_tiles", SolaImage.class);

      World world = new World(1);

      world.createEntity().addComponent(new Position());

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

      ecsSystemContainer.getWorld().getEntitiesWithComponents(Position.class)
        .forEach(entity -> {
          Position position = entity.getComponent(Position.class);

          renderer.fillRect(position.x, position.y, 50, 50, Color.RED);
        });

      super.onRender();
    }
  }
}
