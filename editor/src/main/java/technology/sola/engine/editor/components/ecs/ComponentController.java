package technology.sola.engine.editor.components.ecs;

import technology.sola.engine.ecs.Entity;

// TODO find a better shared abstraction for component controllers
// TODO figure out how to have the UI dynamically create menus for available component types

public interface ComponentController<T> {
  String getFxmlResource();

  void populateInitialValues();

  T create();
}
