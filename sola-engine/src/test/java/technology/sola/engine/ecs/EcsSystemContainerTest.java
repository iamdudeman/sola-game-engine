package technology.sola.engine.ecs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EcsSystemContainerTest {
  private EcsSystemContainer ecsService;

  @BeforeEach
  void setup() {
    ecsService = new EcsSystemContainer();
  }

  @Test
  void whenSettingWorld_shouldReturnWorld() {
    World world = new World(2);

    ecsService.setWorld(world);

    assertEquals(world, ecsService.getWorld());
  }

  @Test
  void whenGettingSystemsThatDoNoExist_shouldReturnNull() {
    assertNull(ecsService.get(TestUpdateEcsSystem.class));
  }

  @Test
  void whenGettingRegisteredSystems_shouldReturn() {
    TestUpdateEcsSystem testUpdateSystem = new TestUpdateEcsSystem();

    ecsService.add(testUpdateSystem);

    assertEquals(testUpdateSystem, ecsService.get(TestUpdateEcsSystem.class));
  }

  @Test
  void whenAddingSystem_shouldSortByOrderAndIgnoreInactive() {
    EcsSystem mockFirstUpdateSystem = Mockito.mock(EcsSystem.class);
    Mockito.when(mockFirstUpdateSystem.getOrder()).thenReturn(0);
    Mockito.when(mockFirstUpdateSystem.isActive()).thenReturn(true);

    EcsSystem mockSecondUpdateSystem = Mockito.mock(EcsSystem.class);
    Mockito.when(mockSecondUpdateSystem.isActive()).thenReturn(true);

    EcsSystem mockThirdUpdateSystem = Mockito.mock(EcsSystem.class);
    Mockito.when(mockThirdUpdateSystem.getOrder()).thenReturn(2);
    Mockito.when(mockThirdUpdateSystem.isActive()).thenReturn(false);

    ecsService.add(mockSecondUpdateSystem);
    ecsService.add(mockFirstUpdateSystem);
    ecsService.add(mockThirdUpdateSystem);

    World mockWorld = Mockito.mock(World.class);
    ecsService.setWorld(mockWorld);
    ecsService.update(0.16f);

    InOrder inOrder = Mockito.inOrder(mockFirstUpdateSystem, mockSecondUpdateSystem);
    inOrder.verify(mockFirstUpdateSystem).update(mockWorld, 0.16f);
    inOrder.verify(mockSecondUpdateSystem).update(mockWorld, 0.16f);
    Mockito.verify(mockThirdUpdateSystem, Mockito.never()).update(mockWorld, 0.16f);
  }

  static class TestUpdateEcsSystem extends EcsSystem {
    @Override
    public int getOrder() {
      return 0;
    }

    @Override
    public void update(World world, float deltaTime) {
    }
  }
}
