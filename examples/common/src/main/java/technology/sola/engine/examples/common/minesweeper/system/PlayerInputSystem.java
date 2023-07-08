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
    if (mouseInput.isMouseClicked(MouseButton.PRIMARY) || mouseInput.isMouseClicked(MouseButton.SECONDARY)) {
      Vector2D worldMousePosition = solaGraphics.screenToWorldCoordinate(mouseInput.getMousePosition());

      for (var entry : world.createView().of(TransformComponent.class, MinesweeperSquareComponent.class).getEntries()) {
        Vector2D translate = entry.c1().getTranslate();
        Rectangle squareBounds = new Rectangle(translate, translate.add(new Vector2D(MinesweeperSquareComponent.SQUARE_SIZE - 1, MinesweeperSquareComponent.SQUARE_SIZE - 1)));
        MinesweeperSquareComponent square = entry.c2();

        if (squareBounds.contains(worldMousePosition)) {
          if (mouseInput.isMouseClicked(MouseButton.PRIMARY)) {
            handleReveal(square);
          } else {
            handleFlag(square);
          }
        }
      }
    }
  }

  private void handleReveal(MinesweeperSquareComponent squareComponent) {
    if (!squareComponent.isFlagged()) {
      squareComponent.reveal();

      if (squareComponent.isBomb()) {
        // todo emit loss event
        System.out.println("You lost");
      } else if (squareComponent.getAdjacentCount() == 0) {
        // todo reveal adjacent ones in all directions until numbered are found (or boundary) recursively
      }
    }
  }

  private void handleFlag(MinesweeperSquareComponent squareComponent) {
    if (!squareComponent.isRevealed()) {
      // todo emit event to increase/decrease flag count
      squareComponent.toggleFlag();
    }
  }
}
