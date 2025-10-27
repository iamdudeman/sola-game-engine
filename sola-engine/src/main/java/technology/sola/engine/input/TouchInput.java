package technology.sola.engine.input;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * TouchInput contains information about user interaction with the touch screen.
 */
@NullMarked
public class TouchInput {
  /**
   * Max number of simultaneous touches supported.
   */
  public static final int MAX_TOUCHES = 10;
  private final @Nullable Touch[] touches = new Touch[MAX_TOUCHES];
  private final @Nullable Touch[] lastEventTouches = new Touch[MAX_TOUCHES];

  public int getTouchCount() {
    int touchCount = 0;

    for (int i = 0; i < MAX_TOUCHES; i++) {
      if (touches[i] != null) {
        touchCount++;
      }
    }

    return touchCount;
  }

  public Iterator<Touch> activeTouchesIterator() {
    return Arrays.stream(touches)
      .filter(Objects::nonNull)
      .iterator();
  }

  @Nullable
  public Touch getFirstTouch() {
    return touches[0];
  }

  @Nullable
  public Touch getFirstActiveTouch() {
    for (var touch : touches) {
      if (touch != null) {
        return touch;
      }
    }

    return null;
  }

  @Nullable
  public Touch getTouch(int id) {
    if (id >= MAX_TOUCHES) {
      throw new IllegalArgumentException("Touch index must be 0 or less than " + MAX_TOUCHES);
    }

    return touches[id];
  }

  public void updatedStatusOfTouches() {
    System.arraycopy(lastEventTouches, 0, touches, 0, MAX_TOUCHES);
    Arrays.fill(lastEventTouches, null);
  }

  public void onTouchEvent(TouchEvent event) {
    var touch = event.touch();
    var touchId = touch.id();
    var current = lastEventTouches[touchId];

    // we don't want to lose ENDED or BEGAN events that happen same frame as MOVED
    if (current == null || current.phase() == TouchPhase.MOVED) {
      lastEventTouches[touchId] = touch;
    } else {
      lastEventTouches[touchId] = new Touch(
        touch.x(), touch.y(),
        current.phase(), current.id()
      );
    }
  }
}
