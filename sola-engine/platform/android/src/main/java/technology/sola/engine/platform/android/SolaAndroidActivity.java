package technology.sola.engine.platform.android;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
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
import technology.sola.engine.platform.android.core.DrawView;
import technology.sola.engine.platform.android.core.SolaAndroidPlatformConfig;

/**
 * The base {@link android.app.Activity} class for a sola game to run on Android.
 */
public abstract class SolaAndroidActivity extends AppCompatActivity {
  private final SolaAndroidPlatformConfig solaAndroidPlatformConfig;

  public SolaAndroidActivity(SolaAndroidPlatformConfig solaAndroidPlatformConfig) {
    super();
    this.solaAndroidPlatformConfig = solaAndroidPlatformConfig;
  }

  /**
   * Reference to the {@link SolaAndroidPlatform} instance running within this activity.
   */
  protected final SolaAndroidPlatform platform = new SolaAndroidPlatform();

  /**
   * @return the {@link Sola} instance that starts running with this activity is created
   */
  public abstract Sola getInitialSola();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    EdgeToEdge.enable(this);

    var rootView = buildRootView();

    setContentView(rootView);

    ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

      return insets;
    });

    if (solaAndroidPlatformConfig.orientation() == Orientation.PORTRAIT) {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    } else {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    platform.play(getInitialSola());
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);

    if (hasFocus) {
      hideSystemUi();
    }
  }

  private void hideSystemUi() {
    View decorView = getWindow().getDecorView();

    var windowInsetsController = WindowCompat.getInsetsController(getWindow(), decorView);

    windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

    windowInsetsController.hide(WindowInsetsCompat.Type.statusBars());
  }

  private View buildRootView() {
    ConstraintLayout rootView = new ConstraintLayout(this);

    rootView.setLayoutParams(new ConstraintLayout.LayoutParams(
      ConstraintLayout.LayoutParams.MATCH_PARENT,
      ConstraintLayout.LayoutParams.MATCH_PARENT
    ));
    rootView.setBackgroundColor(Color.GRAY);

    rootView.addView(new DrawView(this));

    return rootView;
  }
}
