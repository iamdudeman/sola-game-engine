package technology.sola.engine.examples.common.games;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.TextStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.MouseButton;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.system.PhysicsSystem;
import technology.sola.engine.utils.SolaRandom;
import technology.sola.math.geometry.Circle;
import technology.sola.math.linear.Vector2D;

/**
 * CirclePop is a {@link technology.sola.engine.core.Sola} little game that also does a rendering stress test for the
 * sola game engine.
 */
@NullMarked
public class CirclePopGame extends SolaWithDefaults {
  private final TextGuiElement scoreGuiElement = new TextGuiElement().setText("0");
  private int score = 0;

  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public CirclePopGame() {
    super(new SolaConfiguration("Circle Pop", 800, 600, 30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    ExampleLauncherSola.addReturnToLauncherKeyEvent(platform(), eventHub);

    defaultsConfigurator.useGraphics().useGui();

    solaEcs.addSystems(new PhysicsSystem(), new PlayerInputSystem());
    solaEcs.setWorld(buildWorld());

    scoreGuiElement.addStyle(ConditionalStyle.always(
      new TextStyles.Builder<>()
        .setBackgroundColor(Color.WHITE)
        .setPadding(5)
        .setTextColor(Color.BLACK)
        .build()
    ));

    guiDocument().setRootElement(scoreGuiElement);

    platform().getViewport().setAspectMode(AspectMode.STRETCH);
  }

  private class PlayerInputSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      if (mouseInput.isMousePressed(MouseButton.PRIMARY)) {
        var point = solaGraphics().screenToWorldCoordinate(mouseInput.getMousePosition());

        for (var entry : world.createView().of(TransformComponent.class).getEntries()) {
          var radius = entry.c1().getScaleX() * 0.5f;
          Circle circle = new Circle(
            radius,
            entry.c1().getTranslate().add(new Vector2D(radius, radius))
          );

          if (circle.contains(point)) {
            score++;
            entry.entity().destroy();
          }
        }

        scoreGuiElement.setText("" + score);
      }
    }
  }

  private World buildWorld() {
    World world = new World(10000);

    for (int i = 0; i < world.getCurrentCapacity(); i++) {
      int x = SolaRandom.nextInt(0, 800);
      int y = SolaRandom.nextInt(0, 600);
      int scale = SolaRandom.nextInt(3, 20);

      int red = SolaRandom.nextInt(0, 256);
      int green = SolaRandom.nextInt(0, 256);
      int blue = SolaRandom.nextInt(0, 256);
      boolean filled = SolaRandom.nextBoolean();

      float velX = SolaRandom.nextFloat(-50, 50);
      float velY = SolaRandom.nextFloat(-50, 50);

      DynamicBodyComponent dynamicBodyComponent = new DynamicBodyComponent(true);

      dynamicBodyComponent.setVelocity(new Vector2D(velX, velY));

      world.createEntity(
        new TransformComponent(x, y, scale),
        dynamicBodyComponent,
        new CircleRendererComponent(new Color(red, green, blue), filled)
      );
    }

    return world;
  }
}
