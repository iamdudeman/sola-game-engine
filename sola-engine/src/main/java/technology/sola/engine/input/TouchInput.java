package technology.sola.engine.input;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;

@NullMarked
public class TouchInput {
  public static final int MAX_TOUCHES = 10;
  private final @Nullable Touch[] touches = new Touch[MAX_TOUCHES];
  private final @Nullable Touch[] lastEventTouches = new Touch[MAX_TOUCHES];
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

  public void updatedStatusOfTouches() {
    System.arraycopy(lastEventTouches, 0, touches, 0, MAX_TOUCHES);
    Arrays.fill(lastEventTouches, null);

    touchCount = 0;

    for (int i = 0; i < MAX_TOUCHES; i++) {
      if (touches[i] != null) {
        touchCount++;
      }
    }
  }

  public void onTouchEvent(TouchEvent event) {
    lastEventTouches[event.touch().index()] = event.touch();
  }
}
