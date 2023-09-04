package technology.sola.engine.graphics.components;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.blend.BlendFunction;

/**
 * BlendModeComponent is a {@link Component} that modifies the {@link BlendFunction} used when rendering this
 * {@link technology.sola.ecs.Entity}
 */
public class BlendModeComponent implements Component {
  private BlendFunction blendFunction;

  /**
   * Creates a new instance initialized to a {@link BlendFunction}.
   *
   * @param blendFunction the initial {@code BlendFunction}
   */
  public BlendModeComponent(BlendFunction blendFunction) {
    this.blendFunction = blendFunction;
  }

  /**
   * @return the current {@link BlendMode}
   */
  public BlendFunction getBlendFunction() {
    return blendFunction;
  }

  /**
   * Sets the {@link BlendFunction} to be used.
   *
   * @param blendFunction the new {@code BlendFunction}
   */
  public void setBlendFunction(BlendFunction blendFunction) {
    this.blendFunction = blendFunction;
  }
}
