package technology.sola.engine.editor.system;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@NullMarked
public class SolaEditorModuleTest {
  @Test
  void testWhenDependenciesPresent() {
    SolaEditorModule solaEditorModule = new SolaEditorModule();

    solaEditorModule.bind(new SolaEditorModuleBinding<>(
      TestDependency.class,
      injector -> new TestDependency()
    ));

    solaEditorModule.bind(new SolaEditorModuleBinding<>(
      TestSystemNoDependency.class.getName(),
      TestSystemNoDependency.class,
      List.of(),
      injector -> new TestSystemNoDependency()
    ));

    solaEditorModule.bind(new SolaEditorModuleBinding<>(
      TestSystemWithDependency.class.getName(),
      TestSystemWithDependency.class,
      List.of(TestDependency.class),
      injector -> new TestSystemWithDependency(injector.inject(TestDependency.class))
    ));

    var ecsSystems = solaEditorModule.buildEcsSystems();

    assertEquals(TestSystemNoDependency.class, ecsSystems.get(0).getClass());
    assertEquals(TestSystemWithDependency.class, ecsSystems.get(1).getClass());
  }

  @Test
  void testWhenDependencyNotBound() {
    SolaEditorModule solaEditorModule = new SolaEditorModule();

    solaEditorModule.bind(new SolaEditorModuleBinding<>(
      TestSystemNoDependency.class.getName(),
      TestSystemNoDependency.class,
      List.of(TestMissingDependency.class),
      injector -> new TestSystemNoDependency()
    ));

    assertThrows(IllegalStateException.class, solaEditorModule::buildEcsSystems);
  }

  @Test
  void testWhenMissingDependencyDefinition() {
    SolaEditorModule solaEditorModule = new SolaEditorModule();

    solaEditorModule.bind(new SolaEditorModuleBinding<>(
      TestDependency.class.getName(),
      TestDependency.class,
      List.of(),
      injector -> new TestDependency()
    ));

    solaEditorModule.bind(new SolaEditorModuleBinding<>(
      TestSystemWithDependency.class.getName(),
      TestSystemWithDependency.class,
      List.of(),
      injector -> new TestSystemWithDependency(injector.inject(TestDependency.class))
    ));

    assertThrows(IllegalStateException.class, solaEditorModule::buildEcsSystems);
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
