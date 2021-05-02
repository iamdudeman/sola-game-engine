package technology.sola.engine.ecs;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractSystemTest {
  @Test
  void whenCreated_shouldBeActive() {
    assertTrue(new TestUpdateSystem().isActive());
  }

  @Nested
  class setActive {
    @Test
    void whenCalled_withFalse_shouldBeFalse() {
      AbstractSystem updateSystem = new TestUpdateSystem();

      updateSystem.setActive(false);

      assertFalse(updateSystem.isActive());
    }
  }

  private static class TestUpdateSystem extends AbstractSystem {
    @Override
    public void update(World world, float deltaTime) {
    }

    @Override
    public int getOrder() {
      return 0;
    }
  }
}
