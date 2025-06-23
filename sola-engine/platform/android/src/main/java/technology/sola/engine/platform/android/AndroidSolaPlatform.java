package technology.sola.engine.platform.android;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import org.jetbrains.annotations.NotNull;
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
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.input.MouseWheelEvent;
import technology.sola.engine.platform.android.assets.AndroidAudioClipAssetLoader;
import technology.sola.engine.platform.android.assets.AndroidJsonAssetLoader;
import technology.sola.engine.platform.android.assets.AndroidSolaImageLoader;
import technology.sola.engine.platform.android.core.AndroidGameLoop;
import technology.sola.engine.platform.android.core.AndroidRenderer;
import technology.sola.engine.platform.android.core.AndroidRestClient;
import technology.sola.engine.platform.android.core.AndroidSocketClient;
import technology.sola.logging.SolaLogger;

import java.util.function.Consumer;

@NullMarked
public class AndroidSolaPlatform extends SolaPlatform implements LifecycleEventObserver {
  private static final SolaLogger LOGGER = SolaLogger.of(AndroidSolaPlatform.class);
  private final SolaAndroidActivity hostActivity;
  private final boolean useSoftwareRendering;

  public AndroidSolaPlatform(AndroidSolaPlatformConfig androidSolaPlatformConfig, SolaAndroidActivity hostActivity) {
    this.hostActivity = hostActivity;
    socketClient = new AndroidSocketClient();
    restClient = new AndroidRestClient();

    useSoftwareRendering = androidSolaPlatformConfig.useSoftwareRendering();
  }

  @Override
  public SolaPlatformIdentifier getIdentifier() {
    return SolaPlatformIdentifier.ANDROID;
  }

  @Override
  public void onKeyPressed(Consumer<KeyEvent> consumer) {

  }

  @Override
  public void onKeyReleased(Consumer<KeyEvent> consumer) {

  }

  @Override
  public void onMouseMoved(Consumer<MouseEvent> consumer) {

  }

  @Override
  public void onMousePressed(Consumer<MouseEvent> consumer) {

  }

  @Override
  public void onMouseReleased(Consumer<MouseEvent> consumer) {

  }

  @Override
  public void onMouseWheel(Consumer<MouseWheelEvent> consumer) {

  }

  @Override
  protected void initializePlatform(SolaConfiguration solaConfiguration, SolaPlatformInitialization solaPlatformInitialization) {
    hostActivity.setTitle(solaConfiguration.title());

    solaEventHub.add(GameLoopEvent.class, event -> {
      if (event.state() == GameLoopState.STOPPED) {
        if (socketClient.isConnected()) {
          socketClient.disconnect();
        }
      }
    });

    hostActivity.getSolaSurfaceView().setViewport(viewport);

    solaPlatformInitialization.finish();
  }

  @Override
  protected void beforeRender(Renderer renderer) {
    hostActivity.getSolaSurfaceView().startDrawing();
  }

  @Override
  protected void onRender(Renderer renderer) {
    if (useSoftwareRendering) {
      SoftwareRenderer softwareRenderer = (SoftwareRenderer) renderer;

      hostActivity.getSolaSurfaceView().drawFromSoftwareRenderer(softwareRenderer, viewport.getAspectRatioSizing());
    }

    hostActivity.getSolaSurfaceView().finishDrawing();
  }

  @Override
  protected void populateAssetLoaderProvider(AssetLoaderProvider assetLoaderProvider) {
    var assetManager = hostActivity.getAssets();
    AssetLoader<SolaImage> solaImageAssetLoader = new AndroidSolaImageLoader(assetManager);
    AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader = new AndroidJsonAssetLoader(assetManager);

    assetLoaderProvider.add(solaImageAssetLoader);
    assetLoaderProvider.add(jsonElementAssetAssetLoader);
    assetLoaderProvider.add(new AndroidAudioClipAssetLoader());

    assetLoaderProvider.add(new FontAssetLoader(
      jsonElementAssetAssetLoader, solaImageAssetLoader
    ));
    assetLoaderProvider.add(new SpriteSheetAssetLoader(
      jsonElementAssetAssetLoader, solaImageAssetLoader
    ));
    assetLoaderProvider.add(new ControlsConfigAssetLoader(
      jsonElementAssetAssetLoader
    ));
  }

  @Override
  protected Renderer buildRenderer(SolaConfiguration solaConfiguration) {
    LOGGER.info("Using %s rendering", useSoftwareRendering ? "Software" : "Android native");

    return useSoftwareRendering
      ? super.buildRenderer(solaConfiguration)
      : new AndroidRenderer();
  }

  @Override
  protected GameLoopProvider buildGameLoop() {
    return AndroidGameLoop::new;
  }

  @Override
  public void onStateChanged(@NotNull LifecycleOwner lifecycleOwner, @NotNull Lifecycle.Event event) {
    if (event == Lifecycle.Event.ON_RESUME) {
      solaEventHub.emit(new GameLoopEvent(GameLoopState.RESUME));
    } else if (event == Lifecycle.Event.ON_PAUSE) {
      solaEventHub.emit(new GameLoopEvent(GameLoopState.PAUSE));
    }
  }
}
