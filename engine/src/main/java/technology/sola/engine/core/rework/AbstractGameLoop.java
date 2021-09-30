package technology.sola.engine.core.rework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public abstract class AbstractGameLoop implements Runnable {
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractGameLoop.class);
  protected final FpsTracker fpsTracker = new FpsTracker();
  protected final Consumer<Float> updateMethod;
  protected final Runnable renderMethod;
  protected final boolean isRestingAllowed;
  protected final float deltaTime;
  protected boolean isRunning = false; // TODO should this be protected or private?

  protected AbstractGameLoop(Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond) {
    this(updateMethod, renderMethod, targetUpdatesPerSecond, false);
  }

  protected AbstractGameLoop(Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond, boolean isRestingAllowed) {
    this.updateMethod = updateMethod;
    this.renderMethod = renderMethod;
    this.deltaTime = 1f / targetUpdatesPerSecond;
    this.isRestingAllowed = isRestingAllowed;
  }

  public boolean isRunning() {
    return isRunning;
  }

  public void stop() {
    isRunning = false;
    LOGGER.info("Sola GameLoop is stopping");
  }

  protected void shortRest(long loopStartTime) {
    double endTime = loopStartTime + deltaTime;

    while (System.nanoTime() < endTime) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException ex) {
        LOGGER.error("rest was interrupted", ex);
        break;
      }
    }
  }
}
