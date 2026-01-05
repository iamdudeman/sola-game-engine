package technology.sola.engine.editor.system;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.EcsSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NullMarked
public class SolaEditorModule {
  private final Map<Class<?>, SolaEditorModuleBinding<?>> bindings = new HashMap<>();

  public void bind(SolaEditorModuleBinding<?> binding) {
    bindings.put(binding.clazz, binding);
  }

  public List<EcsSystem> buildEcsSystems() {
    Map<Class<?>, Object> instances = new HashMap<>();
    List<SolaEditorModuleBinding<? extends EcsSystem>> ecsSystemsToBuild = new ArrayList<>();
    var injector = buildInjector(instances);

    // find EcsSystem bindings
    for (var binding : bindings.values()) {
      if (binding.isActive && EcsSystem.class.isAssignableFrom(binding.clazz)) {
        ecsSystemsToBuild.add((SolaEditorModuleBinding<? extends EcsSystem>) binding);
      }
    }

    // build EcsSystem instances and required dependencies
    List<EcsSystem> ecsSystems = new ArrayList<>();

    for (var ecsSystemBinding : ecsSystemsToBuild) {
      populateDependency(injector, instances, ecsSystemBinding.clazz);

      ecsSystems.add((EcsSystem) instances.get(ecsSystemBinding.clazz));
    }

    return ecsSystems;
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
