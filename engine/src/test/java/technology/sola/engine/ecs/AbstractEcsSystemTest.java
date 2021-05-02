package technology.sola.engine.ecs;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractEcsSystemTest {
  @Test
  void whenCreated_shouldBeActive() {
    assertTrue(new TestUpdateEcsSystem().isActive());
  }

  @Nested
  class setActive {
    @Test
    void whenCalled_withFalse_shouldBeFalse() {
      AbstractEcsSystem updateSystem = new TestUpdateEcsSystem();

      updateSystem.setActive(false);

      assertFalse(updateSystem.isActive());
    }
  }

  private static class TestUpdateEcsSystem extends AbstractEcsSystem {
    @Override
    public void update(World world, float deltaTime) {
    }

    @Override
    public int getOrder() {
      return 0;
    }
  }
}
