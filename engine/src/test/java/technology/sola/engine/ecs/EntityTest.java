package technology.sola.engine.ecs;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class EntityTest {
  @Mock
  private World mockWorld;

  @Nested
  class addComponent {
    @Test
    void whenCalled_shouldAddComponentClassToCurrentComponents() {
      Entity entity = new Entity(mockWorld, 0);

      entity.addComponent(new TestComponent());

      assertEquals(TestComponent.class, entity.getCurrentComponents().get(0));
    }

    @Test
    void whenCalled_shouldAddToWorld() {
      Entity entity = new Entity(mockWorld, 0);

      TestComponent testComponent = new TestComponent();
      entity.addComponent(testComponent);

      Mockito.verify(mockWorld, Mockito.times(1)).addComponentForEntity(0, testComponent);
    }
  }

  @Nested
  class getComponent {
    @Test
    void whenCalled_shouldGetFromWorld() {
      Entity entity = new Entity(mockWorld, 0);

      entity.getComponent(TestComponent.class);

      Mockito.verify(mockWorld, Mockito.times(1)).getComponentForEntity(0, TestComponent.class);
    }
  }

  @Nested
  class getOptionalComponent {
    @Test
    void whenCalled_shouldGetFromWorld() {
      Entity entity = new Entity(mockWorld, 0);

      Optional<TestComponent> result = entity.getOptionalComponent(TestComponent.class);

      Mockito.verify(mockWorld, Mockito.times(1)).getComponentForEntity(0, TestComponent.class);
      assertTrue(result.isEmpty());
    }
  }

  @Nested
  class removeComponent {
    @Test
    void whenCalled_shouldRemoveComponentClassFromCurrentComponents() {
      Entity entity = new Entity(mockWorld, 0);

      entity.getCurrentComponents().add(TestComponent.class);
      assertEquals(1, entity.getCurrentComponents().size());

      entity.removeComponent(TestComponent.class);
      assertEquals(0, entity.getCurrentComponents().size());
    }

    @Test
    void whenCalled_shouldAddToWorld() {
      Entity entity = new Entity(mockWorld, 0);

      entity.removeComponent(TestComponent.class);

      Mockito.verify(mockWorld, Mockito.times(1)).removeComponent(0, TestComponent.class);
    }
  }

  @Nested
  class destroy {
    @Test
    void whenCalled_shouldDestroyFromWorld() {
      Entity entity = new Entity(mockWorld, 0);

      entity.destroy();

      Mockito.verify(mockWorld, Mockito.times(1)).queueEntityForDestruction(entity);
    }
  }

  private static class TestComponent implements Component<TestComponent> {
    private static final long serialVersionUID = 24775932711767895L;

    @Override
    public TestComponent copy() {
      return new TestComponent();
    }
  }
}
