package technology.sola.engine.platform.android;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import technology.sola.engine.core.Sola;
import technology.sola.engine.platform.android.config.Orientation;
import technology.sola.engine.platform.android.core.SolaSurfaceView;

/**
 * The base {@link android.app.Activity} class for a sola game to run on Android.
 */
public abstract class SolaAndroidActivity extends AppCompatActivity {
  /**
   * Reference to the {@link AndroidSolaPlatform} instance running within this activity.
   */
  protected final AndroidSolaPlatform platform;
  private final AndroidSolaPlatformConfig androidSolaPlatformConfig;
  private SolaSurfaceView solaSurfaceView;

  /**
   * Creates an instance of this activity with desired configuration for the underlying {@link AndroidSolaPlatform}.
   *
   * @param androidSolaPlatformConfig the {@link AndroidSolaPlatformConfig}
   */
  public SolaAndroidActivity(AndroidSolaPlatformConfig androidSolaPlatformConfig) {
    super();

    this.androidSolaPlatformConfig = androidSolaPlatformConfig;

    this.platform = new AndroidSolaPlatform(androidSolaPlatformConfig, this);
  }

  /**
   * @return the {@link Sola} instance that starts running with this activity is created
   */
  public abstract Sola getInitialSola();

  public SolaSurfaceView getSolaSurfaceView() {
    return solaSurfaceView;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    EdgeToEdge.enable(this);

    this.solaSurfaceView = new SolaSurfaceView(this);

    var rootView = buildRootView();

    setContentView(rootView);

    ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

      return insets;
    });

    if (androidSolaPlatformConfig.orientation() == Orientation.PORTRAIT) {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    } else {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    getLifecycle().addObserver(platform);
    platform.play(getInitialSola());
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    getLifecycle().removeObserver(platform);
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);

    if (hasFocus) {
      hideSystemUi();
    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    platform.emitAndroidKeyDown(event);

    return super.onKeyDown(keyCode, event);
  }

  @Override
  public boolean onKeyUp(int keyCode, KeyEvent event) {
    platform.emitAndroidKeyUp(event);

    return super.onKeyUp(keyCode, event);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    platform.emitAndroidSimulatedMouseEvent(event);

    return super.onTouchEvent(event);
  }

  private void hideSystemUi() {
    var window = getWindow();
    var decorView = window.getDecorView();
    var windowInsetsController = WindowCompat.getInsetsController(window, decorView);

    windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
    windowInsetsController.hide(WindowInsetsCompat.Type.statusBars());
    windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars());
  }

  private View buildRootView() {
    ConstraintLayout rootView = new ConstraintLayout(this);

    rootView.setLayoutParams(new ConstraintLayout.LayoutParams(
      ConstraintLayout.LayoutParams.MATCH_PARENT,
      ConstraintLayout.LayoutParams.MATCH_PARENT
    ));
    rootView.setBackgroundColor(Color.GRAY);

    rootView.addView(solaSurfaceView);

    return rootView;
  }
}
