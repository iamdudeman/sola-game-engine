package technology.sola.engine.platform.automation;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.core.*;
import technology.sola.engine.core.event.Subscription;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.input.MouseWheelEvent;
import technology.sola.engine.input.TouchEvent;
import technology.sola.engine.networking.rest.JavaRestClient;
import technology.sola.engine.networking.socket.JavaSocketClient;
import technology.sola.engine.storage.FileSaveStorage;

import java.util.function.Consumer;

@NullMarked
public class AutomationTestingSolaPlatform extends SolaPlatform<AutomationTestingSolaPlatformConfig> {
  /**
   * Initializes {@link SolaPlatform} internals.
   *
   * @param platformConfig the {@link SolaPlatformConfig} for the platform
   */
  protected AutomationTestingSolaPlatform(AutomationTestingSolaPlatformConfig platformConfig) {
    super(new JavaSocketClient(), new JavaRestClient(), new FileSaveStorage(), platformConfig);
  }

  public AutomationTestingSolaPlatformDriver getDriver() {
    return new AutomationTestingSolaPlatformDriver();
  }

  @Override
  public SolaPlatformIdentifier getIdentifier() {
    return new SolaPlatformIdentifier("Automation", SolaPlatformType.Custom);
  }

  @Override
  public Subscription onKeyPressed(Consumer<KeyEvent> keyEventConsumer) {
    return null;
  }

  @Override
  public Subscription onKeyReleased(Consumer<KeyEvent> keyEventConsumer) {
    return null;
  }

  @Override
  public Subscription onMouseMoved(Consumer<MouseEvent> mouseEventConsumer) {
    return null;
  }

  @Override
  public Subscription onMousePressed(Consumer<MouseEvent> mouseEventConsumer) {
    return null;
  }

  @Override
  public Subscription onMouseReleased(Consumer<MouseEvent> mouseEventConsumer) {
    return null;
  }

  @Override
  public Subscription onMouseWheel(Consumer<MouseWheelEvent> mouseWheelEventConsumer) {
    return null;
  }

  @Override
  public Subscription onTouch(Consumer<TouchEvent> touchEventConsumer) {
    return null;
  }

  @Override
  public void setVirtualKeyboardVisible(boolean visible) {
    // No-op
  }

  @Override
  protected void initializePlatform(SolaConfiguration solaConfiguration, SolaPlatformInitialization solaPlatformInitialization) {

  }

  @Override
  protected void beforeRender(Renderer renderer) {

  }

  @Override
  protected void onRender(Renderer renderer) {

  }

  @Override
  protected void populateAssetLoaderProvider(AssetLoaderProvider assetLoaderProvider) {

  }

  @Override
  protected Renderer buildRenderer(SolaConfiguration solaConfiguration) {
    return super.buildRenderer(solaConfiguration);
  }

  @Override
  protected GameLoopProvider buildGameLoop() {
    return super.buildGameLoop();
  }
}
