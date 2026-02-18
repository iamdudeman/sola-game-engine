package technology.sola.engine.editor.scene;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Component;

@NullMarked
public interface ComponentEditorModule<C extends Component> {
  Class<C> getComponentType();

  default String getTitle() {
    return getComponentType().getSimpleName().replace("Component", "");
  }

  C createNewInstance();

  ComponentEditorPanel buildUi(C component);
}
