package technology.sola.engine.input;

import org.jspecify.annotations.Nullable;
import technology.sola.math.linear.Vector2D;

/**
 * TouchGestureHelper provides helper methods for touch gestures.
 */
public class TouchGestureHelper {
  private static final int PINCH_THRESHOLD = 200;
  private final TouchInput touchInput;
  private Touch initialTouch1;
  private Touch initialTouch2;

  TouchGestureHelper(TouchInput touchInput) {
    this.touchInput = touchInput;
  }

  /**
   * @return true if a pinch in gesture has been detected
   */
  public boolean isPinchIn() {
    return isPinchGesture(true);
  }

  /**
   * @return true if a pinch out gesture has been detected
   */
  public boolean isPinchOut() {
    return isPinchGesture(false);
  }

  void update() {
    initialTouch1 = getInitialTouch(0, initialTouch1);
    initialTouch2 = getInitialTouch(1, initialTouch2);
  }

  @Nullable
  private Touch getInitialTouch(int id, @Nullable Touch currentValue) {
    var touch = touchInput.getTouch(id);

    if (touch != null) {
      if (touch.phase() == TouchPhase.ENDED) {
        return null;
      } else if (touch.phase() == TouchPhase.BEGAN) {
        return touch;
      }
    }

    return currentValue;
  }

  private boolean isPinchGesture(boolean isPinchIn) {
    Touch touch1 = touchInput.getTouch(0);
    Touch touch2 = touchInput.getTouch(1);

    if (initialTouch1 == null || initialTouch2 == null || touch1 == null || touch2 == null ) {
      return false;
    }

    if (touch1.phase() != TouchPhase.MOVED || touch2.phase() != TouchPhase.MOVED) {
      return false;
    }

    var currentDistance = new Vector2D(touch2.x(), touch2.y()).distanceSq(new Vector2D(touch1.x(), touch1.y()));
    var initialDistance = new Vector2D(initialTouch2.x(), initialTouch2.y()).distanceSq(new Vector2D(initialTouch1.x(), initialTouch1.y()));

    float distanceBig = isPinchIn ? initialDistance : currentDistance;
    float distanceSmall = isPinchIn ? currentDistance : initialDistance;

    if (distanceBig - distanceSmall > PINCH_THRESHOLD) {
      initialTouch1 = touch1;
      initialTouch2 = touch2;
      return true;
    }

    return false;
  }
}
