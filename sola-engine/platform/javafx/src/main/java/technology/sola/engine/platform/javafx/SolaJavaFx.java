package technology.sola.engine.platform.javafx;

import javafx.application.Platform;
import org.jspecify.annotations.NullMarked;

/**
 * SolaJavaFx is a JavaFx class holding utility methods for working with JavaFx.
 */
@NullMarked
public class SolaJavaFx {
  private static boolean isPlatformStartupNeeded = true;

  /**
   * Starts a {@link Runnable} on the application thread. Also starts up {@link Platform} if it has not yet been
   * started.
   *
   * @param runnable the {@link Runnable}
   */
  public static void startOnApplicationThread(Runnable runnable) {
    if (isPlatformStartupNeeded) {
      Platform.startup(runnable);
      isPlatformStartupNeeded = false;
    } else {
      Platform.runLater(runnable);
    }
  }

  private SolaJavaFx() {
  }
}
