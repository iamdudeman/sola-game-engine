package technology.sola.engine.platform.android;

import android.graphics.*;
import android.view.MotionEvent;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.FontAssetLoader;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheetAssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;
import technology.sola.engine.assets.list.AssetListAssetLoader;
import technology.sola.engine.assets.scene.SceneAssetLoader;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.SolaPlatformIdentifier;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopState;
import technology.sola.engine.core.event.Subscription;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.renderer.SoftwareRenderer;
import technology.sola.engine.graphics.screen.AspectRatioSizing;
import technology.sola.engine.input.*;
import technology.sola.engine.networking.rest.JavaRestClient;
import technology.sola.engine.networking.socket.JavaSocketClient;
import technology.sola.engine.platform.android.assets.audio.AndroidAudioClipAssetLoader;
import technology.sola.engine.platform.android.assets.AndroidJsonAssetLoader;
import technology.sola.engine.platform.android.assets.AndroidSolaImageLoader;
import technology.sola.engine.platform.android.core.AndroidGameLoop;
import technology.sola.engine.platform.android.core.AndroidRenderer;
import technology.sola.engine.storage.FileSaveStorage;
import technology.sola.engine.storage.SaveStorage;
import technology.sola.logging.SolaLogger;
import technology.sola.math.SolaMath;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * AndroidSolaPlatform is a {@link SolaPlatform} implementation for running a {@link technology.sola.engine.core.Sola}
 * in an Android app. It requires a host {@link SolaAndroidActivity} to run in.
 */
@NullMarked
public class AndroidSolaPlatform extends SolaPlatform implements LifecycleEventObserver {
  private static final SolaLogger LOGGER = SolaLogger.of(AndroidSolaPlatform.class);
  private final SolaAndroidActivity hostActivity;
  private final boolean useSoftwareRendering;
  private final int backgroundColor;
  private final List<Consumer<KeyEvent>> keyPressedConsumers = new ArrayList<>();
  private final List<Consumer<KeyEvent>> keyReleasedConsumers = new ArrayList<>();
  private final List<Consumer<TouchEvent>> touchConsumers = new ArrayList<>();
  private final Paint clearPaint = new Paint();

  /**
   * Creates an AndroidSolaPlatform instance with the desired configuration for a host activity.
   *
   * @param androidSolaPlatformConfig the {@link AndroidSolaPlatformConfig}
   * @param hostActivity              the host {@link SolaAndroidActivity}
   */
  public AndroidSolaPlatform(AndroidSolaPlatformConfig androidSolaPlatformConfig, SolaAndroidActivity hostActivity) {
    super(new JavaSocketClient(), new JavaRestClient(), new FileSaveStorage());

    this.hostActivity = hostActivity;

    useSoftwareRendering = androidSolaPlatformConfig.useSoftwareRendering();

    backgroundColor = Color.valueOf(
      androidSolaPlatformConfig.backgroundColor().getRed() / 255f,
      androidSolaPlatformConfig.backgroundColor().getGreen() / 255f,
      androidSolaPlatformConfig.backgroundColor().getBlue() / 255f,
      androidSolaPlatformConfig.backgroundColor().getAlpha() / 255f
    ).toArgb();

    clearPaint.setColor(Color.TRANSPARENT);
    clearPaint.setStyle(Paint.Style.FILL);
    clearPaint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
  }

  @Override
  public SolaPlatformIdentifier getIdentifier() {
    return SolaPlatformIdentifier.ANDROID;
  }

  @Override
  public Subscription onKeyPressed(Consumer<KeyEvent> consumer) {
    keyPressedConsumers.add(consumer);

    return () -> keyPressedConsumers.remove(consumer);
  }

  @Override
  public Subscription onKeyReleased(Consumer<KeyEvent> consumer) {
    keyReleasedConsumers.add(consumer);

    return () -> keyReleasedConsumers.remove(consumer);
  }

  /**
   * Not supported on Android.
   *
   * @param consumer the method called when mouse is moved
   * @return a no-op {@link Subscription} instance
   */
  @Override
  public Subscription onMouseMoved(Consumer<MouseEvent> consumer) {
    return Subscription.NOT_SUPPORTED;
  }

  /**
   * Not supported on Android.
   *
   * @param consumer the method called when mouse is pressed
   * @return a no-op {@link Subscription} instance
   */
  @Override
  public Subscription onMousePressed(Consumer<MouseEvent> consumer) {
    return Subscription.NOT_SUPPORTED;
  }

  /**
   * Not supported on Android.
   *
   * @param consumer the method called when mouse is released
   * @return a no-op {@link Subscription} instance
   */
  @Override
  public Subscription onMouseReleased(Consumer<MouseEvent> consumer) {
    return Subscription.NOT_SUPPORTED;
  }

  /**
   * Not supported on Android.
   *
   * @param consumer the method called when a mouse wheel interaction takes place
   * @return a no-op {@link Subscription} instance
   */
  @Override
  public Subscription onMouseWheel(Consumer<MouseWheelEvent> consumer) {
    return Subscription.NOT_SUPPORTED;
  }

  @Override
  public Subscription onTouch(Consumer<TouchEvent> touchEventConsumer) {
    touchConsumers.add(touchEventConsumer);

    return () -> touchConsumers.remove(touchEventConsumer);
  }

  @Override
  public void setVirtualKeyboardVisible(boolean visible) {
    if (visible) {
      hostActivity.showVirtualKeyboard();
    } else {
      hostActivity.hideVirtualKeyboard();
    }
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

    getSaveStorage().changeSaveDirectory(hostActivity.getApplicationContext().getFileStreamPath(SaveStorage.DEFAULT_SAVE_DIRECTORY).getPath());

    solaPlatformInitialization.finish();
  }

