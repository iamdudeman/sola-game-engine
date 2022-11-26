package technology.sola.engine.graphics.components.animation;

import technology.sola.ecs.Component;
import technology.sola.engine.core.component.TransformComponent;

import java.io.Serial;

public class TransformAnimatorComponent implements Component {
  @Serial
  private static final long serialVersionUID = 1002749182938133802L;

  private EasingFunction easingFunction;
  private long duration;

  private float startingTranslateX;
  private float startingTranslateY;
  private float startingScaleX;
  private float startingScaleY;
  private boolean isInit = false;

  public TransformAnimatorComponent(EasingFunction easingFunction, long duration) {
    // todo need ability to set destination (ie. x,y 50,50)
  }

  private void initialize(TransformComponent transformComponent) {
    startingTranslateX = transformComponent.getX();
    startingTranslateY = transformComponent.getY();
    startingScaleX = transformComponent.getScaleX();
    startingScaleX = transformComponent.getScaleY();
  }

  public void tickAnimation(TransformComponent transformComponent, float deltaTime) {
    if (!isInit) {
      initialize(transformComponent);
      isInit = true;
    }

    // todo update transform stuff
  }

  @FunctionalInterface
  public interface AnimationCompleteCallback {
    void onComplete();
  }

  // todo need easing function to set this
  //  linear - (x)
  //  ease-in - (x^2)
  //  ease-out - (1 - (x - 1)^2)
  //  smooth - (x^2)(3 - 2x)

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
