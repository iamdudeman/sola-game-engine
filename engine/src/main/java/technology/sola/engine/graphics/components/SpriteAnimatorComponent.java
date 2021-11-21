package technology.sola.engine.graphics.components;

import technology.sola.engine.ecs.Component;

import java.util.HashMap;
import java.util.Map;

public class SpriteAnimatorComponent implements Component {
  private final Map<String, SpriteKeyFrame[]> animationMap = new HashMap<>();
  private long lastFrameChange = 0;
  private int keyFrameIndex = 0;
  private String activeAnimationId = null;
  private SpriteKeyFrame activeFrame;
  private SpriteKeyFrame[] activeAnimationKeyFrames;

  public SpriteAnimatorComponent(String activeAnimationId, SpriteKeyFrame ... activeAnimationKeyFrames) {
    addAnimation(activeAnimationId, activeAnimationKeyFrames);
    playAnimation(activeAnimationId);
  }

  public SpriteAnimatorComponent addAnimation(String id, SpriteKeyFrame ...spriteKeyFrames) {
    animationMap.put(id, spriteKeyFrames);
    return this;
  }

  public void playAnimation(String id) {
    activeAnimationId = id;
    activeAnimationKeyFrames = animationMap.get(id);
    keyFrameIndex = 0;
    activeFrame = activeAnimationKeyFrames[0];
    lastFrameChange = System.currentTimeMillis();
  }

  public SpriteKeyFrame getCurrentFrame() {
    long now = System.currentTimeMillis();

    if (now - lastFrameChange > activeFrame.getDuration()) {
      keyFrameIndex = (keyFrameIndex + 1) % activeAnimationKeyFrames.length;
      activeFrame = activeAnimationKeyFrames[keyFrameIndex];
      lastFrameChange = now;
    }

    return activeFrame;
  }

  public String getActiveAnimationId() {
    return activeAnimationId;
  }
}
