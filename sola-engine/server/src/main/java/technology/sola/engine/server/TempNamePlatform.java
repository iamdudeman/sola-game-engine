package technology.sola.engine.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.core.GameLoop;
import technology.sola.engine.event.EventHub;

public class TempNamePlatform {
  protected static final Logger LOGGER = LoggerFactory.getLogger(TempNamePlatform.class);
  protected GameLoop gameLoop;
  protected EventHub solaEventHub;

  public void play(TempNameSola tempNameSola, int targetUpdatesPerSecond) {
    LOGGER.info("Using platform [{}]", this.getClass().getName());

    this.solaEventHub = tempNameSola.eventHub;

    this.gameLoop = new ServerGameLoop(solaEventHub, tempNameSola::onUpdate, targetUpdatesPerSecond);

    new Thread(gameLoop).start();
  }
}
