package technology.sola.engine.editor.components.ecs;

import technology.sola.engine.ecs.Entity;

public interface ComponentController<T> {
  String getFxmlResource();

  void populateInitialValues();

  T create();
}
