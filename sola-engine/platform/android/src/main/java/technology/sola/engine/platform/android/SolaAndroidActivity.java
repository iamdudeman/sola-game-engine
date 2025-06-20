package technology.sola.engine.platform.android;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import technology.sola.engine.core.Sola;
import technology.sola.engine.platform.android.core.DrawView;

/**
 * The base {@link android.app.Activity} class for a sola game to run on Android.
 */
public abstract class SolaAndroidActivity extends AppCompatActivity {
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
    decorView.setSystemUiVisibility(
      View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_FULLSCREEN
    );
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
