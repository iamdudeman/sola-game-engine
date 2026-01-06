package technology.sola.engine.editor.system;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.EcsSystem;
import technology.sola.engine.graphics.modules.SolaGraphicsModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NullMarked
public class SolaEditorModule {
  private final Map<Class<?>, SolaEditorModuleBinding<?>> bindings = new HashMap<>();

  // todo populate JSON object with Class<?> to active state mapping to populate this for a Scene

  public void bind(SolaEditorModuleBinding<?> binding) {
    bindings.put(binding.clazz, binding);
  }

  public List<SolaEditorModuleBinding<? extends EcsSystem>> getEcsSystemBindings() {
    return getBindingsForType(EcsSystem.class);
  }

  public List<EcsSystem> buildEcsSystems() {
    return buildForType(EcsSystem.class);
  }

  public List<SolaEditorModuleBinding<? extends SolaGraphicsModule>> getGraphicsModuleBindings() {
    return getBindingsForType(SolaGraphicsModule.class);
  }

  public List<SolaGraphicsModule> buildGraphicsModules() {
    return buildForType(SolaGraphicsModule.class);
  }

  private <T> List<SolaEditorModuleBinding<? extends T>> getBindingsForType(Class<T> typeClass) {
    List<SolaEditorModuleBinding<? extends T>> instancesOfType = new ArrayList<>();

    // find T bindings
    for (var binding : bindings.values()) {
      if (typeClass.isAssignableFrom(binding.clazz)) {
        instancesOfType.add((SolaEditorModuleBinding<? extends T>) binding);
      }
    }

    return instancesOfType;
  }

  private <T> List<T> buildForType(Class<T> typeClass) {
    Map<Class<?>, Object> instances = new HashMap<>();
    var injector = buildInjector(instances);
    var instancesToBuild = getBindingsForType(typeClass);

    // build T instances and required dependencies
    List<T> graphicsModules = new ArrayList<>();

    for (var binding : instancesToBuild) {
      if (binding.isActive) {
        populateDependency(injector, instances, binding.clazz);

        graphicsModules.add((T) instances.get(binding.clazz));
      }
    }

    return graphicsModules;
  }

  private void populateDependency(
    SolaEditorModuleInjector injector,
    Map<Class<?>, Object> instances,
    Class<?> dependencyClass
  ) {
    if (instances.containsKey(dependencyClass)) {
      return;
    }

    for (var binding : bindings.values()) {
      if (binding.clazz.equals(dependencyClass)) {
        if (isMissingDependency(instances, binding)) {
          for (var dependency : binding.dependencies) {
            populateDependency(injector, instances, dependency);
          }
        }

        if (isMissingDependency(instances, binding)) {
          for (var dependency : binding.dependencies) {
            if (!instances.containsKey(dependency)) {
              throw new IllegalStateException(
                "Could not find dependency " + dependency.getName() + " for binding: " + binding.clazz.getName()
              );
            }
          }
        }

        instances.put(dependencyClass, binding.newInstance(injector));
      }
    }
  }

  private boolean isMissingDependency(Map<Class<?>, Object> instances, SolaEditorModuleBinding<?> binding) {
    var dependencies = binding.dependencies;

    if (dependencies.isEmpty()) {
      return false;
    }

    return !dependencies.stream().allMatch(instances::containsKey);
  }

  private SolaEditorModuleInjector buildInjector(Map<Class<?>, Object> instances) {
    return new SolaEditorModuleInjector() {
      @Override
      public <S> S inject(Class<S> systemClass) {
        var instance = (S) instances.get(systemClass);

        if (instance == null) {
          throw new IllegalStateException(
            "Could not find instance of " + systemClass
            + ". Did you forget to bind it or add it to the dependencies list of the SolaEditorModuleBinding?"
          );
        }

        return instance;
      }
    };
  }
}
