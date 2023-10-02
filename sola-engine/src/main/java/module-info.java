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
  exports technology.sola.engine.assets.json;
  exports technology.sola.engine.assets.loader;

  exports technology.sola.engine.core;
  exports technology.sola.engine.core.component;
  exports technology.sola.engine.core.event;

  exports technology.sola.engine.defaults;
  exports technology.sola.engine.defaults.graphics.modules;

  exports technology.sola.engine.event;

  exports technology.sola.engine.graphics;
  exports technology.sola.engine.graphics.components;
  exports technology.sola.engine.graphics.components.animation;
  exports technology.sola.engine.graphics.gui;
  exports technology.sola.engine.graphics.gui.elements;
  exports technology.sola.engine.graphics.gui.elements.container;
  exports technology.sola.engine.graphics.gui.elements.input;
  exports technology.sola.engine.graphics.gui.event;
  exports technology.sola.engine.graphics.gui.properties;
  exports technology.sola.engine.graphics.renderer;
  exports technology.sola.engine.graphics.renderer.blend;
  exports technology.sola.engine.graphics.screen;
  exports technology.sola.engine.graphics.system;

  exports technology.sola.engine.input;

  exports technology.sola.engine.physics;
  exports technology.sola.engine.physics.component;
  exports technology.sola.engine.physics.event;
  exports technology.sola.engine.physics.system;

   exports technology.sola.engine.networking;
  // todo expose rest when it is ready
  // exports technology.sola.engine.networking.rest;
  exports technology.sola.engine.networking.socket;

  exports technology.sola.math;
  exports technology.sola.math.geometry;
  exports technology.sola.math.linear;
}
