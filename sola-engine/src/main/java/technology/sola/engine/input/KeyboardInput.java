package technology.sola.engine.input;

import org.jspecify.annotations.NullMarked;

import java.util.Arrays;

/**
 * KeyboardInput contains information about user interaction with the keyboard.
 */
@NullMarked
public class KeyboardInput {
  static final int KEY_COUNT = 256;

  private final boolean[] keysDown = new boolean[KEY_COUNT];
  private final KeyState[] keyStates = new KeyState[KEY_COUNT];

  /**
   * Creates a KeyboardInput instance initializing the state of all keys to be released.
   */
  public KeyboardInput() {
    Arrays.fill(keyStates, KeyState.RELEASED);
  }

  /**
   * Checks if a key is pressed based on its keycode.
   *
   * @param keyCode the code of the key to check
   * @return true if key is pressed
   */
  public boolean isKeyPressed(int keyCode) {
    return KeyState.PRESSED.equals(keyStates[keyCode]);
  }

  /**
   * Checks if a {@link Key} is pressed.
   *
   * @param key the key to check
   * @return true if key is pressed
   */
  public boolean isKeyPressed(Key key) {
    return isKeyPressed(key.getCode());
  }

  /**
   * Checks if a key is held based on its keycode.
   *
   * @param keyCode the code of the key to check
   * @return true if key is held
   */
  public boolean isKeyHeld(int keyCode) {
    return KeyState.HELD.equals(keyStates[keyCode]);
  }

  /**
   * Checks if a {@link Key} is held.
   *
   * @param key the key to check
   * @return true if key is held
   */
  public boolean isKeyHeld(Key key) {
    return isKeyHeld(key.getCode());
  }

  /**
   * Called once per frame to update the current status of the keyboard based on the user's interaction.
   */
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

  /**
   * Called on press of a key.
   *
   * @param keyEvent the {@link KeyEvent}
   */
  public void onKeyPressed(KeyEvent keyEvent) {
    int keyCode = keyEvent.keyCode();

    if (keyCode >= 0 && keyCode < KEY_COUNT) {
      keysDown[keyCode] = true;
    } else {
      throw new KeyboardInputException(keyCode);
    }
  }

  /**
   * Called on release of a key.
   *
   * @param keyEvent the {@link KeyEvent}
   */
  public void keyReleased(KeyEvent keyEvent) {
    int keyCode = keyEvent.keyCode();

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
