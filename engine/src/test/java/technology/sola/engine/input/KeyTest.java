package technology.sola.engine.input;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class KeyTest {
  @Test
  void ensureUniqueKey() {
    for (Key key : Key.values()) {
      for (Key otherKey : Key.values()) {
        if (key == otherKey) {
          continue;
        }

        if (key.getCode() == otherKey.getCode()) {
          fail(String.format("%s and %s cannot have the same code", key.name(), otherKey.name()));
        }

        if (key.getName().equals(otherKey.getName())) {
          fail(String.format("%s and %s cannot have the same key name", key.name(), otherKey.name()));
        }
      }
    }
  }
}
