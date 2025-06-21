package technology.sola.engine.examples.android;

import technology.sola.engine.core.Sola;
import technology.sola.engine.platform.android.SolaAndroidActivity;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.platform.android.core.AndroidSolaLoggerFactory;
import technology.sola.engine.platform.android.core.SolaAndroidPlatformConfig;
import technology.sola.logging.SolaLogLevel;
import technology.sola.logging.SolaLogger;

public class MainActivity extends SolaAndroidActivity {
  static {
    SolaLogger.configure(SolaLogLevel.WARNING, new AndroidSolaLoggerFactory());
  }

  public MainActivity() {
    super(new SolaAndroidPlatformConfig());
  }

  @Override
  public Sola getInitialSola() {
    return new ExampleLauncherSola(platform);
  }
}
