package technology.sola.engine.platform.browser;

import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.renderer.SoftwareRenderer;
import technology.sola.engine.graphics.screen.AspectRatioSizing;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.platform.browser.assets.BrowserAudioClipAssetLoader;
import technology.sola.engine.platform.browser.assets.BrowserFontAssetLoader;
import technology.sola.engine.platform.browser.assets.BrowserSolaImageAssetLoader;
import technology.sola.engine.platform.browser.assets.BrowserSpriteSheetAssetLoader;
import technology.sola.engine.platform.browser.core.BrowserCanvasRenderer;
import technology.sola.engine.platform.browser.core.BrowserGameLoop;
import technology.sola.engine.platform.browser.javascript.JsCanvasUtils;
import technology.sola.engine.platform.browser.javascript.JsKeyboardUtils;
import technology.sola.engine.platform.browser.javascript.JsMouseUtils;
import technology.sola.engine.platform.browser.javascript.JsUtils;

import java.util.function.Consumer;

public class BrowserSolaPlatform extends SolaPlatform {
  private final boolean useSoftwareRendering;

  public BrowserSolaPlatform() {
    this(true);
  }

  public BrowserSolaPlatform(boolean useSoftwareRendering) {
    this.useSoftwareRendering = useSoftwareRendering;
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
    JsMouseUtils.mouseEventListener("mousemove", (which, x, y) -> mouseEventConsumer.accept(browserToSola(which, x, y)));
  }

  @Override
  public void onMousePressed(Consumer<MouseEvent> mouseEventConsumer) {
    JsMouseUtils.mouseEventListener("mousedown", (which, x, y) -> mouseEventConsumer.accept(browserToSola(which, x, y)));
  }

  @Override
  public void onMouseReleased(Consumer<MouseEvent> mouseEventConsumer) {
    JsMouseUtils.mouseEventListener("mouseup", (which, x, y) -> mouseEventConsumer.accept(browserToSola(which, x, y)));
  }

  @Override
  protected void initializePlatform(SolaConfiguration solaConfiguration, SolaPlatformInitialization solaPlatformInitialization) {
    JsUtils.setTitle(solaConfiguration.solaTitle());
    JsCanvasUtils.canvasInit(JsCanvasUtils.ID_SOLA_ANCHOR, solaConfiguration.canvasWidth(), solaConfiguration.canvasHeight());

    JsCanvasUtils.observeResize((int width, int height) -> viewport.resize(width, height));

    // TODO something better than this
    JsUtils.exportObject("solaStop", (JsUtils.Function) () -> solaEventHub.emit(GameLoopEvent.STOP));

    solaPlatformInitialization.finish();
  }

  @Override
  protected void beforeRender(Renderer renderer) {
    JsCanvasUtils.clearRect();
  }

  @Override
  protected void onRender(Renderer renderer) {
    int[] pixels = ((SoftwareRenderer) renderer).getPixels();
    int[] pixelDataForCanvas = new int[pixels.length * 4];
    int index = 0;

    for (int current : pixels) {
      Color color = new Color(current);

      pixelDataForCanvas[index++] = color.getRed();
      pixelDataForCanvas[index++] = color.getGreen();
      pixelDataForCanvas[index++] = color.getBlue();
      pixelDataForCanvas[index++] = color.getAlpha();
    }

    AspectRatioSizing aspectRatioSizing = viewport.getAspectRatioSizing();

    JsCanvasUtils.renderToCanvas(pixelDataForCanvas, renderer.getWidth(), renderer.getHeight(), aspectRatioSizing.x(), aspectRatioSizing.y(), aspectRatioSizing.width(), aspectRatioSizing.height());
  }

  @Override
  protected void populateAssetLoaderProvider(AssetLoaderProvider assetLoaderProvider) {
    AssetLoader<SolaImage> solaImageAssetLoader = new BrowserSolaImageAssetLoader();
    assetLoaderProvider.add(solaImageAssetLoader);
    assetLoaderProvider.add(new BrowserSpriteSheetAssetLoader(solaImageAssetLoader));
    assetLoaderProvider.add(new BrowserFontAssetLoader(solaImageAssetLoader));
    assetLoaderProvider.add(new BrowserAudioClipAssetLoader());
  }

  @Override
  protected GameLoopProvider buildGameLoop() {
    return BrowserGameLoop::new;
  }

  @Override
  protected Renderer buildRenderer(SolaConfiguration solaConfiguration) {
    LOGGER.info("Using {} rendering", useSoftwareRendering ? "Software" : "Canvas");

    return useSoftwareRendering
      ? super.buildRenderer(solaConfiguration)
      : new BrowserCanvasRenderer(solaConfiguration.canvasWidth(), solaConfiguration.canvasHeight());
  }

  private MouseEvent browserToSola(int which, int x, int y) {
    MouseCoordinate adjusted = adjustMouseForViewport(x, y);

    return new MouseEvent(which, adjusted.x(), adjusted.y());
  }
}
