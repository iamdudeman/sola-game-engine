package technology.sola.engine.platform.browser;

import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.renderer.SoftwareRenderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.platform.browser.assets.SolaImageAssetPool;
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
    JsMouseUtils.mouseEventListener("mousemove", (which, x, y) -> mouseEventConsumer.accept(new MouseEvent(which, x, y)));
  }

  @Override
  public void onMousePressed(Consumer<MouseEvent> mouseEventConsumer) {
    JsMouseUtils.mouseEventListener("mousedown", (which, x, y) -> mouseEventConsumer.accept(new MouseEvent(which, x, y)));
  }

  @Override
  public void onMouseReleased(Consumer<MouseEvent> mouseEventConsumer) {
    JsMouseUtils.mouseEventListener("mouseup", (which, x, y) -> mouseEventConsumer.accept(new MouseEvent(which, x, y)));
  }

  @Override
  protected void initializePlatform(SolaConfiguration solaConfiguration, SolaPlatformInitialization solaPlatformInitialization) {
    JsUtils.setTitle(solaConfiguration.solaTitle());
    JsCanvasUtils.canvasInit(solaConfiguration.canvasWidth(), solaConfiguration.canvasHeight());

    // TODO something better than this
    JsUtils.exportObject("solaStop", (JsUtils.Function) () -> solaEventHub.emit(GameLoopEvent.STOP));

    solaPlatformInitialization.finish();
  }

  @Override
  protected void beforeRender(Renderer renderer) {
    if (!useSoftwareRendering) {
      JsCanvasUtils.clearRect(renderer.getWidth(), renderer.getHeight());
    }
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

    JsCanvasUtils.renderToCanvas(pixelDataForCanvas);
  }

  @Override
  protected void populateAssetPoolProvider(AssetPoolProvider assetPoolProvider) {
    assetPoolProvider.addAssetPool(new SolaImageAssetPool());
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
}
