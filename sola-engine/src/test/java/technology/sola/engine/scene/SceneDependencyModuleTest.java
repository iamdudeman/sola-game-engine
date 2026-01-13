package technology.sola.engine.scene;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@NullMarked
public class SceneDependencyModuleTest {
  @Test
  void testWhenDependenciesPresent() {
    SceneDependencyModule solaEditorModule = new SceneDependencyModule();

    solaEditorModule.bind(new SceneDependencyBinding<>(
      TestDependency.class,
      injector -> new TestDependency()
    ));

    solaEditorModule.bind(new SceneDependencyBinding<>(
      TestSystemNoDependency.class.getName(),
      TestSystemNoDependency.class,
      List.of(),
      injector -> new TestSystemNoDependency()
    ));

    solaEditorModule.bind(new SceneDependencyBinding<>(
      TestSystemWithDependency.class.getName(),
      TestSystemWithDependency.class,
      List.of(TestDependency.class),
      injector -> new TestSystemWithDependency(injector.inject(TestDependency.class))
    ));

    var ecsSystems = solaEditorModule.buildEcsSystems();

    assertDependencyPresent(ecsSystems, TestSystemNoDependency.class);
    assertDependencyPresent(ecsSystems, TestSystemWithDependency.class);
  }

  @Test
  void testWhenDependencyNotBound() {
    SceneDependencyModule solaEditorModule = new SceneDependencyModule();

    solaEditorModule.bind(new SceneDependencyBinding<>(
      TestSystemNoDependency.class.getName(),
      TestSystemNoDependency.class,
      List.of(TestMissingDependency.class),
      injector -> new TestSystemNoDependency()
    ));

    assertThrows(IllegalStateException.class, solaEditorModule::buildEcsSystems);
  }

  @Test
  void testWhenMissingDependencyDefinition() {
    SceneDependencyModule solaEditorModule = new SceneDependencyModule();

    solaEditorModule.bind(new SceneDependencyBinding<>(
      TestDependency.class.getName(),
      TestDependency.class,
      List.of(),
      injector -> new TestDependency()
    ));

    solaEditorModule.bind(new SceneDependencyBinding<>(
      TestSystemWithDependency.class.getName(),
      TestSystemWithDependency.class,
      List.of(),
      injector -> new TestSystemWithDependency(injector.inject(TestDependency.class))
    ));

    assertThrows(IllegalStateException.class, solaEditorModule::buildEcsSystems);
  }

  private static void assertDependencyPresent(List<?> dependencies, Class<?> clazz) {
    assertTrue(dependencies.stream().anyMatch(dependency -> dependency.getClass().equals(clazz)));
  }

  private static class TestDependency {
  }

  private static class TestMissingDependency {
  }

  private static class TestSystemNoDependency extends EcsSystem {
    @Override
    public void update(World world, float v) {
    }
  }

  private static class TestSystemWithDependency extends EcsSystem {
    public TestSystemWithDependency(@Nullable TestDependency dependency) {
      if (dependency == null) {
        fail("Dependency should not be null if things are working properly!");
      }
    }

    @Override
    public void update(World world, float v) {
    }
  }
}
