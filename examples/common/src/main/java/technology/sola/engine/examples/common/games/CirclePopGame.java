package technology.sola.engine.examples.common.games;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.examples.common.ExampleUtils;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.gui.elements.SectionGuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.TextStyles;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.engine.graphics.gui.style.property.CrossAxisChildren;
import technology.sola.engine.graphics.gui.style.property.MainAxisChildren;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.MouseButton;
import technology.sola.engine.input.TouchPhase;
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
public class CirclePopGame extends Sola {
  private final TextGuiElement scoreGuiElement = new TextGuiElement().setText("0");
  private int score = 0;
  private SolaGraphics solaGraphics;

  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public CirclePopGame() {
    super(new SolaConfiguration("Circle Pop", 800, 600, 30));
  }

  @Override
  protected void onInit() {
    solaGraphics = new SolaGraphics.Builder(platform(), solaEcs)
      .withGui(mouseInput)
      .buildAndInitialize(assetLoaderProvider);

    solaEcs.addSystems(new PhysicsSystem(), new PlayerInputSystem());
    solaEcs.setWorld(buildWorld());

    scoreGuiElement.addStyle(ConditionalStyle.always(
      new TextStyles.Builder<>()
        .setBackgroundColor(Color.WHITE)
        .setPadding(5)
        .setTextColor(Color.BLACK)
        .build()
    ));

    solaGraphics.guiDocument().setRootElement(
      new SectionGuiElement()
        .addStyle(ConditionalStyle.always(new BaseStyles.Builder<>()
          .setWidth("100%")
          .setHeight("100%")
          .setMainAxisChildren(MainAxisChildren.CENTER)
          .setCrossAxisChildren(CrossAxisChildren.CENTER)
          .build())
        )
        .appendChildren(
          ExampleUtils.createReturnToLauncherButton(platform(), eventHub, "0", "0"),
          scoreGuiElement
        )
    );

    platform().getViewport().setAspectMode(AspectMode.STRETCH);
  }

  @Override
  protected void onRender(Renderer renderer) {
    solaGraphics.render(renderer);
  }

  private class PlayerInputSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      Vector2D point = null;

      if (mouseInput.isMousePressed(MouseButton.PRIMARY) || mouseInput.isMouseDragged(MouseButton.PRIMARY)) {
        point = solaGraphics.screenToWorldCoordinate(mouseInput.getMousePosition());
      }

      var touchPoint = touchInput.getFirstTouch();

      if (touchPoint != null && (touchPoint.phase() == TouchPhase.BEGAN || touchPoint.phase() == TouchPhase.MOVED)) {
        point = solaGraphics.screenToWorldCoordinate(new Vector2D(touchPoint.x(), touchPoint.y()));
      }

      if (point != null) {
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
      int scale = SolaRandom.nextInt(5, 30);

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
