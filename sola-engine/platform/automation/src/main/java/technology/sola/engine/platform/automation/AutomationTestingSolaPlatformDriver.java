package technology.sola.engine.platform.automation;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.graphics.renderer.SoftwareRenderer;
import technology.sola.engine.input.*;
import technology.sola.engine.platform.automation.asset.AutomationAudioClip;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * AutomationTestingSolaPlatformDriver controls an {@link AutomationTestingSolaPlatform}. It is used to advance time in
 * the game loop, simulate keyboard and mouse input and take snapshots of the rendered output.
 * <p>
 * All methods are intended to be called from the test thread. Simulated input is delivered immediately and will be
 * processed by the next update.
 */
@NullMarked
public class AutomationTestingSolaPlatformDriver {
  private static final long INIT_TIMEOUT_MILLIS = 10_000;
  private final AutomationTestingSolaPlatform platform;

  AutomationTestingSolaPlatformDriver(AutomationTestingSolaPlatform platform) {
    this.platform = platform;
  }

  /**
   * Advances the game loop by an amount of time. Updates happen in fixed steps based on
   * {@link technology.sola.engine.core.SolaConfiguration#targetUpdatesPerSecond()} with leftover time carried over to
   * the next call. A render happens at the end if at least one update ran.
   *
   * @param seconds the time to advance in seconds
   * @return this
   */
  public AutomationTestingSolaPlatformDriver advanceTime(float seconds) {
    gameLoop().advance(seconds);
    return this;
  }

  /**
   * Advances the game loop by exactly {@code updates} fixed updates followed by a render.
   *
   * @param updates the number of updates to run
   * @return this
   */
  public AutomationTestingSolaPlatformDriver advanceUpdates(int updates) {
    gameLoop().advanceUpdates(updates);
    return this;
  }

  /**
   * Advances the game loop by a single fixed update followed by a render.
   *
   * @return this
   */
  public AutomationTestingSolaPlatformDriver advanceUpdate() {
    return advanceUpdates(1);
  }

  /**
   * @return the fixed time in seconds of a single update
   */
  public float getUpdateDeltaTime() {
    return gameLoop().getDeltaTime();
  }

  /**
   * Simulates a key being pressed.
   *
   * @param key the {@link Key} pressed
   * @return this
   */
  public AutomationTestingSolaPlatformDriver keyPress(Key key) {
    return keyPress(key.getCode());
  }

  /**
   * Simulates a key being pressed.
   *
   * @param keyCode the code of the key pressed
   * @return this
   */
  public AutomationTestingSolaPlatformDriver keyPress(int keyCode) {
    platform.emitKeyPressed(new KeyEvent(keyCode));
    return this;
  }

  /**
   * Simulates a key being released.
   *
   * @param key the {@link Key} released
   * @return this
   */
  public AutomationTestingSolaPlatformDriver keyRelease(Key key) {
    return keyRelease(key.getCode());
  }

  /**
   * Simulates a key being released.
   *
   * @param keyCode the code of the key released
   * @return this
   */
  public AutomationTestingSolaPlatformDriver keyRelease(int keyCode) {
    platform.emitKeyReleased(new KeyEvent(keyCode));
    return this;
  }

  /**
   * Simulates a key being pressed, one update running, and then the key being released.
   *
   * @param key the {@link Key} tapped
   * @return this
   */
  public AutomationTestingSolaPlatformDriver keyTap(Key key) {
    keyPress(key);
    advanceUpdate();
    return keyRelease(key);
  }

  /**
   * Simulates the mouse being moved. Coordinates are in renderer space.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @return this
   */
  public AutomationTestingSolaPlatformDriver mouseMove(int x, int y) {
    platform.emitMouseMoved(new MouseEvent(MouseButton.NONE, x, y));
    return this;
  }

  /**
   * Simulates a mouse button being pressed. Coordinates are in renderer space.
   *
   * @param button the {@link MouseButton} pressed
   * @param x      the x coordinate
   * @param y      the y coordinate
   * @return this
   */
  public AutomationTestingSolaPlatformDriver mousePress(MouseButton button, int x, int y) {
    platform.emitMousePressed(new MouseEvent(button, x, y));
    return this;
  }

