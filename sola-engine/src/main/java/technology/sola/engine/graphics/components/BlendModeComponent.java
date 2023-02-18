package technology.sola.engine.graphics.components;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.renderer.BlendMode;

/**
 * BlendModeComponent is a {@link Component} that modifies the {@link BlendMode} used when rendering this
 * {@link technology.sola.ecs.Entity}
 */
public class BlendModeComponent implements Component {
  private BlendMode blendMode;

  /**
   * Creates a new instance initialized to a {@link BlendMode}.
   *
   * @param blendMode the initial {@code BlendMode}
   */
  public BlendModeComponent(BlendMode blendMode) {
    this.blendMode = blendMode;
  }

  /**
   * @return the current {@link BlendMode}
   */
  public BlendMode getBlendMode() {
    return blendMode;
  }

  /**
   * Sets the {@link BlendMode} to be used.
   *
   * @param blendMode the new {@code BlendMode}
   */
  public void setBlendMode(BlendMode blendMode) {
    this.blendMode = blendMode;
  }
}
