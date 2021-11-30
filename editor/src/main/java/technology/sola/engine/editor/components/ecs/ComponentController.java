package technology.sola.engine.editor.components.ecs;

import javafx.scene.Node;

import java.io.IOException;

// TODO find a better shared abstraction for component controllers
// TODO figure out how to have the UI dynamically create menus for available component types

// TODO this abstraction here might need to be able to create a MenuItem as well (possibly CheckMenuItem)
// TODO would have definition of the default state of the new component

public interface ComponentController<T> {
  String getFxmlResource();

  void populateInitialValues();

  T create();

  Node getNode() throws IOException;
}
