/**
 * Defines the sola-game-engine API.
 */
module technology.sola.engine {
  requires org.slf4j;
  requires transitive technology.sola.json;
  requires transitive technology.sola.ecs;

  exports technology.sola.engine.assets;
  exports technology.sola.engine.assets.graphics;
  exports technology.sola.engine.assets.graphics.font;
  exports technology.sola.engine.assets.graphics.font.mapper;
}
