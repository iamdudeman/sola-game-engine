package technology.sola.engine.examples.common.minesweeper.graphics;

import technology.sola.ecs.World;
import technology.sola.ecs.view.View;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.graphics.modules.SolaGraphicsModule;
import technology.sola.engine.examples.common.minesweeper.components.MinesweeperSquareComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;

/**
 * {@link SolaGraphicsModule} for rendering {@link technology.sola.ecs.Entity} with {@link MinesweeperSquareComponent}.
 */
public class MinesweeperSquareGraphicsModule extends SolaGraphicsModule<View2Entry<TransformComponent, MinesweeperSquareComponent>> {
  private static final int FONT_OFFSET = 5;

  @Override
  public View<View2Entry<TransformComponent, MinesweeperSquareComponent>> getViewToRender(World world) {
    return world.createView().of(TransformComponent.class, MinesweeperSquareComponent.class);
  }

  @Override
  public void renderMethod(Renderer renderer, View2Entry<TransformComponent, MinesweeperSquareComponent> viewEntry, TransformComponent cameraModifiedEntityTransform) {
    MinesweeperSquareComponent square = viewEntry.c2();
    float x = cameraModifiedEntityTransform.getX();
    float y = cameraModifiedEntityTransform.getY();
    float squareSize = MinesweeperSquareComponent.SQUARE_SIZE;

    if (square.isRevealed()) {
      if (square.isBomb()) {
        renderer.fillRect(x, y, squareSize, squareSize, Color.RED);
        renderer.drawString("X", x + FONT_OFFSET, y, Color.BLACK);
      } else if (square.getAdjacentCount() == 0) {
        renderer.fillRect(x, y, squareSize, squareSize, Color.LIGHT_GRAY);
      } else {
        renderer.fillRect(x, y, squareSize, squareSize, Color.LIGHT_GRAY);
        renderer.drawString(String.valueOf(square.getAdjacentCount()), x + FONT_OFFSET, y, Color.BLACK);
      }
    } else if (square.isFlagged()) {
      renderer.fillRect(x, y, squareSize, squareSize, Color.GREEN);
    } else {
      renderer.fillRect(x, y, squareSize, squareSize, Color.DARK_GRAY);
    }

    renderer.drawRect(x, y, squareSize, squareSize, Color.BLACK);
  }
}
