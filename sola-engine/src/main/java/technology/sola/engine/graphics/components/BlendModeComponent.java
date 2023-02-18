package technology.sola.engine.graphics.components;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.renderer.BlendMode;

public class BlendModeComponent implements Component {
  private BlendMode blendMode;

  public BlendModeComponent(BlendMode blendMode) {
    this.blendMode = blendMode;
  }

  public BlendMode getRenderMode() {
    return blendMode;
  }

  public void setRenderMode(BlendMode blendMode) {
    this.blendMode = blendMode;
  }
}
