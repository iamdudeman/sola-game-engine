package technology.sola.engine.platform.automation;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.core.*;
import technology.sola.engine.core.event.Subscription;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.renderer.SoftwareRenderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.input.MouseWheelEvent;
import technology.sola.engine.input.TouchEvent;
import technology.sola.engine.networking.rest.JavaRestClient;
import technology.sola.engine.networking.socket.JavaSocketClient;
import technology.sola.engine.storage.FileSaveStorage;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * AutomationTestingSolaPlatform is a headless {@link SolaPlatform} intended for automation testing. The game loop does
 * not run on its own. Instead, an {@link AutomationTestingSolaPlatformDriver} is used to advance time, simulate input
 * and take snapshots of the rendered output.
 */
@NullMarked
public class AutomationTestingSolaPlatform extends SolaPlatform<AutomationTestingSolaPlatformConfig> {
  private final List<Consumer<KeyEvent>> keyPressedListeners = new CopyOnWriteArrayList<>();
  private final List<Consumer<KeyEvent>> keyReleasedListeners = new CopyOnWriteArrayList<>();
  private final List<Consumer<MouseEvent>> mouseMovedListeners = new CopyOnWriteArrayList<>();
  private final List<Consumer<MouseEvent>> mousePressedListeners = new CopyOnWriteArrayList<>();
  private final List<Consumer<MouseEvent>> mouseReleasedListeners = new CopyOnWriteArrayList<>();
  private final List<Consumer<MouseWheelEvent>> mouseWheelListeners = new CopyOnWriteArrayList<>();
  private final List<Consumer<TouchEvent>> touchListeners = new CopyOnWriteArrayList<>();
  private final AutomationTestingSolaPlatformDriver driver;

  /**
   * Creates an instance with the default {@link AutomationTestingSolaPlatformConfig}.
   */
  public AutomationTestingSolaPlatform() {
    this(new AutomationTestingSolaPlatformConfig());
  }

  /**
   * Creates an instance with the provided {@link AutomationTestingSolaPlatformConfig}.
   *
   * @param platformConfig the {@link AutomationTestingSolaPlatformConfig} for the platform
   */
  public AutomationTestingSolaPlatform(AutomationTestingSolaPlatformConfig platformConfig) {
    super(new JavaSocketClient(), new JavaRestClient(), new FileSaveStorage(), platformConfig);

    driver = new AutomationTestingSolaPlatformDriver(this);
  }

  /**
   * @return the {@link AutomationTestingSolaPlatformDriver} for controlling this platform
   */
  public AutomationTestingSolaPlatformDriver getDriver() {
    return driver;
  }

  @Override
  public SolaPlatformIdentifier getIdentifier() {
    return new SolaPlatformIdentifier("Automation", SolaPlatformType.Custom);
  }

  @Override
  public Subscription onKeyPressed(Consumer<KeyEvent> keyEventConsumer) {
    return subscribe(keyPressedListeners, keyEventConsumer);
  }

  @Override
  public Subscription onKeyReleased(Consumer<KeyEvent> keyEventConsumer) {
    return subscribe(keyReleasedListeners, keyEventConsumer);
  }

  @Override
  public Subscription onMouseMoved(Consumer<MouseEvent> mouseEventConsumer) {
    return subscribe(mouseMovedListeners, mouseEventConsumer);
  }

  @Override
  public Subscription onMousePressed(Consumer<MouseEvent> mouseEventConsumer) {
    return subscribe(mousePressedListeners, mouseEventConsumer);
  }

  @Override
  public Subscription onMouseReleased(Consumer<MouseEvent> mouseEventConsumer) {
    return subscribe(mouseReleasedListeners, mouseEventConsumer);
  }

  @Override
  public Subscription onMouseWheel(Consumer<MouseWheelEvent> mouseWheelEventConsumer) {
    return subscribe(mouseWheelListeners, mouseWheelEventConsumer);
  }

  @Override
  public Subscription onTouch(Consumer<TouchEvent> touchEventConsumer) {
    return subscribe(touchListeners, touchEventConsumer);
  }

  @Override
  public void setVirtualKeyboardVisible(boolean visible) {
    // No-op
  }

  @Override
  protected void initializePlatform(SolaConfiguration solaConfiguration, SolaPlatformInitialization solaPlatformInitialization) {
    solaPlatformInitialization.finish();
  }

  @Override
  protected void beforeRender(Renderer renderer) {
    // No-op
  }

  @Override
  protected void onRender(Renderer renderer) {
    // No-op, pixels are read on demand by the driver
  }

  @Override
  protected void populateAssetLoaderProvider(AssetLoaderProvider assetLoaderProvider) {
    // todo add asset loaders
  }

  @Override
  protected Renderer buildRenderer(SolaConfiguration solaConfiguration) {
    return super.buildRenderer(solaConfiguration);
  }

  @Override
  protected GameLoopProvider buildGameLoop() {
    return AutomationTestingGameLoop::new;
  }

  AutomationTestingSolaPlatformConfig getPlatformConfig() {
    return platformConfig;
  }

  AutomationTestingGameLoop getAutomationGameLoop() {
    if (gameLoop == null) {
      throw new IllegalStateException("Platform has not started. Call AutomationTestingSolaPlatform#play(Sola) first");
    }

    return (AutomationTestingGameLoop) gameLoop;
  }

  SoftwareRenderer getSoftwareRenderer() {
    if (renderer == null) {
      throw new IllegalStateException("Platform has not started. Call AutomationTestingSolaPlatform#play(Sola) first");
    }

    return (SoftwareRenderer) renderer;
  }

  void emitKeyPressed(KeyEvent event) {
    keyPressedListeners.forEach(listener -> listener.accept(event));
  }

  void emitKeyReleased(KeyEvent event) {
    keyReleasedListeners.forEach(listener -> listener.accept(event));
  }

  void emitMouseMoved(MouseEvent event) {
    mouseMovedListeners.forEach(listener -> listener.accept(event));
  }

  void emitMousePressed(MouseEvent event) {
    mousePressedListeners.forEach(listener -> listener.accept(event));
  }

  void emitMouseReleased(MouseEvent event) {
    mouseReleasedListeners.forEach(listener -> listener.accept(event));
  }

  void emitMouseWheel(MouseWheelEvent event) {
    mouseWheelListeners.forEach(listener -> listener.accept(event));
  }

  void emitTouch(TouchEvent event) {
    touchListeners.forEach(listener -> listener.accept(event));
  }

  private static <T> Subscription subscribe(List<Consumer<T>> listeners, Consumer<T> listener) {
    listeners.add(listener);

    return () -> listeners.remove(listener);
  }
}
