package technology.sola.engine.graphics.components;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.RenderMode;

import java.io.Serial;

public class RenderModeComponent implements Component {
  @Serial
  private static final long serialVersionUID = 2038522969023666619L;
  private RenderMode renderMode;

  public RenderModeComponent() {
  }

  public RenderModeComponent(RenderMode renderMode) {
    this.renderMode = renderMode;
  }

  public RenderMode getRenderMode() {
    return renderMode;
  }

  public void setRenderMode(RenderMode renderMode) {
    this.renderMode = renderMode;
  }
}
