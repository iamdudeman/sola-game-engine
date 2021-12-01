package technology.sola.engine.editor.core;

import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.graphics.SolaGraphics;
import technology.sola.engine.ecs.World;
import technology.sola.engine.graphics.Renderer;

public class EditorSola extends Sola {
  private SolaGraphics solaGraphics;

  @Override
  protected SolaConfiguration buildConfiguration() {
    // TODO get this from project settings instead
    return new SolaConfiguration("Preview", 800, 600, 10, true);
  }

  @Override
  protected void onInit() {
    // TODO get assets based on project structure (might need this for all Sola [think VirtualFileSystem of sorts maybe])

    solaGraphics = SolaGraphics.use(ecsSystemContainer, platform.getRenderer(), assetPoolProvider);
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGraphics.render();
  }

  public void setWorld(World world) {
    ecsSystemContainer.setWorld(world);
  }
}
