package technology.sola.engine.input;

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
    Touch touch1 = touchInput.getTouch(0);
    Touch touch2 = touchInput.getTouch(1);

    if (initialTouch1 == null || initialTouch2 == null || touch1 == null || touch2 == null) {
      return false;
    }

    if (touch1.phase() != TouchPhase.MOVED || touch2.phase() != TouchPhase.MOVED) {
      return false;
    }

    var currentDistance = new Vector2D(touch2.x(), touch2.y()).distanceSq(new Vector2D(touch1.x(), touch1.y()));
    var initialDistance = new Vector2D(initialTouch2.x(), initialTouch2.y()).distanceSq(new Vector2D(initialTouch1.x(), initialTouch1.y()));

    if (initialDistance - currentDistance > PINCH_THRESHOLD) {
      initialTouch1 = touch1;
      initialTouch2 = touch2;
      return true;
    }

    return false;
  }

  /**
   * @return true if a pinch out gesture has been detected
   */
  public boolean isPinchOut() {
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

    if (currentDistance - initialDistance > PINCH_THRESHOLD) {
      initialTouch1 = touch1;
      initialTouch2 = touch2;
      return true;
    }

    return false;
  }

  void update() {
    var temp = touchInput.getTouch(0);

    if (temp != null) {
      if (temp.phase() == TouchPhase.ENDED) {
        initialTouch1 = null;
      } else if (temp.phase() == TouchPhase.BEGAN) {
        initialTouch1 = temp;
      }
    }

    temp = touchInput.getTouch(1);

    if (temp != null) {
      if (temp.phase() == TouchPhase.ENDED) {
        initialTouch2 = null;
      } else if (temp.phase() == TouchPhase.BEGAN) {
        initialTouch2 = temp;
      }
    }
  }
}
