package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.World;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.graphics.screen.AspectMode;

public class TempExample extends AbstractSola {
  private SolaGraphics solaGraphics;

  @Override
  protected SolaConfiguration buildConfiguration() {
    return new SolaConfiguration("Temp", 600, 400, 30, true);
  }

  @Override
  protected void onInit() {
    World world = new World(50);

    world.createEntity()
      .addComponent(new TransformComponent())
      .addComponent(new RectangleRendererComponent(Color.GREEN, false));

    for (int i = 0; i < 40; i++) {
      world.createEntity()
        .addComponent(new TransformComponent(50 + i, 50 + i, 1.5f, 1, 0))
        .addComponent(new RectangleRendererComponent(Color.WHITE));
    }

    world.createEntity()
      .addComponent(new TransformComponent(20, 100, 5, 8, 0.5f))
      .addComponent(new RectangleRendererComponent(Color.BLUE));

    ecsSystemContainer.setWorld(world);

    platform.getViewport().setAspectMode(AspectMode.STRETCH);

    solaGraphics = new SolaGraphics(ecsSystemContainer, platform.getRenderer());
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGraphics.render();
  }
}
