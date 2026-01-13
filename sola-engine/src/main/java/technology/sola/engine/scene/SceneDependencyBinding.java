package technology.sola.engine.scene;

import org.jspecify.annotations.NullMarked;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@NullMarked
public class SceneDependencyBinding<T> {
  public final String name;
  public final String description;
  public boolean isActive = true;
  final List<Class<?>> dependencies;
  final Class<T> clazz;
  private final Function<SceneDependencyInjector, T> newInstance;

  public SceneDependencyBinding(Class<T> clazz, Function<SceneDependencyInjector, T> newInstance) {
    this(clazz, Collections.emptyList(), newInstance);
  }

  public SceneDependencyBinding(
    Class<T> clazz,
    List<Class<?>> dependencies,
    Function<SceneDependencyInjector, T> newInstance
  ) {
    this(clazz.getName(), "", clazz, dependencies, newInstance);
  }

  public SceneDependencyBinding(
    String name,
    Class<T> clazz,
    Function<SceneDependencyInjector, T> newInstance
  ) {
    this(name, "", clazz, Collections.emptyList(), newInstance);
  }

  public SceneDependencyBinding(
    String name,
    String description,
    Class<T> clazz,
    Function<SceneDependencyInjector, T> newInstance
  ) {
    this(name, description, clazz, Collections.emptyList(), newInstance);
  }

  public SceneDependencyBinding(
    String name,
    Class<T> clazz,
    List<Class<?>> dependencies,
    Function<SceneDependencyInjector, T> newInstance
  ) {
    this(name, "", clazz, dependencies, newInstance);
  }

  public SceneDependencyBinding(
    String name,
    String description,
    Class<T> clazz,
    List<Class<?>> dependencies,
    Function<SceneDependencyInjector, T> newInstance
  ) {
    this.name = name;
    this.description = description;
    this.clazz = clazz;
    this.dependencies = Collections.unmodifiableList(dependencies);
    this.newInstance = newInstance;
  }

  T newInstance(SceneDependencyInjector injector) {
    return newInstance.apply(injector);
  }
}
