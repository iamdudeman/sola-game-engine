package technology.sola.engine.editor.scene;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Component;
import technology.sola.json.mapper.JsonMapper;

/**
 * ComponentEditorModule defines the api needed for a {@link Component} to be able to be edited in the scene editor.
 *
 * @param <C> the type of the component
 */
@NullMarked
public interface ComponentEditorModule<C extends Component> {
  /**
   * @return the type of component this module is for
   */
  Class<C> getComponentType();

  /**
   * @return the {@link JsonMapper} for the component
   */
  JsonMapper<C> getJsonMapper();

  /**
   * The title of the component's editor panel. This defaults to the component's class simple name.
   *
   * @return the title for the editor panel
   */
  default String getTitle() {
    return getComponentType().getSimpleName().replace("Component", "");
  }

  /**
   * Creates a new instance of the component that will then be added to the entity being edited. This should initialize
   * the component with default values.
   *
   * @return the new component instance
   */
  C createNewInstance();

  /**
   * Constructs the UI for the component's editor panel.
   *
   * @param component the component instance associated with the entity being edited
   * @return the {@link ComponentEditorPanel} instance for the scene editor to mount
   */
  ComponentEditorPanel buildUi(C component);
}
