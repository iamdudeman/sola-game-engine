package technology.sola.engine.input;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

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

  /**
   * Gets the number of active touches (fingers on the screen).
   *
   * @return the active touch count
   */
  public int getActiveTouchCount() {
    int touchCount = 0;

    for (int i = 0; i < MAX_TOUCHES; i++) {
      if (touches[i] != null) {
        touchCount++;
      }
    }

    return touchCount;
  }

  /**
   * @return {@link Iterator} for all the active touches
   */
  public Iterator<Touch> activeTouchesIterator() {
    return activeTouches().iterator();
  }

  /**
   * @return {@link Stream} of all active touches
   */
  public Stream<Touch> activeTouches() {
    return Arrays.stream(touches).filter(Objects::nonNull);
  }

  /**
   * @return the {@link Touch} of the first finger or else null
   */
  @Nullable
  public Touch getFirstTouch() {
    return touches[0];
  }

  /**
   * @return the {@link Touch} of the first finger that is still on the screen or else null
   */
  @Nullable
  public Touch getFirstActiveTouch() {
    for (var touch : touches) {
      if (touch != null) {
        return touch;
      }
    }

    return null;
  }

  /**
   * @return the {@link Touch} of the last finger that is still on the screen or else null
   */
  @Nullable
  public Touch getLastActiveTouch() {
    for (int i = MAX_TOUCHES - 1; i >= 0; i--) {
      if (touches[i] != null) {
        return touches[i];
      }
    }

    return null;
  }

  /**
   * Gets the {@link Touch} of the given finger by unique id.
   *
   * @param id the id (typically 0-9)
   * @return the {@link Touch} of the given finger or else null
   */
  @Nullable
  public Touch getTouch(int id) {
    if (id >= MAX_TOUCHES) {
      throw new IllegalArgumentException("Touch index must be 0 or less than " + MAX_TOUCHES);
    }

    return touches[id];
  }

  /**
   * Updates the status of changed {@link Touch}es from the previous frame.
   */
  public void updatedStatusOfTouches() {
    System.arraycopy(lastEventTouches, 0, touches, 0, MAX_TOUCHES);
    Arrays.fill(lastEventTouches, null);
  }

  /**
   * Called by the {@link technology.sola.engine.core.SolaPlatform} whenever a touch event occurs.
   *
   * @param event the {@link TouchEvent}
   */
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
