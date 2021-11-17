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
//    ecsSystemContainer.add(new RectangleRendererSystem(platform.getRenderer()));

    World world = new World(50);

    world.createEntity()
      .addComponent(new TransformComponent())
      .addComponent(new RectangleRendererComponent(Color.WHITE));

    world.createEntity()
      .addComponent(new TransformComponent(50, 50, 10, 10, 0))
      .addComponent(new RectangleRendererComponent(Color.WHITE));

    world.createEntity()
      .addComponent(new TransformComponent(50, 50, 10, 10, 0.5f))
      .addComponent(new RectangleRendererComponent(Color.WHITE));

    ecsSystemContainer.setWorld(world);

    platform.getViewport().setAspectMode(AspectMode.STRETCH);

    solaGraphics = new SolaGraphics(ecsSystemContainer);
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGraphics.render(renderer);
  }
}
