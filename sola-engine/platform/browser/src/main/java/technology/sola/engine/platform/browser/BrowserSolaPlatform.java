package technology.sola.engine.platform.browser;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.FontAssetLoader;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheetAssetLoader;
import technology.sola.engine.assets.input.ControlsConfigAssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.SolaPlatformIdentifier;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopState;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.renderer.SoftwareRenderer;
import technology.sola.engine.graphics.screen.AspectRatioSizing;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.input.MouseWheelEvent;
import technology.sola.engine.platform.browser.assets.BrowserJsonElementAssetLoader;
import technology.sola.engine.platform.browser.assets.audio.BrowserAudioClipAssetLoader;
import technology.sola.engine.platform.browser.assets.graphics.BrowserSolaImageAssetLoader;
import technology.sola.engine.platform.browser.core.BrowserCanvasRenderer;
import technology.sola.engine.platform.browser.core.BrowserGameLoop;
import technology.sola.engine.platform.browser.core.BrowserRestClient;
import technology.sola.engine.platform.browser.core.BrowserSocketClient;
import technology.sola.engine.platform.browser.javascript.JsCanvasUtils;
import technology.sola.engine.platform.browser.javascript.JsKeyboardUtils;
import technology.sola.engine.platform.browser.javascript.JsMouseUtils;
import technology.sola.engine.platform.browser.javascript.JsUtils;
import technology.sola.logging.SolaLogger;

import java.util.function.Consumer;

/**
 * BrowserSolaPlatform is a {@link SolaPlatform} implementation for running a {@link technology.sola.engine.core.Sola} in
 * a web browser.
 */
@NullMarked
public class BrowserSolaPlatform extends SolaPlatform {
  private static final SolaLogger LOGGER = SolaLogger.of(BrowserSolaPlatform.class);
  private final boolean useSoftwareRendering;

  /**
   * Creates a BrowserSolaPlatform instance using default {@link BrowserSolaPlatformConfig}.
   */
  public BrowserSolaPlatform() {
    this(new BrowserSolaPlatformConfig());
  }

  /**
   * Creates a BrowserSolaPlatform instance with desired configuration.
   *
   * @param platformConfig the {@link BrowserSolaPlatformConfig}
   */
  public BrowserSolaPlatform(BrowserSolaPlatformConfig platformConfig) {
    this.useSoftwareRendering = platformConfig.useSoftwareRendering();
    this.socketClient = new BrowserSocketClient();
    this.restClient = new BrowserRestClient();
  }

  @Override
  public SolaPlatformIdentifier getIdentifier() {
    return SolaPlatformIdentifier.WEB_BROWSER;
  }

  @Override
  public void onKeyPressed(Consumer<KeyEvent> keyEventConsumer) {
    JsKeyboardUtils.keyEventListener("keydown", keyCode -> keyEventConsumer.accept(new KeyEvent(keyCode)));
  }

  @Override
  public void onKeyReleased(Consumer<KeyEvent> keyEventConsumer) {
    JsKeyboardUtils.keyEventListener("keyup", keyCode -> keyEventConsumer.accept(new KeyEvent(keyCode)));
  }

  @Override
  public void onMouseMoved(Consumer<MouseEvent> mouseEventConsumer) {
    JsMouseUtils.mouseEventListener("mousemove", (which, x, y) ->
      mouseEventConsumer.accept(browserToSola(which, x, y)));
  }

  @Override
  public void onMousePressed(Consumer<MouseEvent> mouseEventConsumer) {
    JsMouseUtils.mouseEventListener("mousedown", (which, x, y) ->
      mouseEventConsumer.accept(browserToSola(which, x, y)));
  }

  @Override
  public void onMouseReleased(Consumer<MouseEvent> mouseEventConsumer) {
    JsMouseUtils.mouseEventListener("mouseup", (which, x, y) ->
      mouseEventConsumer.accept(browserToSola(which, x, y)));
  }

