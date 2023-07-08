package technology.sola.engine.examples.common.minesweeper.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaGraphics;
import technology.sola.engine.examples.common.minesweeper.components.MinesweeperSquareComponent;
import technology.sola.engine.input.MouseButton;
import technology.sola.engine.input.MouseInput;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

public class PlayerInputSystem extends EcsSystem {
  private final SolaGraphics solaGraphics;
  private final MouseInput mouseInput;

  public PlayerInputSystem(SolaGraphics solaGraphics, MouseInput mouseInput) {
    this.solaGraphics = solaGraphics;
    this.mouseInput = mouseInput;
  }

  @Override
  public void update(World world, float deltaTime) {
    if (mouseInput.isMouseClicked(MouseButton.PRIMARY)) {
      Vector2D worldPosition = solaGraphics.screenToWorldCoordinate(mouseInput.getMousePosition());

      for (var entry : world.createView().of(TransformComponent.class, MinesweeperSquareComponent.class).getEntries()) {
        Vector2D translate = entry.c1().getTranslate();
        Rectangle rectangle = new Rectangle(translate, translate.add(new Vector2D(MinesweeperSquareComponent.SQUARE_SIZE - 1, MinesweeperSquareComponent.SQUARE_SIZE - 1)));
        MinesweeperSquareComponent square = entry.c2();

        if (rectangle.contains(worldPosition)) {
          square.reveal();

          if (square.isBomb()) {
            // todo emit loss event
            System.out.println("You lost");
          } else if (square.getAdjacentCount() == 0) {
            // todo reveal adjacent ones in all directions until numbered are found (or boundary) recursively
          }
        }
      }
    }
  }
}
