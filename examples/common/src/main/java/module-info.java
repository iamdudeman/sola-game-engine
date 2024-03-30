/**
 * Defines the dependencies for the common example project.
 */
module technology.sola.engine.examples.common {
  requires technology.sola.engine;

  exports technology.sola.engine.examples.common;
  exports technology.sola.engine.examples.common.games;
  exports technology.sola.engine.examples.common.features.networking;
  exports technology.sola.engine.examples.common.features.networking.messages;
  exports technology.sola.engine.examples.common.games.minesweeper;
  exports technology.sola.engine.examples.common.features;
}
