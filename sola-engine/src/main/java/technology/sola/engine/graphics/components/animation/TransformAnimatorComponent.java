package technology.sola.engine.graphics.components.animation;

import technology.sola.ecs.Component;

import java.io.Serial;

public class TransformAnimatorComponent implements Component {
  @Serial
  private static final long serialVersionUID = 1002749182938133802L;

  private EasingFunction easingFunction;
  private long duration;



  @FunctionalInterface
  public interface AnimationCompleteCallback {
    void onComplete();
  }

  @FunctionalInterface
  public interface EasingFunction {
    EasingFunction Linear = ((startingValue, endingValue, percent) -> {
      return startingValue + (endingValue - startingValue) * percent;
    });

    /**
     * Eases a value based.
     *
     * @param startingValue the value to where easing began from
     * @param endingValue   the value to where easing will finish
     * @param percent       the current percent to ease to (0 - 1)
     * @return the value after ease has been applied
     */
    float ease(float startingValue, float endingValue, float percent);
  }
}
