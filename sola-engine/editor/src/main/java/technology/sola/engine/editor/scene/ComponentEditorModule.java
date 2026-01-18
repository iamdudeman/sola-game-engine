package technology.sola.engine.editor.scene;

import technology.sola.ecs.Component;
import technology.sola.engine.editor.core.components.EditorPanel;

public abstract class ComponentEditorModule<C extends Component> extends EditorPanel {
  public ComponentEditorModule() {
    setSpacing(8);
  }

  public abstract Class<C> getComponentType();

  public abstract String getTitle();

  public abstract C onAddComponent();

  public abstract void onEntitySelect(C component);
}
