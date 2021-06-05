package technology.sola.engine.core;

import java.util.function.Consumer;

public interface GameLoopProvider {
  GameLoop get(Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond, boolean isRestingAllowed);
}
