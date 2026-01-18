package technology.sola.engine.editor.scene;

import technology.sola.ecs.Component;

public interface ComponentEditorModule<C extends Component> {
  Class<C> getComponentType();

  String getTitle();

  C createNewInstance();

  ComponentEditorPanel buildUi(C component);
}
