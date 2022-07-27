package technology.sola.engine.graphics.components;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.BlendMode;

import java.io.Serial;

public class BlendModeComponent implements Component {
  @Serial
  private static final long serialVersionUID = 2038522969023666619L;
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
