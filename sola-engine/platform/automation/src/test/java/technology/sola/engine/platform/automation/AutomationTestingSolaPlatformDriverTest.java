package technology.sola.engine.platform.automation;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import technology.sola.engine.assets.BulkAssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.json.JsonElementAsset;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.MouseButton;
import technology.sola.logging.JavaSolaLoggerFactory;
import technology.sola.logging.SolaLogLevel;
import technology.sola.logging.SolaLogger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@NullMarked
class AutomationTestingSolaPlatformDriverTest {
  static {
    SolaLogger.configure(SolaLogLevel.WARNING, new JavaSolaLoggerFactory());
  }

  private TestSola testSola;
  private AutomationTestingSolaPlatformDriver driver;
  @TempDir
  private Path tempDir;

  @BeforeEach
  void setup() {
    AutomationTestingSolaPlatform platform = new AutomationTestingSolaPlatform(
      new AutomationTestingSolaPlatformConfig(tempDir)
    );

    testSola = new TestSola();
    platform.play(testSola);
    driver = platform.getDriver();
  }

  @Nested
  class advanceTime {
    @Test
    void shouldRunFixedUpdates() {
      driver.advanceTime(1);

      assertEquals(30, testSola.updateCount);
      assertEquals(1, testSola.renderCount);
    }

    @Test
    void shouldCarryOverPartialTime() {
      float halfStep = driver.getUpdateDeltaTime() / 2;

      driver.advanceTime(halfStep);
      assertEquals(0, testSola.updateCount);
      assertEquals(0, testSola.renderCount);

      driver.advanceTime(halfStep);
      assertEquals(1, testSola.updateCount);
      assertEquals(1, testSola.renderCount);
    }

    @Test
    void shouldRunExactUpdates() {
      driver.advanceUpdates(5);

      assertEquals(5, testSola.updateCount);
      assertEquals(1, testSola.renderCount);
    }
  }

  @Nested
  class input {
    @Test
    void shouldHandleKeyPressAndRelease() {
      driver.keyPress(Key.D).advanceUpdate();
      assertTrue(testSola.wasDPressed);

      // first update is "pressed", subsequent updates are "held"
      driver.advanceUpdates(3);
      assertEquals(3, testSola.boxX);

      driver.keyRelease(Key.D).advanceUpdates(3);
      assertEquals(3, testSola.boxX);
    }

    @Test
    void shouldHandleMouseClick() {
      driver.mouseClick(MouseButton.PRIMARY, 50, 60);

      assertEquals(50, testSola.clickX);
      assertEquals(60, testSola.clickY);
    }
  }

  @Nested
  class snapshot {
    @Test
    void shouldCaptureRenderedPixels() {
      driver.advanceUpdate();

      BufferedImage image = driver.captureImage();

      assertEquals(Color.WHITE.hexInt(), image.getRGB(5, 5));
      assertEquals(Color.BLACK.hexInt(), image.getRGB(50, 50));
    }

    @Test
    void shouldWriteSnapshotToSnapshotDirectory() throws IOException {
      driver.advanceUpdate();

      Path path = driver.snapshot("initial");

      assertEquals(tempDir.resolve("initial.png"), path);
      assertTrue(Files.exists(path));

      BufferedImage image = ImageIO.read(path.toFile());

      assertEquals(80, image.getWidth());
      assertEquals(60, image.getHeight());
      assertEquals(Color.WHITE.hexInt(), image.getRGB(5, 5));
    }
  }

  private static class TestSola extends Sola {
    int updateCount = 0;
    int renderCount = 0;
    int boxX = 0;
    boolean wasDPressed = false;
    int clickX = -1;
    int clickY = -1;

    TestSola() {
      super(new SolaConfiguration("Test", 80, 60, 30));
    }

    @Override
    protected void onInit() {
    }

    @Override
    protected void onAsyncInit(Runnable completeAsyncInit) {
      new BulkAssetLoader(assetLoaderProvider)
        .addAsset(SolaImage.class, "testimage", "assets/images/duck.png")
//        .addAsset(AudioClip.class, "testaudio", "assets/audio/asgaseg.wav")
        .addAsset(JsonElementAsset.class, "testjson", "assets/config.aseets.json")
        .loadAll()
        .onComplete((assets) -> {
          completeAsyncInit.run();
        });
    }

    @Override
    protected void onUpdate(float deltaTime) {
      super.onUpdate(deltaTime);

      updateCount++;

      if (keyboardInput.isKeyPressed(Key.D)) {
        wasDPressed = true;
      }

      if (keyboardInput.isKeyHeld(Key.D)) {
        boxX++;
      }

      if (mouseInput.isMousePressed(MouseButton.PRIMARY)) {
        clickX = (int) mouseInput.getMousePosition().x();
        clickY = (int) mouseInput.getMousePosition().y();
      }
    }

    @Override
    protected void onRender(Renderer renderer) {
      renderCount++;

      renderer.clear(Color.BLACK);
      renderer.fillRect(boxX, 0, 10, 10, Color.WHITE);
    }
  }
}