  @Override
  public void onMouseWheel(Consumer<MouseWheelEvent> mouseWheelEventConsumer) {
    JsMouseUtils.mouseWheelEventListener((isUp, isDown, isLeft, isRight) ->
      mouseWheelEventConsumer.accept(new MouseWheelEvent(isUp, isDown, isLeft, isRight)));
  }

  @Override
  protected void initializePlatform(SolaConfiguration solaConfiguration, SolaPlatformInitialization solaPlatformInitialization) {
    JsUtils.setTitle(solaConfiguration.title());
    JsCanvasUtils.canvasInit(JsCanvasUtils.ID_SOLA_ANCHOR, solaConfiguration.rendererWidth(), solaConfiguration.rendererHeight());
    JsKeyboardUtils.init();
    JsMouseUtils.init();

    JsCanvasUtils.observeCanvasResize((int width, int height) -> viewport.resize(width, height));
    JsCanvasUtils.observeCanvasFocus(isFocused -> {
      if (isFocused) {
        solaEventHub.emit(new GameLoopEvent(GameLoopState.RESUME));
      } else {
        solaEventHub.emit(new GameLoopEvent(GameLoopState.PAUSE));
      }
    });

    solaEventHub.add(GameLoopEvent.class, event -> {
      if (event.state() == GameLoopState.STOPPED) {
        socketClient.disconnect();
      }
    });

    solaPlatformInitialization.finish();
  }

  @Override
  protected void beforeRender(Renderer renderer) {
    JsCanvasUtils.clearRect();

    if (!useSoftwareRendering) {
      AspectRatioSizing aspectRatioSizing = viewport.getAspectRatioSizing();

      JsCanvasUtils.updateAspectRato(
        aspectRatioSizing.x(), aspectRatioSizing.y(),
        aspectRatioSizing.width() / (float) renderer.getWidth(), aspectRatioSizing.height() / (float) renderer.getHeight()
      );
    }
  }

  @Override
  protected void onRender(Renderer renderer) {
    if (useSoftwareRendering) {
      AspectRatioSizing aspectRatioSizing = viewport.getAspectRatioSizing();
      var pixels = ((SoftwareRenderer) renderer).getPixels();

      JsCanvasUtils.renderToCanvas(
        pixels,
        renderer.getWidth(), renderer.getHeight(),
        aspectRatioSizing.x(), aspectRatioSizing.y(), aspectRatioSizing.width(), aspectRatioSizing.height()
      );
    }
  }

  @Override
  protected void populateAssetLoaderProvider(AssetLoaderProvider assetLoaderProvider) {
    AssetLoader<SolaImage> solaImageAssetLoader = new BrowserSolaImageAssetLoader();
    AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader = new BrowserJsonElementAssetLoader();

    assetLoaderProvider.add(solaImageAssetLoader);
    assetLoaderProvider.add(jsonElementAssetAssetLoader);
    assetLoaderProvider.add(new BrowserAudioClipAssetLoader());

    assetLoaderProvider.add(new SpriteSheetAssetLoader(
      jsonElementAssetAssetLoader, solaImageAssetLoader
    ));
    assetLoaderProvider.add(new FontAssetLoader(
      jsonElementAssetAssetLoader, solaImageAssetLoader
    ));
    assetLoaderProvider.add(new ControlsConfigAssetLoader(
      jsonElementAssetAssetLoader
    ));
  }

  @Override
  protected GameLoopProvider buildGameLoop() {
    return BrowserGameLoop::new;
  }

  @Override
  protected Renderer buildRenderer(SolaConfiguration solaConfiguration) {
    LOGGER.info("Using %s rendering", useSoftwareRendering ? "Software" : "Canvas");

    return useSoftwareRendering
      ? super.buildRenderer(solaConfiguration)
      : new BrowserCanvasRenderer(solaConfiguration.rendererWidth(), solaConfiguration.rendererHeight());
  }

  private MouseEvent browserToSola(int which, int x, int y) {
    MouseCoordinate adjusted = adjustMouseForViewport(x, y);

    return new MouseEvent(which, adjusted.x(), adjusted.y());
  }
}
