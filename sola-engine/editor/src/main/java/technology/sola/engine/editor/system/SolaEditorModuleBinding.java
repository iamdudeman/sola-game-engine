package technology.sola.engine.editor.system;

import org.jspecify.annotations.NullMarked;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@NullMarked
public class SolaEditorModuleBinding<T> {
  public final String name;
  public final String description;
  public final Class<T> clazz;
  public final List<Class<?>> dependencies;
  public boolean isActive = true;
  private final Function<SolaEditorModuleInjector, T> newInstance;

  public SolaEditorModuleBinding(Class<T> clazz, Function<SolaEditorModuleInjector, T> newInstance) {
    this(clazz, Collections.emptyList(), newInstance);
  }

  public SolaEditorModuleBinding(
    Class<T> clazz,
    List<Class<?>> dependencies,
    Function<SolaEditorModuleInjector, T> newInstance
  ) {
    this(clazz.getName(), "", clazz, dependencies, newInstance);
  }

  public SolaEditorModuleBinding(
    String name,
    Class<T> clazz,
    Function<SolaEditorModuleInjector, T> newInstance
  ) {
    this(name, "", clazz, Collections.emptyList(), newInstance);
  }

  public SolaEditorModuleBinding(
    String name,
    String description,
    Class<T> clazz,
    Function<SolaEditorModuleInjector, T> newInstance
  ) {
    this(name, description, clazz, Collections.emptyList(), newInstance);
  }

  public SolaEditorModuleBinding(
    String name,
    Class<T> clazz,
    List<Class<?>> dependencies,
    Function<SolaEditorModuleInjector, T> newInstance
  ) {
    this(name, "", clazz, dependencies, newInstance);
  }

  public SolaEditorModuleBinding(
    String name,
    String description,
    Class<T> clazz,
    List<Class<?>> dependencies,
    Function<SolaEditorModuleInjector, T> newInstance
  ) {
    this.name = name;
    this.description = description;
    this.clazz = clazz;
    this.dependencies = Collections.unmodifiableList(dependencies);
    this.newInstance = newInstance;
  }

  public T newInstance(SolaEditorModuleInjector injector) {
    return newInstance.apply(injector);
  }
}
