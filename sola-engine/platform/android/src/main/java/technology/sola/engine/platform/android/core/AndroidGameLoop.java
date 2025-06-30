package technology.sola.engine.platform.android.core;

import technology.sola.engine.core.FixedUpdateGameLoop;
import technology.sola.engine.event.EventHub;

import java.util.function.Consumer;

/**
 * AndroidGameLoop is a {@link technology.sola.engine.core.GameLoop} implementation for
 * the {@link technology.sola.engine.platform.android.AndroidSolaPlatform}.
 */
public class AndroidGameLoop extends FixedUpdateGameLoop {
  /**
   * Creates an instance of the game loop.
   *
   * @param eventHub               the {@link EventHub} instance
   * @param updateMethod           the update method to run at target updates per second
   * @param renderMethod           the render method to run whenever there is an update
   * @param targetUpdatesPerSecond the target updates per second (update per second is capped at a max of 30)
   */
  public AndroidGameLoop(EventHub eventHub, Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond) {
    super(eventHub, updateMethod, renderMethod, Math.min(30, targetUpdatesPerSecond));
  }
}
