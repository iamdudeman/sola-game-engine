package technology.sola.engine.physics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MaterialTest {
  @Test
  void whenMassLessThanZero_shouldThrowException() {
    assertThrows(IllegalArgumentException.class, () -> new Material(-1));
  }

  @Test
  void whenRestitutionLessThanZero_shouldThrowException() {
    assertThrows(IllegalArgumentException.class, () -> new Material(1, -1));
  }

  @Test
  void whenFrictionLessThanZero_shouldThrowException() {
    assertThrows(IllegalArgumentException.class, () -> new Material(1, 0, -1));
  }

  @Test
  void whenMassZero_shouldSetInverseMassToZero() {
    Material material = new Material(0);

    assertEquals(0, material.getInverseMass());
  }

  @Test
  void whenMassSet_shouldCalculateInverseMass() {
    Material material = new Material(2);

    assertEquals(0.5f, material.getInverseMass());
  }

  @Test
  void whenMaterialsHaveSameProperties_shouldBeEqual() {
    Material material = new Material(2, 0.5f, 0.2f);
    Material material2 = new Material(2, 0.5f, 0.2f);

    assertEquals(material, material2);
    assertEquals(material.hashCode(), material2.hashCode());
  }

  @Test
  void whenMaterialsHaveDifferentProperties_shouldNotBeEqual() {
    Material material = new Material(2, 0.5f);
    Material material2 = new Material(1, 0.5f);

    assertNotEquals(material, material2);
    assertNotEquals(material.hashCode(), material2.hashCode());
  }
}
