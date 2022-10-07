/**
 * Defines the sola-game-engine API.
 */
module technology.sola.engine {
  requires org.slf4j;
  requires transitive technology.sola.json;
  requires transitive technology.sola.ecs;

  exports technology.sola.engine.assets;
  exports technology.sola.engine.assets.audio;
  exports technology.sola.engine.assets.exception;
  exports technology.sola.engine.assets.graphics;
  exports technology.sola.engine.assets.graphics.font;
  exports technology.sola.engine.assets.graphics.font.exception;
  exports technology.sola.engine.assets.graphics.font.mapper;

  exports technology.sola.engine.core;
  exports technology.sola.engine.core.component;
  exports technology.sola.engine.core.event;
  exports technology.sola.engine.core.module;
  exports technology.sola.engine.core.module.graphics;
  exports technology.sola.engine.core.module.graphics.gui;
  exports technology.sola.engine.core.module.physics;

  // todo event
  // todo graphics
  // todo input
  // todo physics

  exports technology.sola.math;
  exports technology.sola.math.geometry;
  exports technology.sola.math.linear;
}
