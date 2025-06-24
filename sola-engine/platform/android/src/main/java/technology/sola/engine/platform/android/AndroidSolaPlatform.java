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
import technology.sola.engine.input.Key;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.input.MouseWheelEvent;
import technology.sola.engine.platform.android.assets.AndroidAudioClipAssetLoader;
import technology.sola.engine.platform.android.assets.AndroidJsonAssetLoader;
import technology.sola.engine.platform.android.assets.AndroidSolaImageLoader;
import technology.sola.engine.platform.android.core.AndroidRenderer;
import technology.sola.engine.platform.android.core.AndroidRestClient;
import technology.sola.engine.platform.android.core.AndroidSocketClient;
import technology.sola.logging.SolaLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@NullMarked
public class AndroidSolaPlatform extends SolaPlatform implements LifecycleEventObserver {
  private static final SolaLogger LOGGER = SolaLogger.of(AndroidSolaPlatform.class);
  private final SolaAndroidActivity hostActivity;
  private final boolean useSoftwareRendering;
  private final List<Consumer<KeyEvent>> keyPressedConsumers = new ArrayList<>();
  private final List<Consumer<KeyEvent>> keyReleasedConsumers = new ArrayList<>();

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
    keyPressedConsumers.add(consumer);
  }

  @Override
  public void onKeyReleased(Consumer<KeyEvent> consumer) {
    keyReleasedConsumers.add(consumer);
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
    assetLoaderProvider.add(new AndroidAudioClipAssetLoader(assetManager));

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
  public void onStateChanged(@NotNull LifecycleOwner lifecycleOwner, @NotNull Lifecycle.Event event) {
    if (event == Lifecycle.Event.ON_RESUME) {
      solaEventHub.emit(new GameLoopEvent(GameLoopState.RESUME));
    } else if (event == Lifecycle.Event.ON_PAUSE) {
      solaEventHub.emit(new GameLoopEvent(GameLoopState.PAUSE));
    }
  }

  void emitAndroidKeyDown(android.view.KeyEvent event) {
    var keyEvent = new KeyEvent(mapKeyCode(event.getKeyCode()));

    for (var consumer : keyPressedConsumers) {
      consumer.accept(keyEvent);
    }
  }

  void emitAndroidKeyUp(android.view.KeyEvent event) {
    var keyEvent = new KeyEvent(mapKeyCode(event.getKeyCode()));

    for (var consumer : keyReleasedConsumers) {
      consumer.accept(keyEvent);
    }
  }

  private int mapKeyCode(int androidKeyCode) {
    if (androidKeyCode == android.view.KeyEvent.KEYCODE_DPAD_UP) {
      return Key.UP.getCode();
    } else if (androidKeyCode == android.view.KeyEvent.KEYCODE_DPAD_DOWN) {
      return Key.DOWN.getCode();
    } else if (androidKeyCode == android.view.KeyEvent.KEYCODE_DPAD_LEFT) {
      return Key.LEFT.getCode();
    } else if (androidKeyCode == android.view.KeyEvent.KEYCODE_DPAD_RIGHT) {
      return Key.RIGHT.getCode();
    } else if (androidKeyCode == android.view.KeyEvent.KEYCODE_SPACE) {
      return Key.SPACE.getCode();
    } else if (androidKeyCode == android.view.KeyEvent.KEYCODE_ENTER) {
      return Key.ENTER.getCode();
    }

    if (androidKeyCode >= android.view.KeyEvent.KEYCODE_0 && androidKeyCode <= android.view.KeyEvent.KEYCODE_9) {
      // Handle number keys
      return (androidKeyCode + 41);
    } else if (androidKeyCode >= android.view.KeyEvent.KEYCODE_A && androidKeyCode <= android.view.KeyEvent.KEYCODE_Z) {
      // Handle alphabet keys
      return (androidKeyCode + 36);
    }

    throw new UnsupportedOperationException("Unsupported Android key code: " + androidKeyCode);
  }
}
