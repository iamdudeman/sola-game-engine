package technology.sola.engine.server;

import technology.sola.ecs.SolaEcs;
import technology.sola.engine.event.EventHub;

public abstract class TempNameSola {
  protected SolaEcs solaEcs;
  protected EventHub eventHub;

  protected TempNameSola() {
    solaEcs = new SolaEcs();
    eventHub = new EventHub();
  }

  protected abstract void onInit();

  void onUpdate(float deltaTime) {
    solaEcs.updateWorld(deltaTime);
  }
}
