package technology.sola.engine.examples.android;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.Sola;
import technology.sola.engine.platform.android.SolaAndroidActivity;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.platform.android.core.AndroidSolaLoggerFactory;
import technology.sola.engine.platform.android.core.AndroidSolaPlatformConfig;
import technology.sola.logging.SolaLogLevel;
import technology.sola.logging.SolaLogger;

/**
 * MainActivity is the entry point for executing a {@link Sola} on
 * the {@link technology.sola.engine.platform.android.AndroidSolaPlatform}.
 */
@NullMarked
public class MainActivity extends SolaAndroidActivity {
  static {
    SolaLogger.configure(SolaLogLevel.WARNING, new AndroidSolaLoggerFactory());
  }

  /**
   * Creates an instance of the activity.
   */
  public MainActivity() {
    super(new AndroidSolaPlatformConfig());
  }

  @Override
  public Sola getInitialSola() {
    return new ExampleLauncherSola(platform);
  }
}
