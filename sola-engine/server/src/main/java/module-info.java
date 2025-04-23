/**
 * Defines the sola-game-engine Server platform API.
 */
module technology.sola.engine.server {
  requires jdk.httpserver;
  requires transitive technology.sola.engine;

  exports technology.sola.engine.server;
  exports technology.sola.engine.server.rest;
}
