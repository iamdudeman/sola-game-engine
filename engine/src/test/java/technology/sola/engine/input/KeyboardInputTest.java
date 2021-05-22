package technology.sola.engine.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class KeyboardInputTest {
  private KeyboardInput solKanaKeyboardInputService;

  @BeforeEach
  void setup() {
    solKanaKeyboardInputService = new KeyboardInput();
  }

  @Test
  void whenUnrecognizedKeyPressed_shouldThrowException() {
    assertThrows(KeyboardInputException.class, () -> solKanaKeyboardInputService.keyPressed(createMockKeyEvent(-1)));
  }

  @Test
  void whenUnrecognizedKeyReleased_shouldThrowException() {
    assertThrows(KeyboardInputException.class, () -> solKanaKeyboardInputService.keyReleased(createMockKeyEvent(-1)));
  }

  @Test
  void whenKeyPressed_withOneUpdate_shouldShowKeyAsPressedButNotHeld() {
    solKanaKeyboardInputService.keyPressed(createMockKeyEvent(Key.A));
    solKanaKeyboardInputService.updateStatusOfKeys();

    assertTrue(solKanaKeyboardInputService.isKeyPressed(Key.A.getCode()));
    assertFalse(solKanaKeyboardInputService.isKeyHeld(Key.A.getCode()));
  }

  @Test
  void whenKeyPressed_withTwoUpdates_shouldShowKeyAsHeldButNotPressed() {
    solKanaKeyboardInputService.keyPressed(createMockKeyEvent(Key.A));
    solKanaKeyboardInputService.updateStatusOfKeys();
    solKanaKeyboardInputService.updateStatusOfKeys();

    assertFalse(solKanaKeyboardInputService.isKeyPressed(Key.A.getCode()));
    assertTrue(solKanaKeyboardInputService.isKeyHeld(Key.A.getCode()));
  }

  @Test
  void whenKeyPressedThenReleased_shouldShowKeyNotHeldOrPressed() {
    solKanaKeyboardInputService.keyPressed(createMockKeyEvent(Key.A));
    solKanaKeyboardInputService.updateStatusOfKeys();
    solKanaKeyboardInputService.keyReleased(createMockKeyEvent(Key.A));
    solKanaKeyboardInputService.updateStatusOfKeys();

    assertFalse(solKanaKeyboardInputService.isKeyPressed(Key.A.getCode()));
    assertFalse(solKanaKeyboardInputService.isKeyHeld(Key.A.getCode()));
  }

  private KeyEvent createMockKeyEvent(Key key) {
    return createMockKeyEvent(key.getCode());
  }

  private KeyEvent createMockKeyEvent(int keyCode) {
    Key mockKey = Mockito.mock(Key.class);
    KeyEvent mockKeyEvent = Mockito.mock(KeyEvent.class);

    Mockito.when(mockKey.getCode()).thenReturn(keyCode);
    Mockito.when(mockKeyEvent.getKey()).thenReturn(mockKey);

    return mockKeyEvent;
  }
}
