package technology.sola.engine.examples.common.features;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.examples.common.ExampleUtils;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.TouchPhase;
import technology.sola.math.linear.Vector2D;

/**
 * TouchInputExample is a {@link Sola} for demoing touch interactions.
 *
 * <ul>
 *   <li>{@link technology.sola.engine.input.TouchInput}</li>
 * </ul>
 */
@NullMarked
public class TouchInputExample extends Sola {
  private SolaGraphics solaGraphics;

  /**
   * Creates an instance of this {@link Sola}.
   */
  public TouchInputExample() {
    super(new SolaConfiguration("Touch Input", 800, 600, 30));
  }

  @Override
  protected void onInit() {
    solaGraphics = new SolaGraphics.Builder(platform(), solaEcs)
      .withBackgroundColor(Color.DARK_GRAY)
      .withGui(mouseInput)
      .buildAndInitialize(assetLoaderProvider);

    platform().getViewport().setAspectMode(AspectMode.MAINTAIN);

    solaGraphics.guiDocument().setRootElement(
      ExampleUtils.createReturnToLauncherButton(platform(), eventHub, "0", "0")
    );

    solaEcs.addSystem(new TouchCreateEntitySystem());
    solaEcs.setWorld(new World(10));
  }

  @Override
  protected void onRender(Renderer renderer) {
    solaGraphics.render(renderer);
  }

  private final class TouchCreateEntitySystem extends EcsSystem {
    private static final int SIZE = 150;
    private static final int HALF_SIZE = SIZE / 2;

    @Override
    public void update(World world, float deltaTime) {
      touchInput.activeTouchesIterator().forEachRemaining(touch -> {
        var existingEntity = world.findEntityByName("touch" + touch.id());

        if (existingEntity == null) {
          if (touch.phase() == TouchPhase.BEGAN) {
            Vector2D worldPosition = solaGraphics.screenToWorldCoordinate(new Vector2D(touch.x(), touch.y()));

            world.createEntity()
              .setName("touch" + touch.id())
              .addComponent(new TransformComponent(worldPosition.x() - HALF_SIZE, worldPosition.y() - HALF_SIZE, SIZE))
              .addComponent(new CircleRendererComponent(Color.GREEN, true));

          }
        } else {
          if (touch.phase() == TouchPhase.MOVED) {
            Vector2D worldPosition = solaGraphics.screenToWorldCoordinate(new Vector2D(touch.x(), touch.y()));

            existingEntity.addComponent(new TransformComponent(worldPosition.x() - HALF_SIZE, worldPosition.y() - HALF_SIZE, SIZE));
          } else if (touch.phase() == TouchPhase.ENDED) {
            existingEntity.destroy();
          }
        }
      });
    }
  }
}