  /**
   * Simulates a mouse button being released. Coordinates are in renderer space.
   *
   * @param button the {@link MouseButton} released
   * @param x      the x coordinate
   * @param y      the y coordinate
   * @return this
   */
  public AutomationTestingSolaPlatformDriver mouseRelease(MouseButton button, int x, int y) {
    platform.emitMouseReleased(new MouseEvent(button, x, y));
    return this;
  }

  /**
   * Simulates a mouse click: moves to the coordinate, presses the button, runs one update and then releases it.
   * Coordinates are in renderer space.
   *
   * @param button the {@link MouseButton} clicked
   * @param x      the x coordinate
   * @param y      the y coordinate
   * @return this
   */
  public AutomationTestingSolaPlatformDriver mouseClick(MouseButton button, int x, int y) {
    mouseMove(x, y);
    mousePress(button, x, y);
    advanceUpdate();
    return mouseRelease(button, x, y);
  }

  /**
   * Simulates a mouse wheel interaction.
   *
   * @param mouseWheelEvent the {@link MouseWheelEvent}
   * @return this
   */
  public AutomationTestingSolaPlatformDriver mouseWheel(MouseWheelEvent mouseWheelEvent) {
    platform.emitMouseWheel(mouseWheelEvent);
    return this;
  }

  /**
   * Simulates a touch interaction.
   *
   * @param touchEvent the {@link TouchEvent}
   * @return this
   */
  public AutomationTestingSolaPlatformDriver touch(TouchEvent touchEvent) {
    platform.emitTouch(touchEvent);
    return this;
  }

  /**
   * @return a copy of the renderer's current pixels as an ARGB {@link BufferedImage}
   */
  public BufferedImage captureImage() {
    SoftwareRenderer renderer = renderer();
    BufferedImage image = new BufferedImage(renderer.getWidth(), renderer.getHeight(), BufferedImage.TYPE_INT_ARGB);
    int[] pixels = renderer.getPixels();
    int[] imageData = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    System.arraycopy(pixels, 0, imageData, 0, pixels.length);

    return image;
  }

  /**
   * Saves the renderer's current pixels as a png in
   * {@link AutomationTestingSolaPlatformConfig#snapshotDirectory()}.
   *
   * @param name the name of the snapshot (".png" is appended if not present)
   * @return the {@link Path} of the written file
   */
  public Path snapshot(String name) {
    String fileName = name.endsWith(".png") ? name : name + ".png";

    return snapshot(platform.getPlatformConfig().snapshotDirectory().resolve(fileName));
  }

  /**
   * Saves the renderer's current pixels as a png at the provided path. Parent directories are created if needed.
   *
   * @param path the {@link Path} to write to
   * @return the {@link Path} of the written file
   */
  public Path snapshot(Path path) {
    BufferedImage image = captureImage();

    try {
      Path parent = path.toAbsolutePath().getParent();

      if (parent != null) {
        Files.createDirectories(parent);
      }

      if (!ImageIO.write(image, "png", path.toFile())) {
        throw new IllegalStateException("No png ImageIO writer available");
      }
    } catch (IOException ex) {
      throw new UncheckedIOException("Failed to write snapshot to " + path, ex);
    }

    return path;
  }

  /**
   * Simulates an audio clip finishing.
   *
   * @param assetLoaderProvider the {@link AssetLoaderProvider} to use
   * @param id                  the id of the audio clip to simulate finishing
   */
  public void simulateFinishedAudioClip(AssetLoaderProvider assetLoaderProvider, String id) {
    AutomationAudioClip automationAudioClip = (AutomationAudioClip) assetLoaderProvider.get(AudioClip.class)
      .get(id)
      .getAsset();

    automationAudioClip.forceFinished();
  }

  private AutomationTestingGameLoop gameLoop() {
    AutomationTestingGameLoop gameLoop = platform.getAutomationGameLoop();

    gameLoop.awaitStarted(INIT_TIMEOUT_MILLIS);

    return gameLoop;
  }

  private SoftwareRenderer renderer() {
    gameLoop();

    return platform.getSoftwareRenderer();
  }
}
