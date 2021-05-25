package technology.sola.engine.input;

import java.util.Arrays;

public class KeyboardInput {
  static final int KEY_COUNT = 256;

  private final boolean[] keysDown = new boolean[KEY_COUNT];
  private final KeyState[] keyStates = new KeyState[KEY_COUNT];

  public KeyboardInput() {
    Arrays.fill(keyStates, KeyState.RELEASED);
  }

  public boolean isKeyPressed(int keyCode) {
    return KeyState.PRESSED.equals(keyStates[keyCode]);
  }

  public boolean isKeyPressed(Key key) {
    return isKeyPressed(key.getCode());
  }

  public boolean isKeyHeld(int keyCode) {
    return KeyState.HELD.equals(keyStates[keyCode]);
  }

  public boolean isKeyHeld(Key key) {
    return isKeyHeld(key.getCode());
  }

  public void updateStatusOfKeys() {
    for (int i = 0; i < KEY_COUNT; i++) {
      if (keysDown[i]) {
        if (keyStates[i] == KeyState.RELEASED) {
          keyStates[i] = KeyState.PRESSED;
        } else if (keyStates[i] == KeyState.PRESSED) {
          keyStates[i] = KeyState.HELD;
        }
      } else {
        keyStates[i] = KeyState.RELEASED;
      }
    }
  }

  public void keyPressed(KeyEvent keyEvent) {
    int keyCode = keyEvent.getKeyCode();

    if (keyCode >= 0 && keyCode < KEY_COUNT) {
      keysDown[keyCode] = true;
    } else {
      throw new KeyboardInputException(keyCode);
    }
  }

  public void keyReleased(KeyEvent keyEvent) {
    int keyCode = keyEvent.getKeyCode();

    if (keyCode >= 0 && keyCode < KEY_COUNT) {
      keysDown[keyCode] = false;
    } else {
      throw new KeyboardInputException(keyCode);
    }
  }

  private enum KeyState {
    HELD,
    PRESSED,
    RELEASED
  }
}