  @Override
  protected void beforeRender(Renderer renderer) {
    Canvas canvas = hostActivity.getSolaSurfaceView().startDrawing();

    if (canvas == null) {
      return;
    }

    canvas.drawColor(backgroundColor);

    if (!useSoftwareRendering) {
      AspectRatioSizing aspectRatioSizing = viewport.getAspectRatioSizing();

      canvas.drawRect(
        aspectRatioSizing.x(), aspectRatioSizing.y(), aspectRatioSizing.width(), aspectRatioSizing.height(),
        clearPaint
      );
      canvas.translate(aspectRatioSizing.x(), aspectRatioSizing.y());
      canvas.scale(aspectRatioSizing.width() / (float) renderer.getWidth(), aspectRatioSizing.height() / (float) renderer.getHeight());

      ((AndroidRenderer) renderer).setCanvas(canvas);
    }
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
    assetLoaderProvider.add(new AssetListAssetLoader(jsonElementAssetAssetLoader));
    assetLoaderProvider.add(new SceneAssetLoader(jsonElementAssetAssetLoader));
  }

  @Override
  protected Renderer buildRenderer(SolaConfiguration solaConfiguration) {
    LOGGER.info("Using %s rendering", useSoftwareRendering ? "Software" : "Android native");

    return useSoftwareRendering
      ? super.buildRenderer(solaConfiguration)
      : new AndroidRenderer(solaConfiguration.rendererWidth(), solaConfiguration.rendererHeight());
  }

  @Override
  protected GameLoopProvider buildGameLoop() {
    return AndroidGameLoop::new;
  }

  @Override
  public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
    if (event == Lifecycle.Event.ON_RESUME) {
      solaEventHub.emit(new GameLoopEvent(GameLoopState.RESUME));
    } else if (event == Lifecycle.Event.ON_PAUSE) {
      solaEventHub.emit(new GameLoopEvent(GameLoopState.PAUSE));
    }
  }

  void emitAndroidKeyDown(android.view.KeyEvent event) {
    if (isKnownUnsupportedKeyCode(event.getKeyCode())) {
      return;
    }

    var keyEvent = new KeyEvent(mapKeyCode(event.getKeyCode()));

    for (var consumer : keyPressedConsumers) {
      consumer.accept(keyEvent);
    }
  }

  void emitAndroidKeyUp(android.view.KeyEvent event) {
    if (isKnownUnsupportedKeyCode(event.getKeyCode())) {
      return;
    }

    var keyEvent = new KeyEvent(mapKeyCode(event.getKeyCode()));

    for (var consumer : keyReleasedConsumers) {
      consumer.accept(keyEvent);
    }
  }

  void emitTouchEvent(MotionEvent event) {
    if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
      int pointerCount = event.getPointerCount();

      for (int i = 0; i < pointerCount; i++) {
        int id = event.getPointerId(i);
        int index = event.findPointerIndex(id);

        PointerCoordinate adjusted = adjustPointerForViewport(
          SolaMath.fastRound(event.getX(index)),
          SolaMath.fastRound(event.getY(index))
        );
        TouchEvent touchEvent = new TouchEvent(new Touch(
          adjusted.x(),
          adjusted.y(),
          TouchPhase.MOVED,
          event.getPointerId(index)
        ));

        touchConsumers.forEach(consumer -> consumer.accept(touchEvent));
      }


      return;
    }

    TouchPhase touchPhase = switch (event.getActionMasked()) {
      case MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> TouchPhase.BEGAN;
      case MotionEvent.ACTION_MOVE -> TouchPhase.MOVED;
      case MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> TouchPhase.ENDED;
      case MotionEvent.ACTION_CANCEL -> TouchPhase.CANCELLED;
      default -> throw new UnsupportedOperationException("Unsupported Android touch event: " + event.getActionMasked());
    };
    int index = event.getActionIndex();

    PointerCoordinate adjusted = adjustPointerForViewport(
      SolaMath.fastRound(event.getX()),
      SolaMath.fastRound(event.getY())
    );
    TouchEvent touchEvent = new TouchEvent(new Touch(
      adjusted.x(),
      adjusted.y(),
      touchPhase,
      event.getPointerId(index)
    ));

    touchConsumers.forEach(consumer -> consumer.accept(touchEvent));
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

    return (switch (androidKeyCode) {
      case android.view.KeyEvent.KEYCODE_SHIFT_LEFT, android.view.KeyEvent.KEYCODE_SHIFT_RIGHT -> Key.SHIFT;
      case android.view.KeyEvent.KEYCODE_DEL -> Key.BACKSPACE;
      case android.view.KeyEvent.KEYCODE_COMMA -> Key.COMMA;
      case android.view.KeyEvent.KEYCODE_PERIOD -> Key.PERIOD;
      case android.view.KeyEvent.KEYCODE_SEMICOLON -> Key.SEMI_COLON;
      case android.view.KeyEvent.KEYCODE_APOSTROPHE -> Key.SINGLE_QUOTE;
      case android.view.KeyEvent.KEYCODE_SLASH -> Key.FORWARD_SLASH;
      case android.view.KeyEvent.KEYCODE_BACKSLASH -> Key.BACKSLASH;
      case android.view.KeyEvent.KEYCODE_MINUS -> Key.HYPHEN;
      case android.view.KeyEvent.KEYCODE_EQUALS -> Key.EQUALS;
      default -> {
        LOGGER.warning("Unsupported Android key code: %s", androidKeyCode);
        yield Key.CONTROL; // todo temporary fallback
      }
    }).getCode();
  }

  private boolean isKnownUnsupportedKeyCode(int keyCode) {
    return keyCode == 4;
  }
}
