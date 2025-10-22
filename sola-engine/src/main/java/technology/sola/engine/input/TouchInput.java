package technology.sola.engine.input;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class TouchInput {
  public static final int MAX_TOUCHES = 10;
  private final @Nullable Touch[] touches = new Touch[MAX_TOUCHES];
  private int touchCount = 0;

  public int getTouchCount() {
    return touchCount;
  }

  @Nullable
  public Touch getTouch(int index) {
    if (index >= MAX_TOUCHES) {
      throw new IllegalArgumentException("Touch index must be 0 or less than " + MAX_TOUCHES);
    }

    return touches[index];
  }

  public void updateTouchState() {
    // todo method to update touches

    touchCount = 0;

    for (int i = 0; i < MAX_TOUCHES; i++) {
      if (touches[i] != null) {
        touchCount++;
      }
    }
  }
}
