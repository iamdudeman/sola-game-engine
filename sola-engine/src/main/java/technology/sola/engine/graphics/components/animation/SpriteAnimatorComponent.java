package technology.sola.engine.graphics.components.animation;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.components.SpriteKeyFrame;

import java.util.HashMap;
import java.util.Map;

/**
 * SpritesAnimatorComponent is a {@link Component} that contains data for controlling how a
 * {@link technology.sola.engine.graphics.components.SpriteComponent} animates between frames. Multiple different
 * animations can be added and played as needed.
 */
public class SpriteAnimatorComponent implements Component {
  /**
   * Constant for telling the {@link SpriteAnimatorComponent} to continuously loop the animation.
   */
  public static final int CONTINUOUS = -1;

  private final Map<String, SpriteKeyFrame[]> animationMap = new HashMap<>();
  private final Map<String, Integer> animationLoopsMap = new HashMap<>();
  private int keyFrameIndex = 0;
  private String activeAnimationId = null;
  private int activeAnimationLoops;
  private SpriteKeyFrame activeFrame;
  private SpriteKeyFrame[] activeAnimationKeyFrames;
  private boolean animationCompleteCalled = false;
  private AnimationCompleteCallback animationCompleteCallback;
  private float elapsedAnimationTime = 0;
  private float elapsedLoops = 0;

  /**
   * Creates a SpriteAnimatorComponent with animation id and an array of key frames. The animation will
   * loop {@link SpriteAnimatorComponent#CONTINUOUS}.
   *
   * @param activeAnimationId        the animation id that starts as active
   * @param activeAnimationKeyFrames the {@link SpriteKeyFrame} array
   */
  public SpriteAnimatorComponent(String activeAnimationId, SpriteKeyFrame... activeAnimationKeyFrames) {
    this(activeAnimationId, CONTINUOUS, activeAnimationKeyFrames);
  }

  /**
   * Creates a SpriteAnimatorComponent with animation id and an array of key frames. The animation will
   * loop the desired amount of times.
   *
   * @param activeAnimationId        the animation id that starts as active
   * @param loops                    the number of loops
   * @param activeAnimationKeyFrames the {@link SpriteKeyFrame} array
   */
  public SpriteAnimatorComponent(String activeAnimationId, int loops, SpriteKeyFrame... activeAnimationKeyFrames) {
    addAnimation(activeAnimationId, loops, activeAnimationKeyFrames);
    playAnimation(activeAnimationId);
  }

  /**
   * Adds a new animation with animation id and an array of key frames. The animation will
   * loop {@link SpriteAnimatorComponent#CONTINUOUS}.
   *
   * @param id              the animation id
   * @param spriteKeyFrames the {@link SpriteKeyFrame} array
   * @return this
   */
  public SpriteAnimatorComponent addAnimation(String id, SpriteKeyFrame... spriteKeyFrames) {
    return addAnimation(id, CONTINUOUS, spriteKeyFrames);
  }

  /**
   * Adds a new animation with animation id and an array of key frames. The animation will
   * loop the desired amount of times.
   *
   * @param id              the animation id
   * @param loops           the number of loops
   * @param spriteKeyFrames the {@link SpriteKeyFrame} array
   * @return this
   */
  public SpriteAnimatorComponent addAnimation(String id, int loops, SpriteKeyFrame... spriteKeyFrames) {
    animationMap.put(id, spriteKeyFrames);
    animationLoopsMap.put(id, loops);
    return this;
  }

  /**
   * Sets the {@link AnimationCompleteCallback} that happens when an animation finishes playing.
   *
   * @param animationCompleteCallback the animation complete callback
   * @return this
   */
  public SpriteAnimatorComponent setAnimationCompleteCallback(AnimationCompleteCallback animationCompleteCallback) {
    this.animationCompleteCallback = animationCompleteCallback;

    return this;
  }

  /**
   * Ticks the animation state by a delta time.
   *
   * @param deltaTime the time elapsed since previous update
   */
  public void tickAnimation(float deltaTime) {
    long duration = activeFrame.getDuration();

    if (duration == SpriteKeyFrame.DURATION_FREEZE) {
      return;
    }

    elapsedAnimationTime += deltaTime * 1000;

    if (elapsedAnimationTime >= duration) {
      if (keyFrameIndex + 1 >= activeAnimationKeyFrames.length) {
        if (activeAnimationLoops != CONTINUOUS) {
          elapsedLoops++;
        }

        keyFrameIndex = 0;
      } else {
        keyFrameIndex++;
      }

      if (activeAnimationLoops == CONTINUOUS || elapsedLoops < activeAnimationLoops) {
        activeFrame = activeAnimationKeyFrames[keyFrameIndex];
      } else if (animationCompleteCallback != null && !animationCompleteCalled) {
        animationCompleteCallback.onComplete(activeAnimationId);
        animationCompleteCalled = true;
      }

      elapsedAnimationTime = 0;
    }
  }

  /**
   * Starts playing the animation with desired id.
   *
   * @param id the id of the animation to play
   */
  public void playAnimation(String id) {
    activeAnimationId = id;
    activeAnimationKeyFrames = animationMap.get(id);
    activeAnimationLoops = animationLoopsMap.get(id);
    restart();
  }

  /**
   * Restarts the current animation.
   */
  public void restart() {
    keyFrameIndex = 0;
    elapsedLoops = 0;
    activeFrame = activeAnimationKeyFrames[0];
    elapsedAnimationTime = 0;
    animationCompleteCalled = false;
  }

  /**
   * @return the currently active {@link SpriteKeyFrame}
   */
  public SpriteKeyFrame getCurrentFrame() {
    return activeFrame;
  }

  /**
   * @return the currently active animation id
   */
  public String getActiveAnimationId() {
    return activeAnimationId;
  }

  /**
   * AnimationCompleteCallback is a {@link FunctionalInterface} for defining functionality that happens when an
   * animation completes.
   */
  @FunctionalInterface
  public interface AnimationCompleteCallback {
    /**
     * Method called when the animation with id is completed.
     *
     * @param animationId the id of the animation that just completed
     */
    void onComplete(String animationId);
  }
}
