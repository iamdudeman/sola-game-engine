package technology.sola.engine.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class KeyboardInputTest {
  private KeyboardInput keyboardInput;

  @BeforeEach
  void setup() {
    keyboardInput = new KeyboardInput();
  }

  @Test
  void whenUnrecognizedKeyPressed_shouldThrowException() {
    assertThrows(KeyboardInputException.class, () -> keyboardInput.keyPressed(createMockKeyEvent(-1)));
  }

  @Test
  void whenUnrecognizedKeyReleased_shouldThrowException() {
    assertThrows(KeyboardInputException.class, () -> keyboardInput.keyReleased(createMockKeyEvent(-1)));
  }

  @Test
  void whenKeyPressed_withOneUpdate_shouldShowKeyAsPressedButNotHeld() {
    keyboardInput.keyPressed(createMockKeyEvent(Key.A));
    keyboardInput.updateStatusOfKeys();

    assertTrue(keyboardInput.isKeyPressed(Key.A));
    assertFalse(keyboardInput.isKeyHeld(Key.A));
  }

  @Test
  void whenKeyPressed_withTwoUpdates_shouldShowKeyAsHeldButNotPressed() {
    keyboardInput.keyPressed(createMockKeyEvent(Key.A));
    keyboardInput.updateStatusOfKeys();
    keyboardInput.updateStatusOfKeys();

    assertFalse(keyboardInput.isKeyPressed(Key.A.getCode()));
    assertTrue(keyboardInput.isKeyHeld(Key.A.getCode()));
  }

  @Test
  void whenKeyPressedThenReleased_shouldShowKeyNotHeldOrPressed() {
    keyboardInput.keyPressed(createMockKeyEvent(Key.B));
    keyboardInput.updateStatusOfKeys();
    keyboardInput.keyReleased(createMockKeyEvent(Key.B));
    keyboardInput.updateStatusOfKeys();

    assertFalse(keyboardInput.isKeyPressed(Key.B.getCode()));
    assertFalse(keyboardInput.isKeyHeld(Key.B.getCode()));
  }

  private KeyEvent createMockKeyEvent(Key key) {
    return createMockKeyEvent(key.getCode());
  }

  private KeyEvent createMockKeyEvent(int keyCode) {
    KeyEvent mockKeyEvent = Mockito.mock(KeyEvent.class);

    Mockito.when(mockKeyEvent.getKeyCode()).thenReturn(keyCode);

    return mockKeyEvent;
  }
}
